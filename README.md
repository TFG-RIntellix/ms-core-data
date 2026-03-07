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
│  │ (REST Controllers)  │                    │  (Repository Adapters)  │ │
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
│  │  │  (Request, Party │  │ (RequestStatus, │  │ (Input & Output    │ ││
│  │  │   Person, Money) │  │  PartyType...)  │  │   Interfaces)      │ ││
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
│   │       ├── RequestSummaryDTO.java       # Lightweight view for list operations
│   │       ├── RequestDetailsDTO.java       # Detailed view for single request
│   │       ├── ScoringDTO.java              # Complete scoring data for a request
│   │       ├── SimulationSummaryDTO.java    # Summary view for simulation listings
│   │       └── SimulationDetailsDTO.java    # Detailed view with base/simulated comparison
│   ├── mappers/
│   │   ├── RequestSummaryDTOMapper.java     # Domain → Summary DTO
│   │   ├── RequestDetailsDTOMapper.java     # Domain → Details DTO
│   │   ├── ScoringDTOMapper.java            # Domain → Scoring DTO
│   │   └── SimulationDTOMapper.java         # Domain → Simulation Summary/Details DTO
│   └── usecases/
│       ├── RequestApplicationService.java   # Request business logic orchestration
│       ├── ScoringApplicationService.java   # Scoring business logic orchestration
│       └── SimulationApplicationService.java # Simulation business logic orchestration
├── domain/                              # DOMAIN LAYER (Core Business Logic)
│   ├── entities/
│   │   ├── Request.java                 # Aggregate root
│   │   ├── RequestDetails.java          # Value object with request specifics
│   │   ├── PropertyCollateral.java      # Mortgage collateral information
│   │   ├── Money.java                   # Value object for monetary amounts
│   │   ├── Party.java                   # Party aggregate (customer)
│   │   ├── Person.java                  # Person entity with identity and profiles
│   │   ├── ContactInfo.java             # Value object for contact data
│   │   ├── FinancialProfile.java        # Value object for financial data
│   │   ├── SocioDemographicProfile.java # Value object for demographic data
│   │   ├── Contract.java                # Abstract base for all contract types
│   │   ├── LoanContract.java            # PRESTAMO (extends Contract)
│   │   ├── MortgageContract.java        # HIPOTECA (extends LoanContract)
│   │   ├── CreditCardContract.java      # TARJETA_CREDITO (extends Contract)
│   │   ├── Scoring.java                 # Scoring aggregate (risk assessment results)
│   │   ├── RiskMetrics.java             # Value object for PD, LGD, EAD, ECL, risk grade
│   │   ├── RiskFeature.java             # Value object for XAI feature contributions
│   │   ├── ModelInputs.java             # Value object for model input features
│   │   └── Simulation.java              # Simulation aggregate (what-if scenarios)
│   ├── enums/
│   │   ├── RequestStatus.java           # PENDIENTE_DE_REVISION, APROBADO, etc.
│   │   ├── RequestType.java             # PRESTAMO, HIPOTECA, TARJETA_CREDITO
│   │   ├── Purpose.java                 # COMPRA_VIVIENDA, EDUCACION, etc.
│   │   ├── ContractType.java            # PRESTAMO, HIPOTECA, TARJETA_CREDITO
│   │   ├── ContractStatus.java          # ACTIVO, PAGADO, EN_MORA
│   │   ├── PartyType.java               # INDIVIDUAL, COMPANY
│   │   ├── Gender.java                  # MALE, FEMALE, OTHER
│   │   ├── MaritalStatus.java           # SINGLE, MARRIED, DIVORCED, WIDOWED
│   │   ├── Education.java               # PRIMARY, SECONDARY, BACHELOR, etc.
│   │   ├── EmploymentStatus.java        # EMPLOYED, SELF_EMPLOYED, etc.
│   │   └── HomeOwnership.java           # OWNER, RENTER, FAMILY, OTHER
│   ├── exceptions/
│   │   └── EntityNotFoundException.java # Domain-specific exception
│   └── ports/
│       ├── input/
│       │   ├── RequestPortService.java    # Input port (Request use cases)
│       │   ├── ScoringPortService.java    # Input port (Scoring use cases)
│       │   └── SimulationPortService.java # Input port (Simulation use cases)
│       └── output/
│           ├── RequestPortRepository.java   # Output port for Request operations
│           ├── PartyPortRepository.java     # Output port for Party operations
│           ├── ContractPortRepository.java  # Output port for Contract operations
│           ├── ScoringPortRepository.java   # Output port for Scoring operations
│           └── SimulationPortRepository.java # Output port for Simulation operations
└── infraestructure/                     # INFRASTRUCTURE LAYER
    ├── adapters/
    │   ├── input/
    │   │   ├── RequestControllerAdapter.java    # REST controller for Request endpoints
    │   │   ├── ScoringControllerAdapter.java    # REST controller for Scoring endpoints
    │   │   └── SimulationControllerAdapter.java # REST controller for Simulation endpoints
    │   └── output/
    │       ├── RequestRepositoryAdapter.java     # Request MongoDB adapter (SRP: only Request)
    │       ├── PartyRepositoryAdapter.java       # Party MongoDB adapter (loads contracts into aggregate)
    │       ├── ContractRepositoryAdapter.java    # Contract MongoDB adapter (SRP: only Contract)
    │       ├── ScoringRepositoryAdapter.java     # Scoring MongoDB adapter
    │       └── SimulationRepositoryAdapter.java  # Simulation MongoDB adapter
    ├── entities/
    │   ├── RequestEntity.java               # MongoDB document mapping for requests
    │   ├── PartyEntity.java                 # Party MongoDB document
    │   ├── ContractEntity.java              # Contract MongoDB document (union of all types)
    │   ├── ScoringEntity.java               # Scoring MongoDB document
    │   ├── SimulationEntity.java            # Simulation MongoDB document
    │   └── embedded/
    │       ├── ContactInfoEntity.java       # Embedded contact data
    │       ├── DemographicsEntity.java      # Embedded demographics
    │       ├── EconomicDataEntity.java      # Embedded economic data
    │       ├── EmploymentEntity.java        # Embedded employment data
    │       ├── CreditHistoryEntity.java     # Embedded credit history
    │       ├── ResultsEntity.java           # Embedded scoring results (PD, LGD, EAD, ECL)
    │       ├── InputFeaturesEntity.java     # Embedded model input features
    │       ├── XaiEntity.java               # Embedded XAI top features
    │       ├── TopFeatureEntity.java        # Embedded single XAI feature contribution
    │       ├── FormChangesEntity.java       # Embedded simulation form changes
    │       ├── SimulatedResultsEntity.java  # Embedded simulated risk metrics
    │       └── DeltaEntity.java             # Embedded simulation delta comparison
    ├── mappers/
    │   ├── RequestMapper.java           # Entity ↔ Domain (includes partyId mapping)
    │   ├── PartyMapper.java             # Party Entity ↔ Domain (includes toPartialDomain)
    │   ├── ContractMapper.java          # Contract Entity → Domain (Map-based strategy, no switches)
    │   ├── ScoringMapper.java           # Scoring Entity → Domain
    │   └── SimulationMapper.java        # Simulation Entity → Domain (FormChanges→HashMap, etc.)
    ├── projections/
    │   └── PartyNameProjection.java     # Spring Data projection for efficient name queries
    └── repository/
        ├── RequestRepository.java       # Spring Data MongoDB (dynamic SpEL filtering)
        ├── PartyRepository.java         # Party Spring Data (with projection query)
        ├── ContractRepository.java      # Contract Spring Data (by partyId queries)
        ├── ScoringRepository.java       # Scoring Spring Data (by requestId query)
        └── SimulationRepository.java    # Simulation Spring Data (dynamic SpEL filtering)
