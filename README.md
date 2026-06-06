
<p align="center">
  <img src="https://img.shields.io/badge/Java-17-%23ED8B00?logo=openjdk&logoColor=white" alt="Java 17">
  <img src="https://img.shields.io/badge/Spring_Boot-3.4.5-%236DB33F?logo=springboot&logoColor=white" alt="Spring Boot 3.4.5">
  <img src="https://img.shields.io/badge/MySQL-8-%234479A1?logo=mysql&logoColor=white" alt="MySQL">
  <img src="https://img.shields.io/badge/JWT-Security-%23000000?logo=jsonwebtokens&logoColor=white" alt="JWT Security">
  <img src="https://img.shields.io/badge/license-MIT-blue" alt="License MIT">
</p>

<h1 align="center">Inmotech Backend</h1>
<p align="center">API REST para portal inmobiliario - Backend con Spring Boot</p>

---

## Stack Tecnologico

| Categoria | Tecnologia |
|---|---|
| **Lenguaje** | Java 17 |
| **Framework** | Spring Boot 3.4.5 |
| **Persistencia** | Spring Data JPA + Hibernate |
| **Base de datos** | MySQL 8 / MariaDB 10.4 |
| **Seguridad** | Spring Security + JWT (jjwt 0.12.6) |
| **Validacion** | Bean Validation (Hibernate Validator) |
| **Build** | Maven |
| **Testing** | JUnit 5 + Spring Security Test |

## Estructura del Proyecto

```
src/main/java/com/ryuunomi/inmotech/
├── config/              # Configuracion CORS, WebMvc
├── controllers/         # REST Controllers
│   ├── AgenciaController
│   ├── ConsultaController
│   ├── FavoritoController
│   ├── ImagenPropiedadController
│   ├── ImagenUsuarioController
│   ├── PropiedadController
│   └── UsuarioController
├── dto/                 # Data Transfer Objects (Java Records)
├── entities/            # Entidades JPA
│   ├── Agencia
│   ├── Consulta
│   ├── Favorito
│   ├── ImagenPropiedad
│   ├── ImagenUsuario
│   ├── Propiedad
│   └── Usuario
├── enums/               # Enumeraciones
│   ├── CapacidadUsuario  (ADMIN, USUARIO, AGENTE)
│   └── EstadoConsulta    (PENDIENTE, RESPONDIDA, CERRADA, RECHAZADA)
├── exceptions/          # Excepciones personalizadas
├── mapper/              # Mappers DTO <-> Entity
├── repositories/        # Spring Data Repositories
├── security/            # Configuracion de seguridad JWT
│   ├── SecurityConfig
│   ├── filter/          # JwtAuthenticationFilter, JwtValidationFilter
│   ├── detailservice/   # UserDetailsServiceImpl
│   └── util/            # JwtUtils
└── services/            # Logica de negocio
    ├── agencia/
    ├── consulta/
    ├── favorito/
    ├── imagenpropiedad/
    ├── imagenusuario/
    ├── propiedad/
    └── usuario/
```

## Endpoints API

### Autenticacion
| Metodo | Ruta | Auth |
|---|---|---|
| POST | `/api/auth/login` | Publico |

### Usuarios
| Metodo | Ruta | Auth |
|---|---|---|
| GET | `/api/user` | Publico |
| GET | `/api/user/{id}` | Publico |
| GET | `/api/user/me` | Autenticado |
| POST | `/api/user/register` | Publico |
| POST | `/api/user/create` | ADMIN |
| PUT | `/api/user/{id}` | USUARIO, ADMIN, AGENTE |
| DELETE | `/api/user/{id}` | Publico |

### Propiedades
| Metodo | Ruta | Auth |
|---|---|---|
| GET | `/api/property` | Publico |
| GET | `/api/property/{id}` | Publico |
| GET | `/api/property/myProperties` | Autenticado |
| GET | `/api/property/user/{id}` | ADMIN, AGENTE |
| GET | `/api/property/agency/{id}` | Autenticado |
| POST | `/api/property` | USUARIO, ADMIN, AGENTE |
| POST | `/api/property/{id}/imagenes` | USUARIO, ADMIN, AGENTE |
| PUT | `/api/property/{id}` | USUARIO, ADMIN, AGENTE (owner) |
| DELETE | `/api/property/{id}` | USUARIO, ADMIN, AGENTE (owner) |

### Agencias
| Metodo | Ruta | Auth |
|---|---|---|
| GET | `/api/agency` | Publico |
| POST | `/api/agency` | Autenticado |
| PUT | `/api/agency/{id}` | Publico |
| DELETE | `/api/agency/{id}` | Publico |

### Consultas, Favoritos, Imagenes
| Metodo | Ruta | Auth |
|---|---|---|
| GET | `/api/inquiry` / `/api/favourite/{userId}` / `/api/imageProperty` | Publico |
| POST | `/api/inquiry` / `/api/favourite/{uid}/{pid}` | Publico |
| DELETE | `/api/favourite/{uid}/{pid}` | Publico |

## Configuracion

1. **Base de datos**: Crear BD `inmotech` en MySQL/MariaDB
2. **application.properties**:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inmotech
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```
3. Ejecutar: `mvn spring-boot:run`

El servidor arranca en `http://localhost:8080`

## Roadmap

Ver [docs/ROADMAP.md](docs/ROADMAP.md) para el plan completo de features.
