package com.ryuunomi.inmotech.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import com.ryuunomi.inmotech.security.filter.TokenJwtConfig;

public class JwtUtils {

    public static String getEmailFromToken(String token) {
        return getAllClaims(token).getSubject(); // suponiendo que el "sub" es el email
    }

    private static Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(TokenJwtConfig.SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
