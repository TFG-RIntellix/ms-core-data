# ms-core-data

## 1. Overview

`ms-core-data` is a core microservice in the RIntellix platform for credit risk analysis.

Its main responsibility is to expose a stable API over MongoDB data for:
- Financial requests
- Risk scorings
- What-if simulations

The service follows a Hexagonal Architecture (Ports and Adapters), keeping domain rules independent from transport and persistence concerns.

## 2. Current Service Scope

| Area | Scope |
|------|-------|
| Requests | Read-only (`GET`) |
| Scorings | Read-only (`GET`) |
| Simulations | Full lifecycle (`GET`, `POST`, `PUT`, `PATCH`, `DELETE`) |

Important rule:
- A simulation must be archived before it can be deleted.

## 3. Implemented Capabilities

### 3.1 Requests
- List requests with optional filters (`partyName`, `requestStatus`)
- Get full request detail by ID
- Application-layer orchestration with party data resolution

### 3.2 Scorings
- Get active scoring associated with a request
- Includes risk metrics, input features, and XAI top features

### 3.3 Simulations
- List simulations with optional filters (`requestId`, `partyId`, `partyName`, `archived`)
- Get simulation detail with base scoring comparison
- Create new simulations from frontend-calculated data (stateless persistence)
- Replace simulation template data (full update)
- Archive/unarchive simulation
- Delete archived simulation only

### 3.4 Cross-Cutting
- Centralized exception handling via `GlobalExceptionHandler`
- Consistent structured logging via `LogMessage` constants + Logback

## 4. API Reference

### 4.1 Request Endpoints

| Method | Path | Description | Success |
|--------|------|-------------|---------|
| `GET` | `/api/requests` | List requests with optional filters | `200 OK` |
| `GET` | `/api/requests/{requestId}` | Get request details | `200 OK` |

Query parameters (`GET /api/requests`):
- `partyName` (optional)
- `requestStatus` (optional)

### 4.2 Scoring Endpoints

| Method | Path | Description | Success |
|--------|------|-------------|---------|
| `GET` | `/api/requests/{requestId}/scoring` | Get scoring for request | `200 OK` |

### 4.3 Simulation Endpoints

| Method | Path | Description | Success |
|--------|------|-------------|---------|
| `GET` | `/api/simulations` | List simulations with optional filters | `200 OK` |
| `GET` | `/api/simulations/{simulationId}` | Get simulation details | `200 OK` |
| `POST` | `/api/simulations` | Create simulation | `201 Created` |
| `PUT` | `/api/simulations/{simulationId}` | Replace simulation template | `200 OK` |
| `PATCH` | `/api/simulations/{simulationId}` | Archive/unarchive simulation | `200 OK` |
| `DELETE` | `/api/simulations/{simulationId}` | Delete archived simulation | `200 OK` |

Query parameters (`GET /api/simulations`):
- `requestId` (optional)
- `partyId` (optional)
- `partyName` (optional, applied in application layer)
- `archived` (optional boolean)

Deletion constraint:
- `DELETE /api/simulations/{simulationId}` returns `400 Bad Request` if simulation is not archived.

## 5. Architecture

### 5.1 Architectural Style

Hexagonal Architecture with three core layers:
- Domain
- Application
- Infrastructure

### 5.2 Layer Responsibilities

| Layer | Responsibilities |
|------|-------------------|
| Domain | Entities, value objects, business rules, ports, domain exceptions |
| Application | Use-case orchestration, DTO mapping, cross-aggregate coordination |
| Infrastructure | REST controllers, repository adapters, Spring Data repositories, Mongo entities |

### 5.3 Key Design Decisions

1. Requests and scorings remain read-only in this service.
2. Simulations are writable to support user-driven what-if workflows.
3. Simulation creation is stateless: frontend sends fully computed simulation payload and this service persists it.
4. Deletion follows archive-before-delete policy (`NotArchivedException`).
5. Aggregate orchestration is in the application layer (not repository adapters), preserving SRP and DIP.
6. Simulation `partyName` filtering is performed in memory after resolving party data, because simulation documents store `party_id` only.
7. Contract mapping uses a map-based strategy to avoid large switch-case mapping logic.

## 6. Domain Highlights

### 6.1 Main Aggregates
- `Request`
- `Party` (with `Person`, profiles, and contracts)
- `Scoring`
- `Simulation`

