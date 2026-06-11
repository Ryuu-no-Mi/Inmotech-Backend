package com.ryuunomi.inmotech.config.stripe;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StripeConfig {

    @Value("${stripe.api.key:}")
    private String apiKey;

    @PostConstruct
    public void init() {
        if (apiKey != null && !apiKey.isEmpty() && !"sk_test_placeholder".equals(apiKey)) {
            Stripe.apiKey = apiKey;
            System.out.println("=== STRIPE: API key configurada correctamente ===");
        } else {
            System.out.println("=== STRIPE: API key NO configurada. Stripe no funcionarÃ¡. Configura STRIPE_API_KEY en variables de entorno ===");
        }
    }
}