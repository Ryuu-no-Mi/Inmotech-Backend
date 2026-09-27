package com.ryuunomi.inmotech.controllers;

import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.StripeObject;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.ryuunomi.inmotech.services.stripe.StripeService;
import com.ryuunomi.inmotech.entities.StripeWebhookEvent;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.repositories.StripeWebhookEventRepository;
import com.ryuunomi.inmotech.repositories.UsuarioRepository;
import com.ryuunomi.inmotech.services.suscripcion.ISuscripcionService;
import com.ryuunomi.inmotech.services.usuario.IUsuarioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    @Autowired
    private StripeService stripeService;

    @Autowired
    private ISuscripcionService suscripcionService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private StripeWebhookEventRepository webhookEventRepository;

    @Value("${app.stripe.success-url:http://localhost:5173/suscripcion-exito}")
    private String successUrl;

    @Value("${app.stripe.cancel-url:http://localhost:5173/suscripcion-cancelada}")
    private String cancelUrl;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(Authentication authentication) {

        try {
            Usuario usuario = usuarioService.findByEmail(authentication.getName())
                    .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
            Session session = stripeService.crearCheckoutSession(usuario.getId(), successUrl, cancelUrl);
            var response = new java.util.HashMap<String, Object>();
            response.put("sessionId", session.getId());
            response.put("checkoutUrl", stripeService.getCheckoutUrl(session));
            if (session.getClientSecret() != null) {
                response.put("clientSecret", session.getClientSecret());
            }
            return ResponseEntity.ok(response);

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al crear sesion de pago: " + e.getMessage()));
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> webhook(@RequestBody String payload,
                                     @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            Event event = stripeService.verificarWebhook(payload, sigHeader);
            if (event.getId() == null || event.getId().isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Stripe event id ausente"));
            }
            if (webhookEventRepository.existsByEventId(event.getId())) {
                return ResponseEntity.ok(Map.of("received", true, "duplicate", true));
            }

            handleWebhookEvent(event);
            webhookEventRepository.save(new StripeWebhookEvent(event.getId()));
            return ResponseEntity.ok(Map.of("received", true));
        } catch (com.stripe.exception.SignatureVerificationException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Firma Stripe invalida"));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.ok(Map.of("received", true, "duplicate", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Webhook no procesado"));
        }
    }

    private void handleWebhookEvent(Event event) {
        String eventType = event.getType();
        StripeObject object = stripeService.getEventObject(event)
                .orElseThrow(() -> new IllegalArgumentException("Stripe object ausente"));

        switch (eventType) {
            case "checkout.session.completed" -> {
                Session session = (Session) object;
                if ("paid".equalsIgnoreCase(session.getPaymentStatus())) {
                    Long userId = stripeService.extraerUserIdDeMetadata(session.getMetadata());
                    activateOrIgnore(userId, session.getSubscription());
                }
            }
            case "customer.subscription.created", "customer.subscription.updated" -> {
                Subscription subscription = (Subscription) object;
                Long userId = stripeService.extraerUserIdDeMetadata(subscription.getMetadata());
                if (userId == null) {
                    userId = findUserIdBySubscription(subscription.getId());
                }
                if ("active".equalsIgnoreCase(subscription.getStatus())
                        || "trialing".equalsIgnoreCase(subscription.getStatus())) {
                    activateOrIgnore(userId, subscription.getId());
                } else if ("canceled".equalsIgnoreCase(subscription.getStatus())
                        || "unpaid".equalsIgnoreCase(subscription.getStatus())
                        || "past_due".equalsIgnoreCase(subscription.getStatus())) {
                    deactivateOrIgnore(userId);
                }
            }
            case "customer.subscription.deleted" -> {
                Subscription subscription = (Subscription) object;
                deactivateOrIgnore(findUserIdBySubscription(subscription.getId()));
            }
            case "invoice.payment_failed" -> {
                Invoice invoice = (Invoice) object;
                String subscriptionId = invoice.getParent() != null
                        && invoice.getParent().getSubscriptionDetails() != null
                        ? invoice.getParent().getSubscriptionDetails().getSubscription()
                        : null;
                deactivateOrIgnore(findUserIdBySubscription(subscriptionId));
            }
            default -> {
                // Eventos no relacionados con el estado local se marcan como recibidos.
            }
        }
    }

    private void activateOrIgnore(Long userId, String subscriptionId) {
        if (userId != null && subscriptionId != null) {
            suscripcionService.activarPremium(userId, subscriptionId);
        }
    }

    private void deactivateOrIgnore(Long userId) {
        if (userId != null) {
            suscripcionService.desactivarPremium(userId);
        }
    }

    private Long findUserIdBySubscription(String subscriptionId) {
        if (subscriptionId == null || subscriptionId.isBlank()) {
            return null;
        }
        return usuarioRepository.findBySuscripcion_StripeSubscriptionId(subscriptionId)
                .map(Usuario::getId)
                .orElse(null);
    }
}
