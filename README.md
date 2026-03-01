# ms-core-data

## Overview

**ms-core-data** is a core microservice within the **RIntellix** ecosystem — a credit risk assessment platform designed to streamline and centralize the analysis of risks associated with granting financial products (loans, mortgages, and credit cards) within a banking institution.

This microservice serves as a **Data Access Layer (DAL)** that provides a unified and secure interface for accessing and managing request-related data stored in MongoDB. It acts as a protective abstraction layer between the data persistence mechanism and the consuming microservices, ensuring data integrity, security, and maintainability.

> **Note:** This microservice is a transitional solution while planning the migration towards a fully decoupled microservices architecture where each service manages its own database, reducing inter-service dependencies.

---

## Architecture

### Hexagonal Architecture (Ports & Adapters)

This project follows the **Hexagonal Architecture** pattern (also known as Ports and Adapters), which promotes a clean separation of concerns and makes the application highly testable, maintainable, and adaptable to changes in external systems.

```
┌─────────────────────────────────────────────────────────────────────────┐
│                              INFRASTRUCTURE                              │
│  ┌─────────────────────┐                    ┌─────────────────────────┐ │
│  │   Input Adapters    │                    │    Output Adapters      │ │
│  │  (REST Controller)  │                    │  (Repository Adapter)   │ │
│  └──────────┬──────────┘                    └────────────┬────────────┘ │
│             │                                            │              │
│             ▼                                            ▼              │
│  ┌─────────────────────────────────────────────────────────────────────┐│
│  │                        APPLICATION LAYER                            ││
│  │  ┌──────────────────┐  ┌─────────────────┐  ┌────────────────────┐ ││
│  │  │  Use Cases       │  │    DTOs         │  │    Mappers         │ ││
│  │  │  (Services)      │  │  (Output)       │  │  (DTO Mappers)     │ ││
│  │  └──────────────────┘  └─────────────────┘  └────────────────────┘ ││
│  └─────────────────────────────────────────────────────────────────────┘│
│             │                                            ▲              │
│             ▼                                            │              │
│  ┌─────────────────────────────────────────────────────────────────────┐│
│  │                          DOMAIN LAYER                               ││
│  │  ┌──────────────────┐  ┌─────────────────┐  ┌────────────────────┐ ││
│  │  │    Entities      │  │     Enums       │  │      Ports         │ ││
│  │  │  (Request, Money │  │ (RequestStatus, │  │ (Input & Output    │ ││
│  │  │   RequestDetails)│  │  RequestType)   │  │   Interfaces)      │ ││
│  │  └──────────────────┘  └─────────────────┘  └────────────────────┘ ││
│  └─────────────────────────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────────────────┘
```

### Why Hexagonal Architecture?

1. **Technology Independence**: The domain logic is completely isolated from frameworks, databases, and external services. MongoDB could be replaced with PostgreSQL without modifying the domain layer.

2. **Testability**: Each layer can be tested in isolation using mocks or stubs for the ports.

3. **Flexibility**: New input channels (e.g., message queues, gRPC) or output mechanisms can be added by simply implementing new adapters.

4. **Maintainability**: Business rules live in the domain layer, making them easy to locate, understand, and modify.

---

## Project Structure

```
src/main/java/es/NTTEnterprise/RIntellix/ms_core_data/
├── MsCoreDataApplication.java          # Spring Boot entry point
├── application/                         # APPLICATION LAYER
│   ├── dtos/
│   │   └── output/
│   │       ├── RequestSummaryDTO.java   # Lightweight view for list operations
│   │       └── RequestDetailsDTO.java   # Detailed view for single request
│   ├── mappers/
│   │   ├── RequestSummaryDTOMapper.java # Domain → Summary DTO
│   │   └── RequestDetailsDTOMapper.java # Domain → Details DTO
│   └── usecases/
│       └── RequestApplicationService.java # Business logic orchestration
├── domain/                              # DOMAIN LAYER (Core Business Logic)
│   ├── entities/
│   │   ├── Request.java                 # Aggregate root
│   │   ├── RequestDetails.java          # Value object with request specifics
│   │   ├── PropertyCollateral.java      # Mortgage collateral information
│   │   └── Money.java                   # Value object for monetary amounts
│   ├── enums/
│   │   ├── RequestStatus.java           # PENDIENTE_DE_REVISION, APROBADO, etc.
│   │   ├── RequestType.java             # PRESTAMO, HIPOTECA, TARJETA_CREDITO
│   │   └── Purpose.java                 # COMPRA_VIVIENDA, EDUCACION, etc.
│   ├── exceptions/
│   │   └── EntityNotFoundException.java # Domain-specific exception
│   └── ports/
│       ├── input/
│       │   └── RequestPortService.java  # Input port (use case interface)
│       └── output/
│           └── RequestPortRepository.java # Output port (repository interface)
└── infraestructure/                     # INFRASTRUCTURE LAYER
    ├── adapters/
    │   ├── input/
    │   │   └── RequestControllerAdapter.java # REST API controller
    │   └── output/
    │       └── RequestRepositoryAdapter.java # MongoDB repository adapter
    ├── entities/
    │   └── RequestEntity.java           # MongoDB document mapping
    ├── mappers/
    │   └── RequestMapper.java           # Entity ↔ Domain conversion
    └── repository/
        └── RequestRepository.java       # Spring Data MongoDB interface
```

