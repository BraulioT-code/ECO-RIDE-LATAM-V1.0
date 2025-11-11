# 🚀 ECO-RIDE LATAM: Carpooling Corporativo

Proyecto desarrollado en **Spring Boot 3** con una arquitectura de **microservicios** basada en **Spring Cloud** (Gateway, Eureka, Config) y el **patrón SAGA por Coreografía** para garantizar transacciones distribuidas y compensaciones.

La comunicación asíncrona entre microservicios se realiza mediante **Kafka**.

---

## 🧭 Progreso del Proyecto

| Parte | Descripción | Estado |
| :--- | :--- | :--- |
| 1️⃣ | **Infraestructura Base** (Docker: Keycloak, Kafka, Postgres x4) | ✅ Completado |
| 2️⃣ | **Núcleo Spring Cloud** (Config Server, Eureka Server) | ✅ Completado |
| 3️⃣ | **API Gateway** (Seguridad JWT, Filtro Custom Token Relay, Rutas estables) | ✅ Completado |
| 4️⃣ | **PassengerService** (Conexión DB, Config. Seguridad, Registro Eureka) | ✅ Completado |
| 5️⃣ | **Implementación de TripService** (Esqueleto, Entidades, Kafka Producer) | ⏳ En Curso |
| 6️⃣ | **Implementación de Saga** (Coreografía: Reserva y Pago) | ⏳ Pendiente |
| 7️⃣ | Observabilidad y Resiliencia (Tracing, Métricas, Circuit Breaker) | ⏳ Pendiente |
| 8️⃣ | Pruebas de Integración y E2E | ⏳ Pendiente |

---

## 🧱 Estructura y Puertos del Proyecto

| Servicio | Módulo | Puerto (HTTP) | Base de Datos (PostgreSQL) | Estado |
| :--- | :--- | :--- | :--- | :--- |
| **API Gateway** | `gateway` | **8080** | N/A | ✅ UP |
| **Config Server** | `config-server` | **8081** | N/A | ✅ UP |
| **Eureka Server** | `eureka-server` | **8761** | N/A | ✅ UP |
| **Passenger Service**| `passenger-service` | **8091** | **5433** | ✅ UP |
| **Trip Service** | `trip-service` | **8092** | **5432** | ⏳ Pendiente |
| **Payment Service** | `payment-service` | **8093** | **5434** | ❌ Pendiente |
| **Notification Service** | `notification-service` | **8094** | **5435** | ❌ Pendiente |

---

## ⚙️ Tecnologías Utilizadas

- **Java 17+**
- **Framework:** **Spring Boot 3.2.6**
- **Orquestación:** **Spring Cloud 2023.0.0** (Gateway, Eureka, Config, OpenFeign)
- **Base de Datos:** **PostgreSQL** (una instancia por servicio)
- **Mensajería Asíncrona:** **Kafka** (para el patrón Saga)
- **Identidad y Acceso:** **Keycloak** (OAuth2/OIDC)
- **Observabilidad:** Zipkin/Tempo, Prometheus, Grafana
- **Build Tool:** Maven
- **Infra Dev:** Docker / Docker Compose

---

## 🔑 Próxima Tarea

El próximo objetivo es completar la implementación del **`TripService`** (puerto **8092**). Este servicio es crucial, ya que debe gestionar las entidades `Trip` y `Reservation` e integrar el **Kafka Producer** para emitir el evento **`ReservationRequested`**, dando inicio al Patrón Saga.

-----
**Laboratorio académico: Microservicios y Patrón Saga en Spring Cloud**

Desarrollado por: *Braulio Tovar* , *Jonathan Vega*
-----