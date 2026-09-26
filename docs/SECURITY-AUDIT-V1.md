# Inmotech Backend - Security Audit v1

Fecha: 2026-09-10  
Alcance: API REST, autenticacion, autorizacion, ownership, OAuth2, Stripe y configuracion.

## Resumen

La aplicacion tiene una base funcional amplia, pero no debe considerarse lista para produccion. El riesgo principal es la autorizacion: varias rutas sensibles son publicas y los controladores aceptan IDs de usuario, agencia y propiedad enviados por el cliente sin comprobar siempre que correspondan al usuario autenticado.

Se congela el desarrollo de funcionalidades hasta cerrar los hallazgos criticos y altos de este documento.

## Inventario de endpoints

| Endpoint | Acceso actual | Ownership actual | Riesgo principal |
|---|---|---|---|
| `GET /api/property`, `/buscar`, `/facetas`, `/{id}` | Publico | No aplica | Enumeracion y abuso de paginacion |
| `POST /api/property` | `@PreAuthorize`, pero ruta publica | El body controla `idUsuario` e `idAgencia` | IDOR y elusion de limites |
| `PUT /api/property/{id}` | `@PreAuthorize` | Parcial en controlador | Puede reasignar agencia desde el body |
| `DELETE /api/property/{id}` | `@PreAuthorize` | Parcial en controlador | Logica fragil y riesgo de NPE |
| `POST /api/property/{id}/imagenes` | Publico; anotacion comentada | Ninguno | Subida a propiedad ajena |
| `GET /api/property/user/{idUsuario}` | `@PreAuthorize` | No restringe el usuario consultado | IDOR |
| `GET /api/property/agency/{idAgencia}` | `@PreAuthorize` | No restringe la agencia consultada | Exposicion de datos |
| `GET /api/user`, `/{id}` | Publico | No aplica | Enumeracion y exposicion de PII |
| `PUT /api/user/{id}` | `@PreAuthorize` generico | No compara con el usuario autenticado | Modificacion de otra cuenta |
| `DELETE /api/user/{id}`, `/email/{email}` | Publico | Ninguno | Borrado arbitrario de cuentas |
| `POST /api/agency` | Autenticado | El body elige el administrador | Escalada de privilegios |
| `PUT`, `DELETE /api/agency/{id}` | Publico | Ninguno | Modificacion o borrado arbitrario |
| `POST`, `DELETE`, `GET /api/favourite/**` | Publico | IDs libres | Manipulacion de favoritos ajenos |
| `GET`, `POST`, `PUT`, `DELETE /api/inquiry/**` | Autenticacion global como maximo | Ninguno | Lectura y modificacion de consultas ajenas |
| `POST`, `DELETE`, `PUT /api/imageProperty/**` | Publico | Ninguno | Abuso de almacenamiento y datos |
| `GET`, `POST`, `DELETE /api/imageUser/**` | Publico | Ninguno | Manipulacion de imagen de otro usuario |
| `POST /api/subscription/confirmar-premium` | Autenticacion manual | Usuario del token | Premium sin pago |
| `POST /api/stripe/create-checkout-session` | Autenticado | Busqueda de usuario incorrecta | Pago asociado a cuenta equivocada |
| `POST /api/stripe/webhook` | Protegido por JWT | Firma Stripe pendiente | Stripe no puede invocarlo |
| `GET /api/property/myProperties` | Autenticado | Usa `Authentication` | Patron correcto a conservar |

## Hallazgos

