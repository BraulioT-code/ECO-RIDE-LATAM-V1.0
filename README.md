# 🚀 ECO-RIDE LATAM: Carpooling Corporativo

Proyecto desarrollado en **Spring Boot** con arquitectura de **microservicios** y la implementación de **Spring Cloud** (Gateway, Eureka, Config)La coordinación de transacciones distribuidas se implementa mediante el **patrón SAGA por Coreografía** a través de **Kafka**.

-----

## 🧭 Progreso del Proyecto

| Parte | Descripción | Estado |
| :--- | :--- | :--- |
| 1️⃣ | **Infraestructura Base** (Docker: Keycloak, Kafka, Postgres x4, Observabilidad) | ✅ Completado |
| 2️⃣ | **Núcleo Spring Cloud** (`Config Server`, `Eureka Server`) | ✅ Completado |
| 3️⃣ | **API Gateway** (Enrutamiento, Seguridad JWT, Configuración) | ✅ Completado |
| 4️⃣ | **Filtro Custom** (Token Relay: `X-User-ID`, `X-User-Roles`) | ✅ Completado |
| 5️⃣ | Esqueleto y Configuración de `PassengerService` (DB y Seguridad) | ✅ Completado |
| 6️⃣ | **Implementación del Flujo Saga** (Trip, Payment, Coreografía) | ⏳ Pendiente |
| 7️⃣ | **Observabilidad** (Tracing, Métricas, Logs) y **Resiliencia** | ⏳ Pendiente |
| 8️⃣ | Pruebas (Unitarias, Integración con Testcontainers, E2E Saga) | ⏳ Pendiente |

-----

## 🧱 Estructura del Proyecto

```
ECO-RIDE-LATAM/
├── config-repo/         # Configuración centralizada (leída por Config Server)
├── deploy/              # Archivos de infraestructura (docker-compose.yml, ecoride-realm.json)
├── config-server/       # 🧠 Cerebro: Servidor de configuración
├── eureka-server/       # ☎️ Directorio: Servidor de descubrimiento
├── gateway/             # 🚪 Puerta: API Gateway (Filtros, Seguridad)
├── passenger-service/   # Microservicio de Perfiles y Reputación 
├── trip-service/        # ❌ Pendiente: Gestión de Viajes y Reservas 
├── payment-service/     # ❌ Pendiente: Gestión de Pagos y Compensaciones 
├── notification-service/ # ❌ Pendiente: Envío de Alertas 
└── README.md
```

-----

## ⚙️ Tecnologías Utilizadas

- **Lenguaje:** Java 17+
- **Framework:** **Spring Boot 3.2.6**
- **Orquestación:** **Spring Cloud 2023.0.0** (Gateway, Eureka, Config, OpenFeign) 
- **Base de Datos:** **PostgreSQL** (una instancia por microservicio) 
- **Mensajería Asíncrona:** **Kafka** (para el patrón Saga) 
- **Identidad y Acceso:** **Keycloak** (OAuth2/OIDC, Roles: `ROLE_DRIVER`, `ROLE_PASSENGER`) 
- **Observabilidad:** Zipkin/Tempo, Prometheus, Grafana 
- **Build Tool:** Maven
- **Infra Dev:** Docker / Docker Compose

-----

## 🐳 Configuración de la Infraestructura

Para levantar toda la infraestructura base de terceros, navega a la carpeta `deploy` y ejecuta:

```
docker compose up -d
```

**End points de la Infraestructura:**

| Servicio | Puerto | Uso |
| :--- | :--- | :--- |
| **Keycloak (Admin)** | `http://localhost:8888` | IAM: Roles, Clientes (`eco-gateway`, `eco-internal`), Usuarios. |
| **Eureka Dashboard** | `http://localhost:8761` | Monitoreo del estado y registro de los microservicios. |
| **Zipkin** | `http://localhost:9411` | Visualización de **Trazas Distribuidas**.  |
| **Prometheus** | `http://localhost:9090` | Recolección de Métricas.  |
| **Grafana** | `http://localhost:3000` | Dashboards de Métricas.  |

## 💻 Microservicios (Arranque)

Los microservicios deben ejecutarse en el orden listado, ya que tienen dependencias entre sí. Utiliza el comando `./mvnw spring-boot:run` dentro de la carpeta de cada módulo.

| Microservicio | Módulo | Puerto | Comando de Ejecución (desde la carpeta del módulo) |
| :--- | :--- | :--- | :--- |
| **Config Server** | `config-server` | 8081 | `./mvnw spring-boot:run` |
| **Eureka Server** | `eureka-server` | 8761 | `./mvnw spring-boot:run` |
| **API Gateway** | `gateway` | 8080 | `./mvnw spring-boot:run` |
| **Passenger Service** | `passenger-service` | 8091 | `./mvnw spring-boot:run` |

-----

## 🎯 Flujos de la Saga (Pendientes)

El proyecto implementa el Patrón Saga por **Coreografía** para la transacción de reserva y pago. El estado actual es el *Kickstart* para la implementación de los eventos. [cite\_start] [cite: 65, 102]

### 1. Caso Exitoso (Flujo Feliz) 

1.  `API Gateway` recibe `POST /api/trips/{id}/reservations`. 
2.  `TripService` crea la reserva como **PENDING**. 
3.  `TripService` envía el evento **`ReservationRequested`** al Broker. 
4.  `PaymentService` consume el evento y **Autoriza el Pago**. 
5.  `PaymentService` envía el evento **`PaymentAuthorized`** al Broker. 
6.  `TripService` consume el evento y **Confirma la reserva** (`confirmed`). 

### 2. Compensación (Falla de Pago) 

1.  `PaymentService` consume `ReservationRequested` y detecta **Falla de Autorización**. 
2.  `PaymentService` envía el evento **`PaymentFailed`** al Broker. 
3.  `TripService` consume el evento y ejecuta la **Compensación** (Cancela la reserva). 
4.  `TripService` envía el evento **`ReservationCancelled`** al Broker. 

-----

**Laboratorio académico: Microservicios y Patrón Saga en Spring Cloud**

Desarrollado por: **Braulio Tovar**

-----