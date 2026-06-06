package com.ryuunomi.inmotech.security.filter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuunomi.inmotech.entities.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.jsonwebtoken.Jwts;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/api/auth/login"); // endpoint de login
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            Map<String, String> credentials = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            String email = credentials.get("email");
            String password = credentials.get("password");

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo las credenciales", e);
        }
    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        // Obtener el UserDetails de Spring Security (el que creas en UserDetailsServiceImpl)
        User user = (User) authResult.getPrincipal(); // Casting seguro a User de Spring Security
        String email = user.getUsername();

        // Obtener los roles (authorities) del UserDetails y convertirlos a Strings
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // Obtiene el String "ROLE_USUARIO", "ROLE_ADMIN", etc.
                .collect(Collectors.toList());

        // Crear los claims para el JWT, incluyendo los roles
        Claims claims = Jwts.claims()
                .setSubject(email)
                .add("roles", roles) // <-- ¡Añadir los roles a los claims!
                .build();

        String token = Jwts.builder()
                .setClaims(claims) // Usar los claims que incluyen los roles
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
                .signWith(TokenJwtConfig.SECRET_KEY)
                .compact();

        // Añadir el token al encabezado de la respuesta (opcional, pero buena práctica)
        response.addHeader(TokenJwtConfig.HEADER_AUTHORIZATION, TokenJwtConfig.PREFIX_TOKEN + token);

        // Preparar el cuerpo JSON de la respuesta para el cliente (Postman, frontend)
        Map<String, Object> body = new HashMap<>(); // Cambiar a Map<String, Object> para roles y ID
        body.put("token", token);
        body.put("email", email);
        body.put("roles", roles);

        response.setContentType(TokenJwtConfig.CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));

    }
}
