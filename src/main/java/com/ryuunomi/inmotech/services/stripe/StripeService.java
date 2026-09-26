package com.ryuunomi.inmotech.services.stripe;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        SessionCreateParams.SubscriptionData subscriptionData = SessionCreateParams.SubscriptionData.builder()
                .putAllMetadata(metadata)
                .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .putAllMetadata(metadata)
                .setSubscriptionData(subscriptionData)
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
        String webhookSecret = webhookSecret();
        if (webhookSecret == null || webhookSecret.isBlank() || webhookSecret.startsWith("whsec_placeholder")) {
            throw new SignatureVerificationException("Webhook secret no configurado", payload);
        }
        return Webhook.constructEvent(payload, sigHeader, webhookSecret);
    }

    public String webhookSecret() {
        return webhookSecret;
    }

    public Long extraerUserIdDeMetadata(Map<String, String> metadata) {
        if (metadata == null || metadata.get("userId") == null) {
            return null;
        }
        try {
            return Long.valueOf(metadata.get("userId"));
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public Optional<StripeObject> getEventObject(Event event) {
        return event.getDataObjectDeserializer().getObject();
    }

    @Value("${stripe.webhook.secret:}")
    private String webhookSecret;
}
