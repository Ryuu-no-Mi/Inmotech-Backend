package com.ryuunomi.inmotech.security.util;

/**
 * JWT identity is exposed through Spring Security's Authentication object.
 * Token parsing must not be duplicated in controllers.
 */
public final class JwtUtils {

    private JwtUtils() {
    }
}
