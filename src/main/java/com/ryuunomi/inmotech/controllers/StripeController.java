package com.ryuunomi.inmotech.controllers;

import com.ryuunomi.inmotech.services.stripe.StripeService;
import com.stripe.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    @Autowired
    private StripeService stripeService;

    @Autowired
    private com.ryuunomi.inmotech.services.suscripcion.ISuscripcionService suscripcionService;

    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(name = "success_url", defaultValue = "http://localhost:5173/suscripcion-exito") String successUrl,
            @RequestParam(name = "cancel_url", defaultValue = "http://localhost:5173/suscripcion-cancelada") String cancelUrl) {

        try {
            String token = authHeader.replace("Bearer ", "");
            String email = com.ryuunomi.inmotech.security.util.JwtUtils.getEmailFromToken(token);

            Long userId = null;
            try {
                var user = com.ryuunomi.inmotech.security.util.JwtUtils.class;
                var usersResp = new org.springframework.web.client.RestTemplate()
                        .getForEntity("http://localhost:8080/api/user?email=" + email, com.ryuunomi.inmotech.dto.UsuarioDTO[].class);
                if (usersResp.getBody() != null && usersResp.getBody().length > 0) {
                    userId = usersResp.getBody()[0].id();
                }
            } catch (Exception e) {
                return ResponseEntity.status(401).body("Usuario no encontrado");
            }

            if (userId == null) {
                return ResponseEntity.status(401).body("No se pudo identificar al usuario");
            }

            String sessionId = stripeService.crearCheckoutSession(userId, successUrl, cancelUrl);
            return ResponseEntity.ok(Map.of("sessionId", sessionId));

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
            return handleWebhookEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private ResponseEntity<?> handleWebhookEvent(Event event) {
        String eventType = event.getType();
        System.out.println("Stripe webhook recibido: " + eventType);

        if ("checkout.session.completed".equals(eventType)) {
            try {
                String sessionId = event.getDataObjectDeserializer().getObject().toString();
                com.stripe.model.checkout.Session sessionObj = com.stripe.model.checkout.Session.retrieve(sessionId.split("@")[0].replace("{", ""));
                String userIdStr = sessionObj.getMetadata().get("userId");
                if (userIdStr != null) {
                    Long userId = Long.parseLong(userIdStr);
                    String subscriptionId = sessionObj.getSubscription();
                    suscripcionService.activarPremium(userId, subscriptionId);
                }
            } catch (Exception e) {
                System.err.println("Error procesando checkout.completed: " + e.getMessage());
            }
        } else if ("customer.subscription.deleted".equals(eventType)) {
            System.out.println("Suscripcion cancelada - volver a plan gratuito");
        }

        return ResponseEntity.ok(Map.of("received", true));
    }
}