├── utils/                               # UTILITIES
│   └── LogMessage.java                  # Centralized log message constants
```

---

## Layer Responsibilities

### Domain Layer

The **heart of the application** containing pure business logic with no external dependencies.

| Component | Description |
|-----------|-------------|
| `Request` | The aggregate root representing a financial product request. Contains creation date, status, details, `partyId` reference (for lazy loading), `party` association (populated when needed), and optional collateral. |
| `RequestDetails` | Value object encapsulating request-specific information: type, purpose, amount, term, interest rate, credit limit, and repayment system. |
| `Money` | Value object representing monetary amounts with currency. Ensures type safety and provides arithmetic operations with currency validation. |
| `PropertyCollateral` | Value object for mortgage-specific collateral data (property value, first home indicator). |
| `Party` | Aggregate representing a customer (individual or company). Contains party type and person details with business methods like `getTotalDebt()` and `getGlobalDTI()`. |
| `Person` | Entity containing identity data (firstName, lastName, NIF), demographic profile, financial profile, contact info, and active contracts. |
| `ContactInfo` | Value object for contact information: phone number, email, and physical address. |
| `FinancialProfile` | Value object containing income data, employment status, occupation, seniority, and credit history information. |
| `SocioDemographicProfile` | Value object with birth date, gender, marital status, education level, home ownership, country of residence, and number of dependants. |
| `Contract` | **Abstract base class** for all financial contracts. Defines `calculateMonthlyPayment()` and `getOutstandingDebt()` as abstract methods, enabling polymorphic DTI calculation without switch statements. |
| `LoanContract` | Concrete entity for PRESTAMO contracts. Extends `Contract`. Monthly payment comes directly from the database (pre-calculated French amortization). Also serves as base class for `MortgageContract`. |
| `MortgageContract` | Concrete entity for HIPOTECA contracts. **Extends `LoanContract`** (a mortgage IS-A loan with additional property fields). Adds `propertyValue`, `isFirstHome`, and LTV calculation. |
| `CreditCardContract` | Concrete entity for TARJETA_CREDITO contracts. Extends `Contract`. Calculates monthly payment based on revolving mode: non-revolving = `currentBalance / 12`; revolving = French system over 12 months with interest. |
| `RequestPortService` | Input port defining the contract for use cases: `listRequests()` and `getRequestDetails()`. |
| `RequestPortRepository` | Output port defining the contract for Request data persistence operations. |
| `PartyPortRepository` | Output port defining the contract for Party retrieval operations. |
| `Scoring` | Aggregate representing the risk assessment result of a request. Contains model results (`RiskMetrics`), input features (`ModelInputs`), XAI explanations (`RiskFeature` list), decision, and metadata. |
| `RiskMetrics` | Value object encapsulating the core risk indicators: PD (Probability of Default), LGD (Loss Given Default), EAD (Exposure At Default), ECL (Expected Calculated Loss), and risk grade. |
| `ModelInputs` | Value object containing the input features fed to the AI model for scoring calculation. |
| `RiskFeature` | Value object representing a single XAI feature contribution (feature name, value, contribution weight). |
| `ScoringPortService` | Input port defining the contract for scoring use cases: `getScoring()`. |
| `ScoringPortRepository` | Output port defining the contract for Scoring persistence operations. Provides `findByRequestId()` and `findById()`. |
| `Simulation` | Aggregate representing a what-if simulation scenario derived from a base scoring. Contains modified inputs (`formChanges`), recalculated results (`simulatedResults`), simulated decision, and computed deltas (`pdChange`, `elChange`, `riskGradeChange`). Party is transient, resolved at the application layer. |
| `SimulationPortService` | Input port defining the contract for simulation use cases: `listSimulations()` and `getSimulationDetails()`. |
| `SimulationPortRepository` | Output port defining the contract for Simulation persistence operations. Provides `findById()` and `findWithFilters()` (dynamic filtering by requestId and partyId). |

### Application Layer

Orchestrates the flow of data between the domain and the outside world.

| Component | Description |
|-----------|-------------|
| `RequestApplicationService` | Implements `RequestPortService`. **Orchestrates Request and Party aggregates** at the application layer. For listings, resolves party names via `PartyPortRepository.findPartyName()`. For details, resolves full party data via `PartyPortRepository.findById()`. This ensures proper separation of concerns following hexagonal architecture. |
| `RequestSummaryDTO` | Lightweight DTO for list views containing: status, type, amount, currency, and dates. |
| `RequestDetailsDTO` | Comprehensive DTO including: request info, party name, NIF, phone, email, address, employment status, and income. |
| `RequestSummaryDTOMapper` | Transforms `Request` domain entities to `RequestSummaryDTO`. |
| `RequestDetailsDTOMapper` | Transforms `Request` domain entities to `RequestDetailsDTO`, including all party information. |
| `ScoringApplicationService` | Implements `ScoringPortService`. Orchestrates scoring retrieval for a request. Delegates to `ScoringPortRepository.findByRequestId()`. |
| `ScoringDTO` | DTO containing the complete scoring data: model results, input features, XAI top features, decision, and metadata. |
| `ScoringDTOMapper` | Transforms `Scoring` domain entities to `ScoringDTO`. |
| `SimulationApplicationService` | Implements `SimulationPortService`. **Orchestrates Simulation, Party, and Scoring aggregates**. For listings, resolves party names post-fetch via `PartyPortRepository.findPartyName()` and applies case-insensitive party name filtering in-memory. For details, resolves base scoring via `ScoringPortRepository.findById()` for comparison. |
| `SimulationSummaryDTO` | Lightweight DTO for simulation list views: simulation ID, scenario name, party name, request ID, simulation date. |
| `SimulationDetailsDTO` | Comprehensive DTO including: modified values (formChanges), base scoring results, simulated results, simulated decision, and computed deltas (pdChange, elChange, riskGradeChange). |
| `SimulationDTOMapper` | Transforms `Simulation` domain entities to `SimulationSummaryDTO` and `SimulationDetailsDTO`. For detail DTO, also receives the base `Scoring` for comparison fields. |

### Infrastructure Layer

Handles all external concerns: HTTP, database, serialization, etc.

| Component | Description |
|-----------|-------------|
| `RequestControllerAdapter` | REST controller exposing endpoints. Implements the input adapter pattern by delegating to `RequestPortService`. |
| `RequestRepositoryAdapter` | Implements `RequestPortRepository`. **Single Responsibility**: Only handles Request aggregate persistence operations. Does NOT resolve Party data — that responsibility belongs to the application layer. Bridges the domain with Spring Data MongoDB by converting between `RequestEntity` and `Request`. |
| `PartyRepositoryAdapter` | Implements `PartyPortRepository`. **Single Responsibility**: Only handles Party aggregate operations. Provides two retrieval modes: `findById()` for full party data, and `findPartyName()` for optimized name-only queries using MongoDB projections. |
| `RequestEntity` | MongoDB document mapping with `@Document` and `@Field` annotations. Represents the Request persistence model. |
| `PartyEntity` | MongoDB document mapping for parties with embedded documents for demographics, contact info, employment, economic data, and credit history. |
| `ContactInfoEntity` | Embedded document for storing contact information within PartyEntity. |
| `DemographicsEntity` | Embedded document for demographic data (name, NIF, birth date, gender, etc.). |
| `EconomicDataEntity` | Embedded document for economic/financial information. |
| `EmploymentEntity` | Embedded document for employment details. |
| `CreditHistoryEntity` | Embedded document for credit history tracking. |
| `RequestMapper` | Bidirectional mapper between `RequestEntity` (infrastructure) and `Request` (domain). |
| `PartyMapper` | Bidirectional mapper between `PartyEntity` (infrastructure) and `Party`/`Person` (domain). Includes `toPartialDomain()` method for creating lightweight Party objects with only name data. |
| `RequestRepository` | Spring Data MongoDB repository with custom `@Query` methods for filtering. |
| `PartyRepository` | Spring Data MongoDB repository for Party retrieval operations. Includes optimized projection query `findPartyNameProjectionById()` using MongoDB's `fields` projection to retrieve only `demographics.first_name` and `demographics.last_name`. |
| `ContractEntity` | MongoDB document mapping for the "contracts" collection. Uses a single-document structure (union of all contract-type fields) since MongoDB stores all types in the same collection with discriminated fields by `contract_type`. |
| `ContractMapper` | Maps `ContractEntity` → domain `Contract` hierarchy using a **Map-based strategy pattern**: a `Map<String, Function<ContractEntity, Contract>>` dispatches by `contract_type` without switches. To add a new type: create a subclass, add a mapping method, register in the map. |
| `ContractRepository` | Spring Data MongoDB repository for contracts. Provides `findByPartyId()` and `findByPartyIdAndStatus()` queries. |
| `ContractRepositoryAdapter` | Implements `ContractPortRepository`. Bridges domain with Contract persistence, delegating to `ContractRepository` and `ContractMapper`. |
| `PartyRepositoryAdapter` | Now also loads active contracts as part of the Party aggregate via `ContractPortRepository.findActiveByPartyId()`, attaching them to `Person.activeContracts`. |
| `ScoringControllerAdapter` | REST controller exposing Scoring endpoints at `/api/requests/{id}/scoring`. Input adapter for the Scoring aggregate. |
| `ScoringRepositoryAdapter` | Implements `ScoringPortRepository`. Handles Scoring persistence with `findByRequestId()` (ObjectId conversion) and `findById()`. |
| `ScoringEntity` | MongoDB document mapping for the `scorings` collection. Contains embedded documents: `ResultsEntity`, `InputFeaturesEntity`, `XaiEntity`, `TopFeatureEntity`. |
| `ScoringMapper` | Maps `ScoringEntity` → `Scoring` domain, including nested embedded entities to domain value objects. |
| `ScoringRepository` | Spring Data MongoDB repository for scorings. Provides `findByRequestId(ObjectId)` query. |
| `SimulationControllerAdapter` | REST controller exposing Simulation endpoints at `/api/simulations`. Input adapter for the Simulation aggregate. |
| `SimulationRepositoryAdapter` | Implements `SimulationPortRepository`. Handles String→ObjectId conversion for dynamic query parameters. Follows SRP: party resolution is at application layer. |
| `SimulationEntity` | MongoDB document mapping for the `simulations` collection. Contains embedded documents: `FormChangesEntity`, `SimulatedResultsEntity`, `DeltaEntity`. |
| `FormChangesEntity` | Embedded entity mapping the `form_changes` sub-document (modified inputs: annual_income, term_months, amount, interest_rate, nr_dependants, repayment_system, employment_status). |
| `SimulatedResultsEntity` | Embedded entity mapping the `simulated_results` sub-document (PD, LGD, EAD, ECL, risk_grade, decision). |
| `DeltaEntity` | Embedded entity mapping the `delta` sub-document (pd_change, el_change, risk_grade_change). |
| `SimulationMapper` | Maps `SimulationEntity` → `Simulation` domain: `FormChanges`→`HashMap<String,Object>`, `SimulatedResults`→`RiskMetrics`+decision, `Delta`→individual fields. Uses `putIfNotNull` for sparse form changes. |
| `SimulationRepository` | Spring Data MongoDB repository for simulations. Uses dynamic SpEL filtering with `$and`/`$or` null-skipping pattern (same as `RequestRepository`). |

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
  "purpose": "COMPRA_VIVIENDA",
  "partyName": "Juan García López",
  "partyNIF": "12345678A",
  "partyPhoneNumber": "+34 612 345 678",
  "partyEmail": "juan.garcia@email.com",
  "partyAddress": "Calle Mayor, 15, Madrid",
  "partyLaboralSituation": "EMPLOYED",
  "partyIncome": "45000.00 EUR"
}
```

