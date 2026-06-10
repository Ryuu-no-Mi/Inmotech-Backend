package com.ryuunomi.inmotech.services.stripe;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StripeService {

    @Value("${stripe.price.id:price_placeholder}")
    private String priceId;

    @Value("${stripe.api.key:}")
    private String apiKey;

    public Session crearCheckoutSession(Long userId, String successUrl, String cancelUrl) throws Exception {
        if (apiKey == null || apiKey.isEmpty() || "sk_test_placeholder".equals(apiKey)) {
            throw new IllegalStateException("Stripe no esta configurado. Configura STRIPE_API_KEY en variables de entorno.");
        }

        if (priceId == null || priceId.isEmpty() || "price_placeholder".equals(priceId)) {
            throw new IllegalStateException("Stripe price_id no configurado. Configura STRIPE_PRICE_ID en variables de entorno.");
        }

        Map<String, String> metadata = new HashMap<>();
        metadata.put("userId", userId.toString());

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setPrice(priceId)
                .setQuantity(1L)
                .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .putAllMetadata(metadata)
                .addLineItem(lineItem)
                .build();

        return Session.create(params);
    }

    public String getCheckoutUrl(Session session) {
        String url = session.getUrl();
        if (url == null || url.isEmpty()) {
            throw new IllegalStateException("Stripe no proporciono URL de checkout. Verifica la configuracion del producto en Stripe.");
        }
        return url;
    }

    public Event verificarWebhook(String payload, String sigHeader) throws SignatureVerificationException {
        String webhookSecret = System.getenv("STRIPE_WEBHOOK_SECRET");
        if (webhookSecret == null || webhookSecret.startsWith("whsec_")) {
            throw new SignatureVerificationException("Webhook secret no configurado", payload);
        }
        return Webhook.constructEvent(payload, sigHeader, webhookSecret);
    }

    public Long extraerUserIdDeSession(Event event) {
        try {
            String json = event.getDataObjectDeserializer().getRawJson();
            if (json == null) return null;
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode node = mapper.readTree(json);
            com.fasterxml.jackson.databind.JsonNode metaNode = node.get("metadata");
            if (metaNode != null && metaNode.has("userId")) {
                return Long.parseLong(metaNode.get("userId").asText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}