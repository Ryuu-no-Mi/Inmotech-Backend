package com.ryuunomi.inmotech.security;

import com.ryuunomi.inmotech.security.filter.JwtAuthenticationFilter;
import com.ryuunomi.inmotech.security.filter.JwtValidationFilter;
import com.ryuunomi.inmotech.security.oauth2.CustomOAuth2UserService;
import com.ryuunomi.inmotech.security.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Por defecto spring boot security protege por defecto todas las rutas url,
 * sino configuras nada, activa un login con usurario: user y una contraseña aleatoria generada en consola
 * Angular: 4200
 * React: 5173
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

        private final CustomOAuth2UserService customOAuth2UserService;
        private final OAuth2AuthenticationSuccessHandler oAuth2SuccessHandler;

        public SecurityConfig(CustomOAuth2UserService customOAuth2UserService,
                              OAuth2AuthenticationSuccessHandler oAuth2SuccessHandler) {
            this.customOAuth2UserService = customOAuth2UserService;
            this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        }

        @Bean
        AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
            return http
                    .csrf(csrf -> csrf.disable())
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                    .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(authz -> authz
                            .requestMatchers("/oauth2/**", "/login/**").permitAll()
                            .requestMatchers("/api/auth/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/property", "/api/property/**").permitAll()
                            .requestMatchers( "/api/user", "/api/user/**").permitAll()
                            .requestMatchers( "/api/favourite", "/api/favourite/**").permitAll()
                            .requestMatchers("/api/property/**", "/api/agency/**", "/api/imageProperty/**", "/api/imageUser/**").permitAll()
                            .requestMatchers("/imagenesPropiedades/**").permitAll()
                            .requestMatchers("/imagenesUsuarios/**").permitAll()
                            .requestMatchers("/imagenes/**").permitAll()
                            .requestMatchers("/api/auth/login")
                            .hasAnyRole("USUARIO", "AGENTE", "ADMIN")

                    )
                    .oauth2Login(oauth2 -> oauth2
                            .userInfoEndpoint(userInfo -> userInfo
                                    .userService(customOAuth2UserService)
                            )
                            .successHandler(oAuth2SuccessHandler)
                    )
                    // Filtros JWT: autenticacion primero, luego validacion
                    .addFilter(new JwtAuthenticationFilter(authManager))
                    .addFilterBefore(new JwtValidationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .build();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOriginPatterns(List.of("*"));
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE","OPTIONS"));
            config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
            config.setAllowCredentials(true);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", config);
            return source;
        }

    /*
    // Opcion sin proteccion, necesario para el comienzo del desarrollo

    @Bean
    public SecurityFilterChain filterchain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactiva protección CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Permite todas las peticiones sin autenticación
                );

        return http.build();
    }
    */

}