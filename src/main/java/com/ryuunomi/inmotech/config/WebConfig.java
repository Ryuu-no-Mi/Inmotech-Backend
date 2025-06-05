package com.ryuunomi.inmotech.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Expone “/imagenesPropiedades/**” como archivos estáticos desde la carpeta “src/main/resources/imagenesPropiedades/”
        registry.addResourceHandler("/imagenesPropiedades/**")
                .addResourceLocations("classpath:/imagenesPropiedades/");
    }
}