| Severidad | Problema | Archivo / referencia | Riesgo | Solucion | Estado |
|---|---|---|---|---|---|
| Critical | Activacion de Premium sin pago | `controllers/SuscripcionController.java:40-52` | Cualquier usuario puede activar Premium | Eliminar endpoint publico; activar solo desde webhook Stripe verificado | Open |
| Critical | Rutas mutables publicas | `security/SecurityConfig.java:65-75` | Permite alcanzar operaciones sensibles sin autenticacion | Aplicar `authenticated()` por defecto y permitir solo lecturas publicas y login/registro/webhook | Open |
| Critical | Borrado arbitrario de usuarios | `controllers/UsuarioController.java:101-117` | Cualquier cliente puede borrar cuentas por ID o email | `DELETE /me` para el propio usuario y admin para borrado administrativo | Open |
| Critical | Actualizacion arbitraria de usuarios | `controllers/UsuarioController.java:84-97` | Un usuario puede cambiar datos, password o agencia de otra cuenta | Comparar identidad autenticada o exigir `ADMIN`; separar DTO de perfil y administrativo | Open |
| Critical | Creacion de propiedad con propietario controlado por cliente | `mapper/PropiedadMapper.java:53-92`, `services/propiedad/PropiedadServiceImpl.java:79-98` | Puede atribuir propiedades a otra cuenta y eludir limites | Ignorar `idUsuario` del DTO y usar el usuario del `SecurityContext` | Open |
| Critical | Agencias sin control de ownership | `controllers/AgenciaController.java:51-143` | Creacion, cambio de admin y borrado arbitrarios; posible escalada a `ADMIN` | Autorizar admin global o administrador de la agencia; nunca confiar en `idUsuarioAdmin` del body | Open |
| Critical | Favoritos sin ownership | `controllers/FavoritoController.java:22-63`, `services/favorito/FavoritoServiceImpl.java:24-49` | Lectura y modificacion de favoritos de cualquier cuenta | Obtener usuario desde `Authentication` e ignorar `userId` del path | Open |
| Critical | Consultas sin ownership | `controllers/ConsultaController.java:23-87`, `services/consulta/ConsultaServiceImpl.java:30-79` | Se pueden leer, crear, cambiar y borrar consultas ajenas | Crear con usuario autenticado y autorizar autor, propietario/agente o admin | Open |
| Critical | Imagenes sin ownership | `controllers/ImagenPropiedadController.java:33-126`, `controllers/ImagenUsuarioController.java:22-62` | Subida, borrado y cambios sobre recursos ajenos | Autorizar propietario/agencia/administrador antes de cada operacion | Open |
| Critical | Webhook bloqueado por JWT | `security/SecurityConfig.java:75`, `controllers/StripeController.java:67-76` | Stripe recibe 401 antes de validar `Stripe-Signature` | Permitir solo esa ruta sin JWT y exigir firma Stripe valida | Open |
| Critical | Secreto JWT hardcodeado | `security/filter/TokenJwtConfig.java:12-13` | Se pueden fabricar tokens y roles | Mover a secreto externo, rotarlo e invalidar tokens antiguos | Open |
| High | CORS abierto con credenciales | `security/SecurityConfig.java:96-106`, `config/WebConfig.java:24-34` | Peticiones desde cualquier origen y configuracion duplicada | Allowlist por entorno y una sola configuracion CORS | Open |
| High | Contraseñas en logs | `services/usuario/UsuarioServiceImpl.java:87-107`, `mapper/UsuarioRegistroMapper.java:54`, `controllers/UsuarioController.java:94` | Filtracion de credenciales en consola o plataforma de logs | Eliminar todos los logs de passwords, tokens y secretos | Open |
| High | Roles confiados desde el JWT | `security/filter/JwtValidationFilter.java:46-60` | Un token robado conserva privilegios aunque cambie la cuenta | Cargar autoridades desde BD o implementar versionado/revocacion | Open |
| High | JWT en query string OAuth2 | `security/oauth2/OAuth2AuthenticationSuccessHandler.java:60-72` | Token en historial, logs, referrer y analitica | Usar codigo temporal de un solo uso o cookie `HttpOnly` segura | Open |
| High | Usuario Stripe resuelto mediante llamada HTTP incorrecta | `controllers/StripeController.java:27-47` | `/api/user?email=...` ignora el filtro y usa el primer usuario | Inyectar servicio/repositorio y usar directamente el sujeto autenticado | Open |
| High | URLs Stripe controladas por cliente | `controllers/StripeController.java:21-25`, `services/stripe/StripeService.java:41-45` | Redireccion a dominios maliciosos tras el pago | Usar URLs fijas desde configuracion y allowlist | Open |
| High | Secreto webhook rechazado por condicion incorrecta | `services/stripe/StripeService.java:61-66` | Un secreto normal `whsec_...` se considera invalido | Validar null, vacio y placeholder, no `startsWith("whsec_")` | Open |
| High | Webhook no idempotente y cancelacion incompleta | `controllers/StripeController.java:79-100` | Reintentos duplican efectos y cancelaciones no quitan Premium | Guardar event IDs procesados y manejar `customer.subscription.deleted` | Open |
| High | Imagen de portada no valida pertenencia | `controllers/ImagenPropiedadController.java:113-126` | Se puede asociar una imagen de otra propiedad | Comprobar `imagen.propiedad.id == propiedad.id` | Open |
| Medium | CSRF desactivado sin politica documentada | `security/SecurityConfig.java:60-64` | Riesgo si OAuth2 o cookies participan en operaciones | Mantener Bearer-only o diseñar politica CSRF explicita | Open |
| Medium | Rutas de imagenes y `@PathVariable` inconsistentes | `controllers/ImagenPropiedadController.java:33-95`, `ImagenUsuarioController.java:22-62` | Endpoints rotos o variables no resolubles | Alinear mappings y variables; probar cada ruta | Open |
| Medium | Tipos de parsing JWT inconsistentes | `JwtValidationFilter.java:40-44`, `JwtUtils.java` | Comportamientos distintos al validar tokens | Centralizar emision y validacion JWT | Open |
| Medium | Subidas de archivos con validacion insuficiente | `services/imagenpropiedad/ImagenPropiedadServiceImpl.java:82-120`, `ImagenUsuarioServiceImpl.java:31-58` | MIME, extension, tamaño y contenido no controlados de forma suficiente | Allowlist de MIME/extensiones, límites, nombre generado y almacenamiento externo o aislado | Open |
| Medium | Configuracion con credenciales y defaults inseguros | `resources/application-mysql.yml:2-6`, `application.properties:3-13` | Credenciales expuestas y schema alterable automaticamente | Variables de entorno, perfiles separados y `ddl-auto=validate` en prod | Open |

## Regla de ownership para v1.0

Los endpoints mutables no deben aceptar al cliente como fuente de identidad. El flujo obligatorio sera:

```text
Authorization: Bearer <token>
        |
        v
SecurityContext / Authentication
        |
        v
Usuario autenticado cargado desde la base de datos
        |
        v
Recurso y relacion de ownership comprobados en servidor
```

El `idUsuario` enviado por body o URL puede servir como referencia administrativa en endpoints expresamente autorizados, pero nunca para decidir la identidad del actor.

## Primer lote de correcciones

El primer lote debe cerrar los bloqueadores que permiten toma de control, escalada o fraude:

1. Rehacer las reglas base de `SecurityConfig`.
2. Eliminar `confirmar-premium` como mecanismo de activacion.
3. Proteger usuarios, agencias, favoritos, consultas e imagenes.
4. Asignar propietario desde `Authentication` al crear propiedades y consultas.
5. Mover el secreto JWT a configuracion externa y eliminar passwords de logs.
6. Separar y corregir el webhook Stripe antes de activar pagos reales.

## Criterio de cierre del audit

Este documento no se considera cerrado hasta que cada hallazgo Critical y High tenga:

- Correccion implementada.
- Test que demuestra el acceso permitido.
- Test que demuestra el acceso denegado.
- Referencia actualizada en este documento.