### Get Scoring for a Request

```http
GET /api/requests/{requestId}/scoring
```

**Response:** `200 OK` with `ScoringDTO`

```json
{
  "scoringId": "665f1a2b3c4d5e6f7a8b9c0d",
  "requestId": "507f1f77bcf86cd799439011",
  "decision": "APPROVED",
  "scoringDate": "2026-03-01T14:30:00",
  "pd": 0.032,
  "lgd": 0.45,
  "ead": 250000.00,
  "ecl": 3600.00,
  "riskGrade": "B+",
  "inputFeatures": { ... },
  "topFeatures": [
    { "featureName": "annual_income", "featureValue": 45000, "contribution": 0.28 },
    { "featureName": "dti_ratio", "featureValue": 0.32, "contribution": -0.15 }
  ]
}
```

### List Simulations

```http
GET /api/simulations
GET /api/simulations?requestId={id}
GET /api/simulations?partyName={name}
GET /api/simulations?partyId={id}
GET /api/simulations?requestId={id}&partyName={name}&partyId={id}
```

**Response:** `200 OK` with `List<SimulationSummaryDTO>`

```json
[
  {
    "simulationId": "665f1a2b3c4d5e6f7a8b9c0d",
    "scenarioName": "Higher income scenario",
    "partyName": "Juan García López",
    "requestId": "507f1f77bcf86cd799439011",
    "simulationDate": "2026-03-02T10:15:00"
  }
]
```

