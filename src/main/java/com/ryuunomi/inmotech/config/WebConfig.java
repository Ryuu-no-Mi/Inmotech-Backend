package com.ryuunomi.inmotech.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/imagenesPropiedades/**")
                .addResourceLocations("file:C:/imagenes_inmotech/");

        registry
                .addResourceHandler("/imagenes/usuarios/**")
                .addResourceLocations("file:C:/imagenes_inmotech/usuarios/");

    }
}
