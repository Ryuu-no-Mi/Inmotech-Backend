# Despliegue en Render con Aiven

## Servicio

El backend se despliega como un servicio Docker usando el `Dockerfile` del proyecto.

- Build: lo ejecuta el `Dockerfile` con Maven y Java 17.
- Health check: `/actuator/health`.
- Perfil activo: `render`.
- El `DataSeeder` se ejecuta únicamente cuando la tabla `usuario` está vacía.

## Variables de entorno en Render

Configurar estas variables en el servicio de Render. No guardarlas en Git.

```text
SPRING_PROFILES_ACTIVE=render
SPRING_DATASOURCE_URL=jdbc:mysql://inmotech-inmotech.d.aivencloud.com:13085/defaultdb?sslMode=REQUIRED&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=avnadmin
SPRING_DATASOURCE_PASSWORD=<contraseña actual de Aiven>
SPRING_JPA_HIBERNATE_DDL_AUTO=update
JWT_SECRET=<secreto aleatorio de al menos 32 bytes>
CORS_ALLOWED_ORIGINS=<URL pública del frontend>
```

Render puede generar `JWT_SECRET` automáticamente desde `render.yaml`. La contraseña de Aiven debe introducirse como secreto y regenerarse si ha sido compartida fuera del panel de Aiven.

## Primer arranque

1. Crear el servicio web desde el repositorio del backend o usar `render.yaml` como Blueprint.
2. Confirmar las variables de entorno, especialmente la contraseña de Aiven y `CORS_ALLOWED_ORIGINS`.
3. Esperar a que `/actuator/health` responda `UP`.
4. Revisar los logs: el primer arranque crea el esquema y carga el seeder actual.
5. Mantener `SPRING_JPA_HIBERNATE_DDL_AUTO=update` solo durante la inicialización del demo. Después, cambiar a `validate` cuando exista una migración versionada.

## Frontend

En el servicio del frontend definir:

```text
VITE_API_URL=https://<url-publica-del-backend-en-render>
```

La aplicación mantiene `http://localhost:8080` únicamente como fallback de desarrollo local.