### Get Simulation Details

```http
GET /api/simulations/{simulationId}
```

**Response:** `200 OK` with `SimulationDetailsDTO`

```json
{
  "simulationId": "665f1a2b3c4d5e6f7a8b9c0d",
  "scenarioName": "Higher income scenario",
  "simulationDate": "2026-03-02T10:15:00",
  "requestId": "507f1f77bcf86cd799439011",
  "baseScoringId": "665f1a2b3c4d5e6f7a8b9c0e",
  "formChanges": {
    "annual_income": 60000.00,
    "term_months": 240
  },
  "basePd": 0.045,
  "baseLgd": 0.45,
  "baseEad": 250000.00,
  "baseEcl": 5062.50,
  "baseRiskGrade": "B",
  "simulatedPd": 0.032,
  "simulatedLgd": 0.45,
  "simulatedEad": 250000.00,
  "simulatedEcl": 3600.00,
  "simulatedRiskGrade": "B+",
  "simulatedDecision": "APPROVED",
  "pdChange": -0.013,
  "elChange": -1462.50,
  "riskGradeChange": "B → B+"
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

## Logging System

### Overview

The microservice implements a comprehensive logging system using **SLF4J** with **Logback** as the underlying logging framework. All log messages are centralized in a utility class to ensure consistency and maintainability.

### Architecture

```
┌─────────────────────────────────────────────────────────────────────────┐
│                          LOGGING ARCHITECTURE                           │
│  ┌─────────────────────────────────────────────────────────────────────┐│
│  │                        utils/LogMessage.java                        ││
│  │           Centralized log message constants for all layers          ││
│  └─────────────────────────────────────────────────────────────────────┘│
│                                    │                                    │
│     ┌──────────────────────────────┼──────────────────────────────┐    │
│     ▼                              ▼                              ▼    │
│  ┌──────────────┐         ┌────────────────┐         ┌──────────────┐  │
│  │  Controller  │         │ Application    │         │  Repository  │  │
│  │   Adapter    │────────▶│   Service      │────────▶│   Adapter    │  │
│  │   (INFO)     │         │   (DEBUG)      │         │   (DEBUG)    │  │
│  └──────────────┘         └────────────────┘         └──────────────┘  │
│                                                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐│
│  │                   logback-spring.xml Configuration                  ││
│  │  - Console Appender (colored output)                                ││
│  │  - File Appender (rolling, 10MB max, 30 days retention)             ││
│  │  - Error File Appender (errors only)                                ││
│  └─────────────────────────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────────────────┘
```

### Log Message Categories

| Category | Layer | Level | Example Messages |
|----------|-------|-------|------------------|
| `CONTROLLER_*` | Infrastructure (Input) | INFO/WARN & ERROR| Request received, Response sent, Validation errors |
| `SERVICE_*` | Application | DEBUG | Operation start/end, Validation, Mapping |
| `REPOSITORY_*` | Infrastructure (Output) | DEBUG | Database operations, Entity mapping |
| `REPOSITORY_CONTRACT_*` | Infrastructure (Output) | DEBUG | Contract loading by partyId |
| `DOMAIN_*` | Domain | DEBUG | DTI, LTV, total debt, monthly payment calculations |
| `MAPPER_*` | Infrastructure | DEBUG | Object conversions |
| `EXCEPTION_*` | All | WARN/ERROR | Entity not found, Illegal arguments |

### Configuration

The logging configuration is defined in `src/main/resources/logback-spring.xml`:

- **Console Output**: Colored, human-readable format
- **File Output**: `logs/ms-core-data.log` (rolling, 10MB max file size)
- **Error File**: `logs/ms-core-data-error.log` (errors only)
- **Retention**: 30 days, 1GB total size cap

### Log Levels by Layer

| Layer | Default Level | Description |
|-------|---------------|-------------|
| Controllers | INFO & ERROR| Request/Response tracking |
| Services | DEBUG | Business logic flow |
| Repository Adapters | DEBUG | Database operations |
| Spring Framework | INFO | Framework internals |
| MongoDB Driver | WARN | Database driver messages |

### Example Log Flow

```
2026-03-01 10:15:23.456 INFO  [http-nio-8080-1] RequestControllerAdapter : Request received: GET /api/requests
2026-03-01 10:15:23.457 DEBUG [http-nio-8080-1] RequestControllerAdapter : Request parameters - partyName: [John], requestStatus: [null]
2026-03-01 10:15:23.458 DEBUG [http-nio-8080-1] RequestApplicationService : Starting listRequests operation with filters - partyName: [John], requestStatus: [null]
2026-03-01 10:15:23.459 DEBUG [http-nio-8080-1] RequestRepositoryAdapter : Executing findWithFilters operation - partyName: [John], requestStatus: [null]
2026-03-01 10:15:23.485 DEBUG [http-nio-8080-1] RequestRepositoryAdapter : findWithFilters operation completed - Retrieved 3 entity(ies)
2026-03-01 10:15:23.486 DEBUG [http-nio-8080-1] RequestApplicationService : listRequests operation completed - Found 3 request(s)
2026-03-01 10:15:23.487 INFO  [http-nio-8080-1] RequestControllerAdapter : Response sent successfully - Status: 200 - Items count: 3
```

### Adding New Log Messages

To maintain consistency, all new log messages should be added to `LogMessage.java`:

```java
// In utils/LogMessage.java
public static final String MY_NEW_LOG = "Description with placeholders: {} and {}";