---

## Layer Responsibilities

### Domain Layer

The **heart of the application** containing pure business logic with no external dependencies.

| Component | Description |
|-----------|-------------|
| `Request` | The aggregate root representing a financial product request. Contains creation date, status, details, and optional collateral. |
| `RequestDetails` | Value object encapsulating request-specific information: type, purpose, amount, term, interest rate, credit limit, and repayment system. |
| `Money` | Value object representing monetary amounts with currency. Ensures type safety and provides arithmetic operations with currency validation. |
| `PropertyCollateral` | Value object for mortgage-specific collateral data (property value, first home indicator). |
| `RequestPortService` | Input port defining the contract for use cases: `listRequests()` and `getRequestDetails()`. |
| `RequestPortRepository` | Output port defining the contract for data persistence operations. |

### Application Layer

Orchestrates the flow of data between the domain and the outside world.

| Component | Description |
|-----------|-------------|
| `RequestApplicationService` | Implements `RequestPortService`. Coordinates fetching data from the repository, applying business rules, and mapping to DTOs. |
| `RequestSummaryDTO` | Lightweight DTO for list views containing: status, type, amount, currency, and dates. |
| `RequestDetailsDTO` | Comprehensive DTO for detailed views including all request information and party data (pending implementation). |
| `RequestSummaryDTOMapper` | Transforms `Request` domain entities to `RequestSummaryDTO`. |
| `RequestDetailsDTOMapper` | Transforms `Request` domain entities to `RequestDetailsDTO`. |

### Infrastructure Layer

Handles all external concerns: HTTP, database, serialization, etc.

| Component | Description |
|-----------|-------------|
| `RequestControllerAdapter` | REST controller exposing endpoints. Implements the input adapter pattern by delegating to `RequestPortService`. |
| `RequestRepositoryAdapter` | Implements `RequestPortRepository`. Bridges the domain with Spring Data MongoDB by converting between `RequestEntity` and `Request`. |
| `RequestEntity` | MongoDB document mapping with `@Document` and `@Field` annotations. Represents the persistence model. |
| `RequestMapper` | Bidirectional mapper between `RequestEntity` (infrastructure) and `Request` (domain). |
| `RequestRepository` | Spring Data MongoDB repository with custom `@Query` methods for filtering. |

---

## API Endpoints

### List Requests

```http
GET /api/requests
GET /api/requests?partyName={name}
GET /api/requests?requestStatus={status}
GET /api/requests?partyName={name}&requestStatus={status}
```

**Response:** `200 OK` with `List<RequestSummaryDTO>`

```json
[
  {
    "requestId": "507f1f77bcf86cd799439011",
    "status": "PENDIENTE_DE_REVISION",
    "requestType": "HIPOTECA",
    "amount": 250000.00,
    "currency": "EUR",
    "creationDate": "2026-02-15",
    "lastReviewDate": null
  }
]
```

### Get Request Details

```http
GET /api/requests/{requestId}
```

**Response:** `200 OK` with `RequestDetailsDTO`

```json
{
  "requestId": "507f1f77bcf86cd799439011",
  "requestDate": "2026-02-15",
  "requestType": "HIPOTECA",
  "status": "PENDIENTE_DE_REVISION",
  "requestedAmount": 250000.00,
  "currency": "EUR",
  "requestTermMonths": 360,
  "interestRate": 2.5,
  "purpose": "COMPRA_VIVIENDA"
}
```

---

## Domain Model

### Request Types

| Type | Description |
|------|-------------|
| `PRESTAMO` | Personal loan |
| `HIPOTECA` | Mortgage (requires collateral) |
| `TARJETA_CREDITO` | Credit card |

### Request Status Workflow

```
PENDIENTE_DE_REVISION → REVISADO → APROBADO
                               └→ RECHAZADO
```

### Purpose Categories

