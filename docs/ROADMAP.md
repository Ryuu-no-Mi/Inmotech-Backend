# Roadmap - Inmotech Backend

> Plan maestro de implementacion de features para la API REST de Inmotech

---

## Fase 1: Google OAuth

| # | Tarea | Estado |
|---|---|---|
| 1.1 | Anadir `spring-boot-starter-oauth2-client` al pom.xml | COMPLETADO |
| 1.2 | Crear entidad `OAuth2Provider` (GOOGLE) y campos `provider` + `providerId` en `Usuario` | COMPLETADO |
| 1.3 | Modificar `SecurityConfig` con `oauth2Login()` y `OAuth2AuthenticationSuccessHandler` | COMPLETADO |
| 1.4 | Crear `CustomOAuth2UserService` (busca/registra usuario con datos de Google) | COMPLETADO |
| 1.5 | Generar JWT propio tras login con Google | COMPLETADO |
| 1.6 | Externalizar secretos OAuth2 a `application-dev.yml` | COMPLETADO |

---

## Fase 2: Limites de Suscripcion

| # | Tarea | Estado |
|---|---|---|
| 2.1 | Crear entidad `Suscripcion` (id, usuario, tipo, fechaInicio, fechaFin, stripeId) | COMPLETADO |
| 2.2 | Crear entidad `Plan` (nombre, maxPropUsuario, maxPropAgencia, precio, stripePriceId) | COMPLETADO |
| 2.3 | Anadir campos `suscripcion` a `Usuario` y `plan` a `Agencia` | COMPLETADO |
| 2.4 | Crear `SubscriptionService` con logica de limites | COMPLETADO |
| 2.5 | Validar limite en `PropiedadController.createProperty()` | COMPLETADO |
| 2.6 | Endpoint `GET /api/subscription/limits` | COMPLETADO |
| 2.7 | Error 402 "Payment Required" al exceder limite gratuito | COMPLETADO |

---

## Fase 3: Pagos con Stripe

| # | Tarea | Estado |
|---|---|---|
| 3.1 | Anadir `stripe-java` al pom.xml | PENDIENTE |
| 3.2 | Configurar `StripeClient` con API key | PENDIENTE |
| 3.3 | Crear `StripeService` (checkout session + webhook handler) | PENDIENTE |
| 3.4 | Endpoint `POST /api/stripe/create-checkout-session` | PENDIENTE |
| 3.5 | Endpoint `POST /api/stripe/webhook` | PENDIENTE |
| 3.6 | Manejar eventos: `checkout.session.completed`, `customer.subscription.deleted` | PENDIENTE |
| 3.7 | Gestion de cancelacion y renovacion de suscripciones | PENDIENTE |

---

## Fase 4: Email + Alertas

| # | Tarea | Estado |
|---|---|---|
| 4.1 | Anadir `spring-boot-starter-mail` y `spring-boot-starter-thymeleaf` | PENDIENTE |
| 4.2 | Configurar SMTP Gmail en `application-dev.yml` | PENDIENTE |
| 4.3 | Crear `EmailService` generico con plantillas Thymeleaf | PENDIENTE |
| 4.4 | Templates: `bienvenida.html`, `nueva-propiedad-alerta.html`, `confirmacion-suscripcion.html` | PENDIENTE |
| 4.5 | Emails transaccionales: bienvenida, confirmacion suscripcion premium | PENDIENTE |
| 4.6 | Crear entidad `Alerta` (usuario, ciudad, provincia, precioMin, precioMax, superficieMin) | PENDIENTE |
| 4.7 | CRUD de alertas: `POST /api/alerts`, `GET /api/alerts/mis-alertas` | PENDIENTE |
| 4.8 | `AlertaScheduler` (@Scheduled) que busque propiedades nuevas y envie emails | PENDIENTE |

---

## Fase 5: IA

### 5A - Recomendaciones Inteligentes

| # | Tarea | Estado |
|---|---|---|
| 5A.1 | Crear `RecommendationService` (contenido + colaborativo) | PENDIENTE |
| 5A.2 | Endpoint `GET /api/property/recommendations?userId=X` | PENDIENTE |
| 5A.3 | Endpoint `GET /api/property/{id}/similar` | PENDIENTE |

### 5B - Chatbot RAG

| # | Tarea | Estado |
|---|---|---|
| 5B.1 | Anadir `spring-ai-openai` o cliente HTTP OpenAI | PENDIENTE |
| 5B.2 | Crear `ChatbotService` (busca propiedades en BD -> construye prompt -> llama a GPT) | PENDIENTE |
| 5B.3 | Endpoint `POST /api/ai/chat` | PENDIENTE |

### 5C - Generacion de Descripciones

| # | Tarea | Estado |
|---|---|---|
| 5C.1 | Endpoint `POST /api/ai/generate-description` | PENDIENTE |
| 5C.2 | Endpoint `POST /api/ai/enhance-property/{id}` | PENDIENTE |

---

## Fase 6: Mejoras de Calidad

| # | Tarea | Estado |
|---|---|---|
| 6.1 | Anadir Lombok y refactorizar entidades | PENDIENTE |
| 6.2 | Anadir `@ControllerAdvice` para manejo global de excepciones | PENDIENTE |
| 6.3 | Anadir `springdoc-openapi` (Swagger UI) | PENDIENTE |
| 6.4 | Crear perfiles `application-dev.yml` y `application-prod.yml` | PENDIENTE |
| 6.5 | Externalizar todas las claves a variables de entorno | PENDIENTE |

---

## Estrategia de Ramas

```
main
├── feat/oauth2-google
├── feat/limites-suscripcion
├── feat/stripe-pagos
├── feat/email-alertas
├── feat/ia-backend
└── feat/mejoras-backend
```

Cada rama se mergea via PR (squash merge) a `main`.

---

## Estado Global

| Fase | Estado |
|---|---|
| Fase 1 - Google OAuth | COMPLETADO |
| Fase 2 - Limites Suscripcion | COMPLETADO |
| Fase 3 - Stripe Pagos | PENDIENTE |
| Fase 4 - Email + Alertas | PENDIENTE |
| Fase 5 - IA | PENDIENTE |
| Fase 6 - Mejoras Calidad | PENDIENTE |