// Usage in code
log.info(LogMessage.MY_NEW_LOG, value1, value2);
```

---

## TODO List

### High Priority

- [x] **Add Party Information to Requests** ✅
  - ~~Implement `Party` and `Person` domain entities~~
  - ~~Create relationship between `Request` and `Party`~~
  - ~~Add party data to `RequestSummaryDTO` (partyName)~~ ✅
  - ~~Complete `RequestDetailsDTO` party fields (NIF, phone, email, address, laboral situation, income)~~
  - ~~Update mappers to include party information~~
  - ~~Implement `PartyPortRepository` and `PartyRepositoryAdapter`~~
  - ~~Create `PartyEntity` with embedded documents for MongoDB~~
  - ~~Add new domain value objects: `ContactInfo`, `FinancialProfile`, `SocioDemographicProfile`, `Contract`~~
  - ~~Implement `PartyMapper` for entity-domain conversion~~

- [x] **Architectural Refactoring: Aggregate Orchestration** ✅
  - ~~Move aggregate orchestration from Infrastructure to Application layer~~
  - ~~Add `partyId` field to `Request` domain entity for lazy loading~~
  - ~~Remove `PartyRepositoryAdapter` dependency from `RequestRepositoryAdapter`~~
  - ~~Implement efficient projection queries for `findPartyName()`~~
  - ~~Apply SRP: Each adapter handles only its own aggregate~~
  - ~~Apply DIP: Application layer depends on interfaces, not concrete classes~~

### Medium Priority

- [ ] **Improve Data Validations**
  - Add input validation annotations (`@Valid`, `@NotNull`, `@NotBlank`)
  - Implement custom validators for business rules
  - Add validation for monetary amounts (positive values, valid currencies)
  - Validate request status transitions
  - Add DTO validation at controller level

- [x] **Implement Comprehensive Logging System** ✅
  - ~~Configure structured logging with a clear, visual format~~
  - ~~Implement log file generation for error traceability~~
  - Add correlation IDs for request tracking (future enhancement)
  - Log request/response payloads (sanitized) (future enhancement)
  - ~~Create different log levels for each layer (DEBUG, INFO, WARN, ERROR)~~
  - Consider using ELK stack (Elasticsearch, Logstash, Kibana) for log aggregation (future enhancement)

### Low Priority

- [x] **Implement Contract Hierarchy & DTI/LTV Calculations** ✅
  - ~~Model Contract as abstract base with polymorphic subtypes (LoanContract, MortgageContract, CreditCardContract)~~
  - ~~MortgageContract extends LoanContract (shared monthly payment logic from DB)~~
  - ~~CreditCardContract with revolving/non-revolving monthly payment formulas~~
  - ~~DTI = Σ(monthly payments) / Gross Monthly Income~~
  - ~~LTV = Outstanding Balance / Property Value (mortgages only)~~
  - ~~Map-based strategy pattern in ContractMapper (no switches)~~
  - ~~ContractEntity, ContractRepository, ContractRepositoryAdapter~~
  - ~~Load contracts into Party aggregate via ContractPortRepository~~
  - ~~DEBUG-level logging for DTI, LTV, total debt, monthly payment calculations~~

- [ ] **Enhance Mappers Based on Request Type**
  - Differentiate DTO fields based on `RequestType`:
    - `PRESTAMO`: Show term, interest rate, repayment system
    - `HIPOTECA`: Include collateral information (property value, first home)
    - `TARJETA_CREDITO`: Show credit limit, revolving flag
  - Consider polymorphic DTOs or conditional field inclusion
  - Implement a mapper strategy pattern for type-specific mapping

### High Priority

- [x] **Implement Scoring, Simulation & Report Endpoints** (partial ✅)
  - **Scoring** ✅
    - [x] ~~Define `Scoring` domain entity (with `RiskMetrics`, `ModelInputs`, `RiskFeature`)~~
    - [x] ~~Create `ScoringEntity` (infrastructure) with embedded entities (`ResultsEntity`, `InputFeaturesEntity`, `XaiEntity`, `TopFeatureEntity`) and `ScoringMapper`~~
    - [x] ~~Create `ScoringRepository` (Spring Data MongoDB) and `ScoringPortRepository` (output port)~~
    - [x] ~~Implement `ScoringRepositoryAdapter` (with `findByRequestId` and `findById`)~~
    - [x] ~~Create `ScoringDTO` (output) and `ScoringDTOMapper` (application)~~
    - [x] ~~Implement `GET api/requests/{id}/scoring` — recovers the active scoring linked to a request~~
    - [ ] Define error handling: 401/403 (auth) — pending security layer implementation
  - **Simulations** ✅ (read-only endpoints)
    - [x] ~~Define `Simulation` domain entity (fields: id, requestId, partyId, baseScoringId, scenarioName, simulationDate, formChanges, simulatedResults, delta)~~
    - [x] ~~Create `SimulationEntity` with embedded entities (`FormChangesEntity`, `SimulatedResultsEntity`, `DeltaEntity`) and `SimulationMapper`~~
    - [x] ~~Create `SimulationRepository` (dynamic SpEL filtering) and `SimulationPortRepository`~~
    - [x] ~~Implement `SimulationRepositoryAdapter` (String→ObjectId conversion, SRP-compliant)~~
    - [x] ~~Create `SimulationSummaryDTO`, `SimulationDetailsDTO` and `SimulationDTOMapper`~~
    - [x] ~~Implement `GET api/simulations` — lists simulations with filtering by requestId, partyName, partyId~~
    - [x] ~~Implement `GET api/simulations/{id}` — retrieves simulation detail with base scoring comparison~~
    - [ ] `POST api/simulations` — persists a new simulation generated by ms-risk-engine *(deferred: write operations belong to a future phase)*
    - [ ] `PATCH api/simulations/{id}` — partial update (e.g. status transitions) *(deferred)*
    - [ ] `DELETE api/simulations/{id}` — deletes a simulation *(deferred)*
    - [ ] Define error handling: 401/403 (auth) — pending security layer implementation
  - **Reports** *(deferred — final phase of implementation)*
    - [ ] Define `Report` domain entity
    - [ ] Create `ReportEntity`, `ReportMapper`, `ReportRepository`, `ReportPortRepository`, `ReportRepositoryAdapter`
    - [ ] Create `ReportDTO`, `ReportCreateDTO`, `ReportDTOMapper`
    - [ ] Implement `POST /reports` — persists a new report generated by ms-reporting
    - [ ] Implement `GET /reports?requestId={id}&scoringId={id}` — retrieves the report linked to a scoring
    - [ ] Define error handling: 400, 404, 409, 401/403, 500

- [ ] **Implement Kafka Producer & Consumer (Async Scoring Flow)**
  - **Infrastructure setup**
    - [ ] Add Spring for Apache Kafka dependency to `pom.xml`
    - [ ] Configure Kafka broker connection, topics, serializer/deserializer in `application.properties`
    - [ ] Define topic names as constants (e.g. `scoring.request`, `scoring.result`)
  - **Producer (outbound event → AI model input)**
    - [ ] Define `ScoringRequestEvent` DTO with the fields required as input for the AI model
    - [ ] Create output port `ScoringEventPublisher` (domain/ports/output)
    - [ ] Implement `KafkaScoringEventPublisher` adapter (infrastructure/adapters/output)
    - [ ] Wire the producer into the application service: when a scoring is triggered, publish the event to Kafka
    - [ ] Add structured logging for event publication (success / failure)
  - **Consumer (inbound event → scoring persistence)**
    - [ ] Create `KafkaScoringResultConsumer` (infrastructure/adapters/input) with `@KafkaListener`
    - [ ] Define `ScoringResultEvent` DTO with the scoring result payload
    - [ ] On message received: map event → domain entity → persist via `ScoringPortRepository` (same flow as a POST)
    - [ ] Add idempotency guard (e.g. check if scoring already exists for the given requestId)
    - [ ] Add structured logging for event consumption and persistence
  - **Testing & resilience**
    - [ ] Verify end-to-end async flow: produce event → consume result → verify persistence
    - [ ] Define error/retry strategy (DLQ — Dead Letter Queue) for failed consumptions
    - [ ] Document Kafka topics, event schemas, and async flow in README

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

## Recent Updates (March 2026)

### Architectural Refactoring: Aggregate Orchestration ✅ (March 2, 2026)

**Problem Identified:**
The initial implementation had `RequestRepositoryAdapter` directly depending on `PartyRepositoryAdapter` to resolve Party data. This violated:
- **Single Responsibility Principle (SRP)**: The adapter was handling two aggregates
- **Dependency Inversion Principle (DIP)**: Depending on a concrete class instead of an interface
- **Hexagonal Architecture**: Infrastructure-to-infrastructure coupling bypassed the domain

**Solution Applied:**

#### 1. Domain Layer Changes

| Change | Description |
|--------|-------------|
| `Request.partyId` | Added `partyId` field as a simple reference (String). Enables lazy loading pattern where Party is resolved only when needed. |

```java
// Request.java - New field
private String partyId;      // Reference to Party by ID (lazy loading)
private Party party;         // Populated when needed by application layer
```

#### 2. Infrastructure Layer Changes

| Change | Description |
|--------|-------------|
| `RequestRepositoryAdapter` | **Removed** dependency on `PartyRepositoryAdapter`. Now follows SRP by handling only Request persistence. |
| `RequestMapper` | Maps `partyId` from entity to domain for later resolution. |
| `PartyRepository` | Added projection query using MongoDB's `fields` for efficient name-only retrieval. |
| `PartyMapper.toPartialDomain()` | Creates lightweight Party with only Person name data. |

```java
// RequestRepositoryAdapter - Before (WRONG)
public class RequestRepositoryAdapter {
    private final PartyRepositoryAdapter partyRepositoryAdapter; // ❌ Concrete dependency
    // ... fetched and set party inside adapter
}