- `COMPRA_VIVIENDA` - Home purchase
- `MEJORA_VIVIENDA` - Home improvement
- `COMPRA_VEHICULO` - Vehicle purchase
- `REFORMA_HOGAR` - Home renovation
- `EDUCACION` - Education
- `SALUD` - Healthcare
- `CONSOLIDACION_DEUDA` - Debt consolidation
- `ELECTRODOMESTICOS` - Appliances
- `TECNOLOGIA` - Technology
- `VIAJES` - Travel
- `OTROS` - Other purposes

---

## Technical Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Programming language |
| Spring Boot | 4.0.3 | Application framework |
| Spring Data MongoDB | - | MongoDB integration |
| MongoDB | Latest | Document database |
| Maven | - | Build tool & dependency management |

---

## Configuration

### application.properties

```properties
spring.application.name=ms-core-data
spring.docker.compose.enabled=false
spring.mongodb.uri=mongodb://localhost:27017/RIntellix
spring.data.mongodb.database=RIntellix
```

---

## Design Decisions & Rationale

### 1. Read-Only Microservice

This microservice is intentionally designed as **read-only** for the current phase. Write operations will be handled by dedicated microservices to maintain single responsibility and reduce complexity.

### 2. Dual DTO Strategy

- **RequestSummaryDTO**: Optimized for list views, containing only essential fields to minimize payload size and improve performance.
- **RequestDetailsDTO**: Complete information for detail views, including party information (pending implementation).

### 3. Dynamic Query Filtering

The repository supports optional filtering using Spring Data MongoDB's `@Query` annotation with SpEL expressions, allowing null parameters to be ignored:

```java
@Query("{ $and: [ " +
       "{ $or: [ { $expr: { $eq: [:#{#partyName}, null] } }, { 'party.name': :#{#partyName} } ] }, " +
       "{ $or: [ { $expr: { $eq: [:#{#status}, null] } }, { 'status': :#{#status} } ] } " +
       "] }")
List<RequestEntity> findWithFilters(@Param("partyName") String partyName, @Param("status") String status);
```

### 4. Null-Safe Mappers

Mappers implement null checks to handle optional fields gracefully, preventing `NullPointerException` when domain objects have incomplete data:

```java
requestSummaryDTO.setAmount(
    request.getRequestDetails().getRequestedAmount() != null 
        ? request.getRequestDetails().getRequestedAmount().getAmount() 
        : null
);
```

---

## Running the Application

### Prerequisites

- Java 17+
- MongoDB running on `localhost:27017`
- Maven (or use the included wrapper)

### Start the Application

```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Or on Windows
.\mvnw.cmd spring-boot:run
```

The application will start on `http://localhost:8080`.

---

## TODO List

### High Priority

- [ ] **Add Party Information to Requests**
  - Implement `Party` and `Person` domain entities
  - Create relationship between `Request` and `Party`
  - Add party data to `RequestSummaryDTO` (partyName)
  - Complete `RequestDetailsDTO` party fields (NIF, phone, email, address, laboral situation, income)
  - Update mappers to include party information

### Medium Priority

- [ ] **Improve Data Validations**
  - Add input validation annotations (`@Valid`, `@NotNull`, `@NotBlank`)
  - Implement custom validators for business rules
  - Add validation for monetary amounts (positive values, valid currencies)
  - Validate request status transitions
  - Add DTO validation at controller level

- [ ] **Implement Comprehensive Logging System**
  - Configure structured logging with a clear, visual format
  - Implement log file generation for error traceability
  - Add correlation IDs for request tracking
  - Log request/response payloads (sanitized)
  - Create different log levels for each layer (DEBUG, INFO, WARN, ERROR)
  - Consider using ELK stack (Elasticsearch, Logstash, Kibana) for log aggregation

### Low Priority

- [ ] **Enhance Mappers Based on Request Type**
  - Differentiate DTO fields based on `RequestType`:
    - `PRESTAMO`: Show term, interest rate, repayment system
    - `HIPOTECA`: Include collateral information (property value, first home)
    - `TARJETA_CREDITO`: Show credit limit, revolving flag
  - Consider polymorphic DTOs or conditional field inclusion
  - Implement a mapper strategy pattern for type-specific mapping

### Future Enhancements

- [ ] Implement pagination for list requests
- [ ] Add sorting capabilities
- [ ] Implement caching layer (Redis)
- [ ] Add API versioning
- [ ] Implement health checks and metrics (Actuator)
- [ ] Add OpenAPI/Swagger documentation
- [ ] Implement integration tests with Testcontainers
- [ ] Add security layer (JWT validation via API Gateway)

---

## Author

**Lucía Fernández Mancebo**

*Date: February 28, 2026* 