### 6.2 Contract Hierarchy
- `Contract` (abstract)
- `LoanContract`
- `MortgageContract` (extends `LoanContract`)
- `CreditCardContract`

Supported business calculations:
- DTI (Debt-to-Income)
- LTV (Loan-to-Value, mortgage contracts)

### 6.3 Relevant Domain Exceptions
- `EntityNotFoundException`
- `NotArchivedException`
- `RequestPartyMismatchException`

## 7. Project Structure (High-Level)

```text
src/main/java/es/NTTEnterprise/RIntellix/ms_core_data/
|-- MsCoreDataApplication.java
|-- application/
|   |-- dtos/
|   |   |-- input/
|   |   `-- output/
|   |-- mappers/
|   `-- usecases/
|-- domain/
|   |-- entities/
|   |-- enums/
|   |-- exceptions/
|   `-- ports/
|       |-- input/
|       `-- output/
|-- infraestructure/
|   |-- adapters/
|   |   |-- input/
|   |   `-- output/
|   |-- entities/
|   |   `-- embedded/
|   |-- mappers/
|   |-- projections/
|   `-- repository/
`-- utils/
    `-- LogMessage.java
```

## 8. Error Handling

`GlobalExceptionHandler` (`@RestControllerAdvice`) centralizes API error responses.

| Exception | HTTP Status |
|-----------|-------------|
| `EntityNotFoundException` | `404 Not Found` |
| `MethodArgumentNotValidException` | `400 Bad Request` |
| `IllegalArgumentException` | `400 Bad Request` |
| `NotArchivedException` | `400 Bad Request` |
| Any other exception | `500 Internal Server Error` |

Standard response body includes:
- `timestamp`
- `status`
- `error`
- `message`
- `path`

## 9. Logging

Logging stack:
- SLF4J
- Logback (`src/main/resources/logback-spring.xml`)

Output channels:
- Console logs
- Rolling file logs (`logs/ms-core-data.log`)
- Error-only file logs (`logs/ms-core-data-error.log`)

Log messages are centralized in:
- `src/main/java/es/NTTEnterprise/RIntellix/ms_core_data/utils/LogMessage.java`

## 10. Configuration

Main runtime properties (`src/main/resources/application.properties`):

```properties
spring.application.name=ms-core-data
spring.docker.compose.enabled=false
spring.mongodb.uri=mongodb://localhost:27017/RIntellix
spring.data.mongodb.database=RIntellix
```

## 11. Local Development

### 11.1 Prerequisites
- Java 17+
- MongoDB running on `localhost:27017`
- Maven (or Maven Wrapper)

### 11.2 Run

```bash
# Linux/macOS
./mvnw spring-boot:run

# Windows
.\mvnw.cmd spring-boot:run
```

Service default URL:
- `http://localhost:8080`

## 12. Build and Test

### 12.1 Build

```bash
./mvnw clean verify
```

### 12.2 Test Status

Current automated test coverage is minimal in this repository:
- `MsCoreDataApplicationTests.contextLoads()` smoke test

## 13. Implementation Status

### 13.1 Completed
- Hexagonal architecture baseline
- Request read APIs
- Scoring read API
- Simulation full lifecycle (create/update/archive/delete)
- Archive-before-delete enforcement
- Contract hierarchy and DTI/LTV domain logic
- Centralized exception handling
- Layered logging strategy

### 13.2 Pending / Backlog
- Security layer and auth-related status handling (`401/403`)
- Report endpoints and related domain/infrastructure components
- Kafka producer/consumer async scoring flow
- Pagination and sorting for list endpoints
- OpenAPI/Swagger documentation
- Integration tests (for example with Testcontainers)
- Observability improvements (health checks/metrics)

### 13.3 Known Code TODO
- In `SimulationApplicationService.getSimulationDetails()`: base scoring is expected to exist, but current implementation tolerates missing base scoring and continues mapping.

## 14. Recent Milestones (March 2026)

- Aggregate orchestration refactor (application layer ownership)
- Contract hierarchy and financial calculations (DTI/LTV)
- Scoring endpoint implementation
- Simulation read endpoints
- Code format refactor
- Simulation update/archive + centralized exception handling
- Stateless simulation creation + archive-guarded deletion

## 15. Author

Lucia Fernandez Mancebo

- Initial date: February 28, 2026
- Last updated: March 15, 2026