// RequestRepositoryAdapter - After (CORRECT)
public class RequestRepositoryAdapter {
    private final RequestRepository requestRepository;           // ✅ Only its own dependencies
    private final RequestMapper requestMapper;
    // ... only maps Request, returns partyId for later resolution
}
```

#### 3. Application Layer Changes

| Change | Description |
|--------|-------------|
| `RequestApplicationService.listRequests()` | After retrieving Requests, iterates and resolves Party names via `PartyPortRepository.findPartyName()` (interface dependency). |
| `RequestApplicationService.getRequestDetails()` | Resolves full Party via `PartyPortRepository.findById()` (interface dependency). |

```java
// RequestApplicationService - Orchestration at correct layer
public List<RequestSummaryDTO> listRequests(String partyName, String requestStatus) {
    List<Request> requests = requestPortRepository.findWithFilters(partyName, requestStatus);
    
    // Orchestration: Resolve Party at application layer
    requests.forEach(request -> {
        if (request.getPartyId() != null) {
            request.setParty(partyPortRepository.findPartyName(request.getPartyId())); // ✅ Interface
        }
    });
    
    return requests.stream().map(requestSummaryDTOMapper::toDTO).toList();
}
```

### Why This Architecture Is Optimal

#### 1. Single Responsibility Principle (SRP) ✅

Each adapter handles **only its own aggregate**:

| Adapter | Responsibility |
|---------|---------------|
| `RequestRepositoryAdapter` | Request persistence operations only |
| `PartyRepositoryAdapter` | Party persistence operations only |
| `RequestApplicationService` | **Orchestration** between aggregates |

#### 2. Dependency Inversion Principle (DIP) ✅

Application layer depends on **abstractions (interfaces)**, not concrete implementations:

```
RequestApplicationService
         │
         ├─── PartyPortRepository (interface) ◄─── PartyRepositoryAdapter (concrete)
         │
         └─── RequestPortRepository (interface) ◄─── RequestRepositoryAdapter (concrete)
```

#### 3. Hexagonal Architecture Compliance ✅

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         APPLICATION LAYER                                │
│   ┌─────────────────────────────────────────────────────────────────┐   │
│   │              RequestApplicationService                          │   │
│   │                                                                 │   │
│   │  1. Get Requests (via RequestPortRepository interface)          │   │
│   │  2. For each request, resolve Party (via PartyPortRepository)   │   │
│   │  3. Map to DTOs and return                                      │   │
│   └─────────────────────────────────────────────────────────────────┘   │
│                    │                              │                      │
│                    ▼                              ▼                      │
│           ┌─────────────────┐            ┌─────────────────┐            │
│           │ RequestPort     │            │ PartyPort       │            │
│           │ Repository      │            │ Repository      │            │
│           │ (OUTPUT PORT)   │            │ (OUTPUT PORT)   │            │
│           └────────┬────────┘            └────────┬────────┘            │
└────────────────────┼──────────────────────────────┼─────────────────────┘
                     │                              │
┌────────────────────┼──────────────────────────────┼─────────────────────┐
│                    ▼                              ▼                      │
│           ┌─────────────────┐            ┌─────────────────┐            │
│           │ Request         │            │ Party           │            │
│           │ Repository      │            │ Repository      │            │
│           │ Adapter         │            │ Adapter         │            │
│           └─────────────────┘            └─────────────────┘            │
│                                                                         │
│                        INFRASTRUCTURE LAYER                             │
└─────────────────────────────────────────────────────────────────────────┘
```

#### 4. Query Efficiency ✅

| Use Case | Query Strategy | Data Retrieved |
|----------|----------------|----------------|
| List Requests | `findPartyName()` with MongoDB projection | Only `firstName` + `lastName` |
| Request Details | `findById()` | Complete Party data |

This avoids over-fetching: listings don't load full Party documents, saving bandwidth and memory.

### Contract Hierarchy & DTI/LTV Calculations ✅ (March 2–3, 2026)

**Objective:** Model the different financial contract types (PRESTAMO, HIPOTECA, TARJETA_CREDITO) as domain entities within the Party aggregate, and implement DTI (Debt-To-Income) and LTV (Loan-To-Value) calculations using polymorphism instead of switch statements.

#### Domain Model: Contract Hierarchy

```
Contract (abstract)
├── calculateMonthlyPayment(): Money   [abstract]
├── getOutstandingDebt(): Money         [abstract]
│
├── LoanContract (PRESTAMO)
│   ├── monthlyPayment from DB (French amortization, pre-calculated)
│   └── getOutstandingDebt() → outstandingBalance
│
├── MortgageContract extends LoanContract (HIPOTECA)
│   ├── Inherits calculateMonthlyPayment() and getOutstandingDebt()
│   ├── propertyValue, isFirstHome
│   └── getLTV() = outstandingBalance / propertyValue
│
└── CreditCardContract (TARJETA_CREDITO)
    ├── Non-revolving: M = currentBalance / 12
    └── Revolving: M = currentBalance × [i(1+i)^12 / ((1+i)^12 − 1)]
```

