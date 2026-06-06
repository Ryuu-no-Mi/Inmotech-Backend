package com.ryuunomi.inmotech.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuunomi.inmotech.security.filter.TokenJwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${app.oauth2.redirect-uri:http://localhost:5173/oauth2/callback}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        Long userId = null;
        Object idAttr = attributes.get("id");
        if (idAttr instanceof Integer) {
            userId = ((Integer) idAttr).longValue();
        } else if (idAttr instanceof Long) {
            userId = (Long) idAttr;
        }

        List<String> roles = oAuth2User.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Claims claims = Jwts.claims()
                .setSubject(email)
                .add("roles", roles)
                .build();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(TokenJwtConfig.SECRET_KEY)
                .compact();

        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("email", email);
        body.put("roles", roles);
        if (userId != null) {
            body.put("userId", userId);
        }

        String jsonBody = new ObjectMapper().writeValueAsString(body);
        String encoded = URLEncoder.encode(jsonBody, StandardCharsets.UTF_8);
        String targetUrl = redirectUri + "?data=" + encoded;

        response.sendRedirect(targetUrl);
    }
}
