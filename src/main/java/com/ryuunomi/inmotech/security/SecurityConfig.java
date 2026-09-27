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
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Arrays;
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
        private final org.springframework.security.core.userdetails.UserDetailsService userDetailsService;
        private final SecretKey jwtSecretKey;
        private final String allowedOrigins;

        public SecurityConfig(CustomOAuth2UserService customOAuth2UserService,
                              OAuth2AuthenticationSuccessHandler oAuth2SuccessHandler,
                              org.springframework.security.core.userdetails.UserDetailsService userDetailsService,
                              SecretKey jwtSecretKey,
                              @Value("${app.cors.allowed-origins:http://localhost:5173}") String allowedOrigins) {
            this.customOAuth2UserService = customOAuth2UserService;
            this.oAuth2SuccessHandler = oAuth2SuccessHandler;
            this.userDetailsService = userDetailsService;
            this.jwtSecretKey = jwtSecretKey;
            this.allowedOrigins = allowedOrigins;
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
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .requestMatchers("/oauth2/**", "/login/**", "/api/auth/login", "/api/user/register").permitAll()
                             .requestMatchers("/actuator/health").permitAll()
                             .requestMatchers(HttpMethod.POST, "/api/stripe/webhook").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/property/myProperties", "/api/property/user/**", "/api/property/agency/**").authenticated()
                            .requestMatchers(HttpMethod.GET, "/api/property", "/api/property/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/agency", "/api/agency/*").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/imageProperty/**").permitAll()
                            .requestMatchers("/imagenesPropiedades/**").permitAll()
                            .requestMatchers("/imagenesUsuarios/**").permitAll()
                            .requestMatchers("/imagenes/**").permitAll()
                            .anyRequest().authenticated()
                    )
                    .exceptionHandling(ex -> ex
                            .authenticationEntryPoint((request, response, authException) -> {
                                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                response.setContentType("application/json");
                                response.getWriter().write("{\"error\": \"No autenticado\", \"message\": \"Requiere token JWT\"}");
                            })
                    )
                    .oauth2Login(oauth2 -> oauth2
                            .userInfoEndpoint(userInfo -> userInfo
                                    .userService(customOAuth2UserService)
                            )
                            .successHandler(oAuth2SuccessHandler)
                    )
                    // Filtros JWT: autenticacion primero, luego validacion
                    .addFilter(new JwtAuthenticationFilter(authManager, jwtSecretKey))
                    .addFilterBefore(new JwtValidationFilter(jwtSecretKey, userDetailsService), UsernamePasswordAuthenticationFilter.class)
                    .build();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(Arrays.stream(allowedOrigins.split(","))
                    .map(String::trim)
                    .filter(origin -> !origin.isBlank())
                    .toList());
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE","OPTIONS"));
            config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Stripe-Signature"));
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