**Key design decision:** `MortgageContract extends LoanContract` because a mortgage IS-A loan with additional property-related fields. Both share the same monthly payment logic (stored directly from the database), avoiding code duplication.

#### DTI Calculation Formula

```
DTI = Σ(monthly payments from all active contracts) / Gross Monthly Income

Where:
  Gross Monthly Income = Annual Income / 12

Monthly payment per contract type:
  - PRESTAMO / HIPOTECA → monthly_payment (from DB, pre-calculated with French system)
  - TARJETA_CREDITO (non-revolving) → current_balance / 12
  - TARJETA_CREDITO (revolving) → French system over 12 months with interest rate
```

#### LTV Calculation Formula (Mortgages only)

```
LTV = Outstanding Balance / Property Value
```

#### Infrastructure: Map-Based Strategy Pattern (No Switches)

The `ContractMapper` uses a `Map<String, Function<ContractEntity, Contract>>` to dispatch entity-to-domain conversion by contract type, avoiding switch/if-else chains:

```java
private static final Map<String, Function<ContractEntity, Contract>> MAPPERS = Map.of(
    "PRESTAMO",        ContractMapper::mapToLoanContract,
    "HIPOTECA",        ContractMapper::mapToMortgageContract,
    "TARJETA_CREDITO", ContractMapper::mapToCreditCardContract
);
```

To add a new contract type: (1) create domain subclass, (2) add mapping method, (3) register in the map.

#### New Domain Enums

| Enum | Values |
|------|--------|
| `ContractType` | PRESTAMO, HIPOTECA, TARJETA_CREDITO |
| `ContractStatus` | ACTIVO, PAGADO, EN_MORA |

#### New Infrastructure Components

| Component | Description |
|-----------|-------------|
| `ContractEntity` | MongoDB document mapping for the `contracts` collection (single-document union of all contract-type fields) |
| `ContractMapper` | Entity → Domain using Map-based strategy pattern (no switches) |
| `ContractRepository` | Spring Data MongoDB repository with `findByPartyId()` and `findByPartyIdAndStatus()` |
| `ContractRepositoryAdapter` | Implements `ContractPortRepository`, bridges domain with persistence |
| `ContractPortRepository` | Output port with `findByPartyId()` and `findActiveByPartyId()` |

#### Party Aggregate Integration (DDD)

Contracts are loaded as part of the Party aggregate in `PartyRepositoryAdapter.findById()`:

```java
// Load active contracts as part of the Party aggregate
List<Contract> activeContracts = contractPortRepository.findActiveByPartyId(partyId);
party.getPersonDetails().setActiveContracts(activeContracts);
```

This follows DDD principles: contracts hang off the Party aggregate root and are accessed through `Person.getActiveContracts()`.

#### Domain Calculation Logging

Added `@Slf4j` logging at DEBUG level and centralized log constants in `LogMessage.java` for traceability of financial calculations:

| Constant | Description |
|----------|-------------|
| `DOMAIN_TOTAL_DEBT_RESULT` | Total outstanding debt result |
| `DOMAIN_TOTAL_MONTHLY_PAYMENT_RESULT` | Total monthly debt payment result |
| `DOMAIN_DTI_RESULT` | DTI calculation (monthly payment, gross monthly income, DTI ratio) |
| `DOMAIN_DTI_NO_INCOME` | DTI skipped due to missing/zero income |
| `DOMAIN_LTV_RESULT` | LTV calculation (outstanding balance, property value, LTV ratio) |
| `DOMAIN_LTV_NO_DATA` | LTV skipped due to missing data |

---

### Party Management Implementation ✅

Complete implementation of the Party aggregate with all associated components:

#### Domain Layer

| Component | Description |
|-----------|-------------|
| `Party` | Aggregate representing a customer (individual or company). Contains party type and person details. |
| `Person` | Entity with identity data (name, NIF), and grouped profiles for demographics, financials, and contact info. |
| `ContactInfo` | Value object containing phone number, email, and physical address. |
| `FinancialProfile` | Value object with income, employment status, occupation, seniority, and credit history data. |
| `SocioDemographicProfile` | Value object with birth date, gender, marital status, education, home ownership, and dependants. |
| `Contract` | Entity representing active contracts with outstanding balance and monthly payment. |

#### New Enums

| Enum | Values |
|------|--------|
| `PartyType` | INDIVIDUAL, COMPANY |
| `Gender` | MALE, FEMALE, OTHER |
| `MaritalStatus` | SINGLE, MARRIED, DIVORCED, WIDOWED |
| `Education` | PRIMARY, SECONDARY, BACHELOR, MASTER, DOCTORATE |
| `EmploymentStatus` | EMPLOYED, SELF_EMPLOYED, UNEMPLOYED, RETIRED, STUDENT |
| `HomeOwnership` | OWNER, RENTER, FAMILY, OTHER |

#### Infrastructure Layer

| Component | Description |
|-----------|-------------|
| `PartyEntity` | MongoDB document mapping for parties collection with embedded documents. |
| `ContactInfoEntity` | Embedded document for contact information. |
| `DemographicsEntity` | Embedded document for demographic data. |
| `EconomicDataEntity` | Embedded document for economic/financial data. |
| `EmploymentEntity` | Embedded document for employment information. |
| `CreditHistoryEntity` | Embedded document for credit history tracking. |
| `PartyMapper` | Bidirectional mapper between PartyEntity and Party domain objects. |
| `PartyRepository` | Spring Data MongoDB repository for parties. |
| `PartyRepositoryAdapter` | Implements `PartyPortRepository`, bridging domain with persistence. |
| `PartyPortRepository` | Output port defining the contract for Party retrieval operations. |

#### Application Layer Updates

- `RequestApplicationService` now **orchestrates** Request and Party aggregates:
  - `listRequests()`: Resolves party names using `PartyPortRepository.findPartyName()` (projection-based, efficient)
  - `getRequestDetails()`: Resolves full party using `PartyPortRepository.findById()`
- `RequestDetailsDTO` includes complete party information: name, NIF, phone, email, address, employment status, and income.
- `RequestDetailsDTOMapper` maps party data from the domain to the DTO.

#### Projection Infrastructure

| Component | Description |
|-----------|-------------|
| `PartyNameProjection` | Spring Data interface projection for efficient name-only queries. Nested interface structure mirrors MongoDB document. |
| `PartyRepository.findPartyNameProjectionById()` | MongoDB query with `fields` projection retrieving only `demographics.first_name` and `demographics.last_name`. |
| `PartyMapper.toPartialDomain()` | Creates lightweight `Party` domain object with only `Person` containing name data.

#### Logging Updates

- Added Party-specific log messages in `LogMessage.java`:
  - `REPOSITORY_PARTY_FIND_BY_ID_START`
  - `REPOSITORY_PARTY_FIND_BY_ID_FOUND`
  - `REPOSITORY_PARTY_FIND_BY_ID_NOT_FOUND`
  - `REPOSITORY_PARTY_FIND_BY_ID_MAPPING`

---

### Scoring Endpoints Implementation ✅ (March 3, 2026)

**Objective:** Implement the scoring retrieval endpoint (`GET /api/requests/{id}/scoring`) to expose the AI model's risk assessment results for a given request.

#### New Components

| Layer | Component | Description |
|-------|-----------|-------------|
| Domain | `Scoring`, `RiskMetrics`, `ModelInputs`, `RiskFeature` | Domain entities/value objects for risk scoring data |
| Domain | `ScoringPortService`, `ScoringPortRepository` | Input and output ports |
| Application | `ScoringApplicationService`, `ScoringDTO`, `ScoringDTOMapper` | Use case, DTO, and mapper |
| Infrastructure | `ScoringEntity`, `ResultsEntity`, `InputFeaturesEntity`, `XaiEntity`, `TopFeatureEntity` | MongoDB document and embedded entities |
| Infrastructure | `ScoringMapper`, `ScoringRepository`, `ScoringRepositoryAdapter` | Mapper, repository, and adapter |
| Infrastructure | `ScoringControllerAdapter` | REST controller at `/api/requests/{id}/scoring` |

---

### Simulation Endpoints Implementation ✅ (March 3, 2026)

**Objective:** Implement read-only simulation endpoints (`GET /api/simulations` and `GET /api/simulations/{id}`) to expose what-if scenario analysis data, following the same hexagonal architecture patterns established in the project.

#### Architecture Overview

The simulation feature follows the same aggregate orchestration pattern as Request/Party:

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         APPLICATION LAYER                                │
│   ┌─────────────────────────────────────────────────────────────────┐   │
│   │            SimulationApplicationService                         │   │
│   │                                                                 │   │
│   │  listSimulations():                                             │   │
│   │    1. Get Simulations (via SimulationPortRepository)            │   │
│   │    2. Resolve party names (via PartyPortRepository)             │   │
│   │    3. Apply partyName filter in-memory (post-fetch)             │   │
│   │    4. Map to SimulationSummaryDTO                               │   │
│   │                                                                 │   │
│   │  getSimulationDetails():                                        │   │
│   │    1. Get Simulation by ID (via SimulationPortRepository)       │   │
│   │    2. Resolve base Scoring (via ScoringPortRepository)          │   │
│   │    3. Map to SimulationDetailsDTO with comparison data          │   │
│   └─────────────────────────────────────────────────────────────────┘   │
│           │                    │                    │                    │
│           ▼                    ▼                    ▼                    │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐        │
│  │ SimulationPort  │  │ PartyPort       │  │ ScoringPort     │        │
│  │ Repository      │  │ Repository      │  │ Repository      │        │
│  └────────┬────────┘  └────────┬────────┘  └────────┬────────┘        │
└───────────┼─────────────────────┼───────────────────┼──────────────────┘
            │                     │                    │
┌───────────┼─────────────────────┼───────────────────┼──────────────────┐
│           ▼                     ▼                    ▼                  │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐        │
│  │ Simulation      │  │ Party           │  │ Scoring         │        │
│  │ Repository      │  │ Repository      │  │ Repository      │        │
│  │ Adapter         │  │ Adapter         │  │ Adapter         │        │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘        │
│                      INFRASTRUCTURE LAYER                              │
└─────────────────────────────────────────────────────────────────────────┘
```

#### New Components

| Layer | Component | Description |
|-------|-----------|-------------|
| Domain | `Simulation` | Aggregate with form changes, simulated results, delta, and transient party |
| Domain | `SimulationPortService` | Input port: `listSimulations()`, `getSimulationDetails()` |
| Domain | `SimulationPortRepository` | Output port: `findById()`, `findWithFilters()` |
| Application | `SimulationApplicationService` | Orchestrates Simulation + Party + Scoring aggregates |
| Application | `SimulationSummaryDTO` | Summary for list view (scenario name, party, request, date) |
| Application | `SimulationDetailsDTO` | Detail with base/simulated comparison and deltas |
| Application | `SimulationDTOMapper` | Domain → DTO conversion (summary and detail) |
| Infrastructure | `SimulationEntity` | `@Document(collection = "simulations")` with embedded entities |
| Infrastructure | `FormChangesEntity` | Embedded: modified inputs (income, term, amount, rate, etc.) |
| Infrastructure | `SimulatedResultsEntity` | Embedded: recalculated risk metrics (PD, LGD, EAD, ECL, risk grade, decision) |
| Infrastructure | `DeltaEntity` | Embedded: differences (pd_change, el_change, risk_grade_change) |
| Infrastructure | `SimulationMapper` | Entity → Domain (FormChanges→HashMap, SimulatedResults→RiskMetrics, Delta→fields) |
| Infrastructure | `SimulationRepository` | Spring Data MongoDB with dynamic SpEL filtering (same pattern as `RequestRepository`) |
| Infrastructure | `SimulationRepositoryAdapter` | String→ObjectId conversion, delegates to single dynamic query |
| Infrastructure | `SimulationControllerAdapter` | REST controller at `/api/simulations` |

#### Modified Components

| Component | Change Description |
|-----------|-------------------|
| `ScoringPortRepository` | Added `findById(String scoringId)` method for simulation detail comparison |
| `ScoringRepositoryAdapter` | Implemented `findById()` with EntityNotFoundException handling |
| `LogMessage.java` | Added 20+ simulation-specific log constants (controller, service, repository layers) |

#### Dynamic Filtering (SpEL)

The `SimulationRepository` uses the same null-skipping pattern as `RequestRepository`, allowing optional filters:

```java
@Query("{ $and: [ " +
       "{ $or: [ { $expr: { $eq: [:#{#requestId}, null] } }, { 'request_id': :#{#requestId} } ] }, " +
       "{ $or: [ { $expr: { $eq: [:#{#partyId}, null] } }, { 'party_id': :#{#partyId} } ] } " +
       "] }")
List<SimulationEntity> findWithFilters(@Param("requestId") ObjectId requestId,
                                       @Param("partyId") ObjectId partyId);
```

**Note:** Party name filtering is not possible at the database level because the `simulations` collection only stores `party_id` (ObjectId reference). Party name filtering is handled at the application layer via post-fetch in-memory filtering with case-insensitive `contains` matching.

#### Known TODOs in Code

| Location | TODO | Description |
|----------|------|-------------|
| `SimulationApplicationService.listSimulations()` | `TODO: To change because it is not working` | Party name post-fetch filtering needs to be reviewed/tested with actual data |
| `SimulationApplicationService.getSimulationDetails()` | `TODO: Resolve base scoring for comparison` | Base scoring should always be persisted; null check may be unnecessary |

---

## Author

**Lucía Fernández Mancebo**

*Date: February 28, 2026*

*Last Updated: March 3, 2026*