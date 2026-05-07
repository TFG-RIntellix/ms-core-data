# ms-core-data

> Core microservice for credit risk analysis and financial simulations in the RIntellix platform

![Java](https://img.shields.io/badge/Java-17-blue?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-6DB33F?logo=spring)
![MongoDB](https://img.shields.io/badge/MongoDB-Latest-13AA52?logo=mongodb)
![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36)

## Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Project Architecture](#project-architecture)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [Troubleshooting](#troubleshooting)

---

## Overview

**ms-core-data** is a production-grade microservice that forms part of the **RIntellix Core** system for processing financial requests, credit risk scoring, and what-if simulations. Built with **Spring Boot 4.0.3**, **Java 17**, and **MongoDB**, it implements hexagonal architecture (Ports and Adapters) to maintain clean separation between domain logic and infrastructure concerns.

### Purpose

The microservice provides a stable REST API for:

- **Financial Requests**: Retrieve and manage customer credit requests with comprehensive details
- **Risk Scorings**: Generate and retrieve credit risk assessments with financial metrics
- **What-If Simulations**: Create and manage custom simulations for scenario analysis

All operations are built on a domain-driven design foundation ensuring business logic remains independent from transport and persistence technologies.

### Current Service Scope

| Component | Capability |
|-----------|-----------|
| **Requests** | Read-only operations (view and list) |
| **Scorings** | Read-only operations (retrieve associated scoring) |
| **Simulations** | Full lifecycle (create, read, update, archive, delete) |

---

## Key Features

### Request Management

- **List all requests** with optional filtering by party name or request status
- **Retrieve detailed request information** including associated party and financial details
- **Application-layer orchestration** with automatic party data resolution

### Risk Scoring

- **Retrieve active scoring** associated with any request
- **Comprehensive risk metrics** including:
  - Socio-demographic features (age, education, marital status, etc.)
  - Employment and financial profile data
  - Loan-specific features (amount, term, purpose)
  - Risk calculations (DTI, LTV ratios)
- **XAI top features** for model interpretability

### Simulation Engine

- **Full CRUD lifecycle** for what-if simulations
- **Advanced filtering** by request ID, party ID, or party name
- **Stateless persistence** - frontend calculates, backend stores
- **Base scoring comparison** showing simulation vs. original scoring
- **Archive-before-delete** enforcement for data safety
- **Soft-delete via archival** (data retention pattern)

### Async Scoring Generation with Strategy Pattern

- **Type-Specific Payloads**: Implements Strategy Pattern to handle different request types with optimized message payloads
  - **Loans & Mortgages** (`LoanScoringTransportStrategy`): Full 21-field feature set including LTV, DTI, term, purpose
  - **Credit Cards** (`CreditCardScoringTransportStrategy`): Focused 10-field set (demographics, income, credit limit, revolving status)
- **Factory-Based Strategy Selection**: `ScoringTransportStrategyFactory` automatically selects the correct strategy based on request type
- **Kafka Message Building**: Each strategy constructs typed Kafka messages with proper headers (X-Request-ID, X-Timestamp)
- **Efficient Transport**: Credit card payloads 52% smaller than loan payloads, reducing Kafka bandwidth overhead
- **DTO Mapping**: Specialized mappers (`ScoringGenerationTransportDTOMapper`, `CreditCardScoringGenerationTransportDTOMapper`) transform domain objects to transport DTOs

### Cross-Cutting Features

- **Centralized exception handling** with structured error responses
- **Consistent structured logging** with configurable log levels
- **MongoDB integration** with contract hierarchy support
- **Async scoring generation** with Strategy Pattern for type-specific Kafka transport
  - **Loan/Mortgage Strategy**: Full 21-field payload for complex loan products
  - **Credit Card Strategy**: Optimized 10-field payload for credit card products
  - **Type-Based Message Routing**: Factory-driven strategy selection based on request type

---

## Project Architecture

### Hexagonal Architecture Overview

The system is organized into three distinct layers:

```
┌─────────────────────────────────────────┐
│   Infrastructure Layer                  │
│  (Controllers • Adapters • Repositories)│
├─────────────────────────────────────────┤
│   Application Layer                     │
│  (Use Cases • DTOs • Mappers)           │
├─────────────────────────────────────────┤
│   Domain Layer                          │
│  (Entities • Business Rules • Ports)    │
└─────────────────────────────────────────┘
```

**Layer Responsibilities:**

| Layer | Responsibilities |
|-------|------------------|
| **Domain** | Entities, value objects, business rules, domain ports, domain exceptions—pure Java with zero framework dependencies |
| **Application** | Use-case orchestration, DTO mapping, cross-aggregate coordination, application services |
| **Infrastructure** | REST controllers, Spring Data repositories, MongoDB entities, adapter implementations |

### Component Interaction

1. **REST Controller** receives HTTP request
2. **Application Service** orchestrates business logic using domain entities and repositories
3. **Domain Entities** enforce business rules (e.g., contract calculations for DTI/LTV)
4. **Repository Adapters** persist/retrieve data from MongoDB
5. **Exception Handler** catches and formats errors

### Key Design Decisions

- **Requests and scorings are read-only** to maintain data integrity in scoring workflows
- **Simulations support full CRUD** to enable user-driven what-if scenarios
- **Stateless simulation creation**: frontend calculates simulation payloads, backend persists
- **Aggregate orchestration in application layer** preserves Single Responsibility Principle
- **Archive-before-delete policy** ensures simulation data safety
- **Contract hierarchy** supports diverse financial instruments (loans, mortgages, credit cards)
- **Strategy Pattern for Scoring Transport**: Type-specific message payloads for efficient Kafka communication
  - Decouples request type logic from message building
  - Enables easy addition of new product types without modifying existing strategies
  - Reduces payload size for credit cards by only including relevant fields

---

## Strategy Pattern Implementation for Scoring Generation

### Overview

Scoring generation uses the **Strategy Pattern** to handle type-specific message transport to the scoring engine via Kafka. Each financial product type requires different feature sets, and the strategy pattern ensures efficient, type-appropriate payloads without conditional logic.

### Architecture

```
ScoringGenerationRequest (21 fields - all types)
        ↓
ScoringTransportStrategyFactory.createStrategy()
        ↓
        ├── PRESTAMO/HIPOTECA → LoanScoringTransportStrategy
        └── TARJETA_CREDITO → CreditCardScoringTransportStrategy
        ↓
Strategy.buildScoreGenerationMessage()
        ├── Loan: ScoringGenerationDTO (21 fields)
        └── CreditCard: CreditCardScoringGenerationDTO (10 fields)
        ↓
Kafka Message with Headers (X-Request-ID, X-Timestamp)
```

### Strategy Implementations

#### Loan Scoring Transport Strategy

**Used for:** `PRESTAMO` (Loans) and `HIPOTECA` (Mortgages)

**DTO:** `ScoringGenerationDTO` (21 fields)

**Fields:**
- Core: `requestId`, `partyId`
- Demographics: `age`, `gender`, `maritalStatus`, `education`, `dependents`, `homeOwnership`, `hasMortgage`
- Employment: `employmentStatus`, `occupationSector`
- Financial: `annualIncome`
- Loan Details: `requestType`, `purpose`, `loanAmount`, `termMonths`, `interestRate`, `loanType`
- Credit History: `ltv`, `dti`, `previousLoansCount`, `previousDefaultsCount`

**Mapper:** `ScoringGenerationTransportDTOMapper`

**Message Headers:**
- `KafkaHeaders.TOPIC`: Target Kafka topic
- `KafkaHeaders.KEY`: Request ID (for partitioning)
- `X-Request-ID`: Request identifier
- `X-Timestamp`: Current milliseconds

#### Credit Card Scoring Transport Strategy

**Used for:** `TARJETA_CREDITO` (Credit Cards)

**DTO:** `CreditCardScoringGenerationDTO` (10 fields)

**Fields:**
- Core: `requestId`, `partyId`
- Demographics: `age`, `gender`, `maritalStatus`, `employmentStatus`
- Financial: `annualIncome`
- Card-Specific: `requestType`, `creditLimit`, `isRevolving`

**Mapper:** `CreditCardScoringGenerationTransportDTOMapper`

**Message Headers:** Same as Loan strategy

**Size Reduction:** ~52% smaller than loan payload (10 vs 21 fields)

### Factory Pattern

`ScoringTransportStrategyFactory` centralizes strategy creation:

```java
ScoringTransportStrategy strategy = ScoringTransportStrategyFactory
    .createStrategy(scoringGenerationRequest);
Message<?> message = strategy.buildScoreGenerationMessage(request, kafkaTopic);
```

**Selection Logic:**
- `PRESTAMO` → `LoanScoringTransportStrategy`
- `HIPOTECA` → `LoanScoringTransportStrategy` (same as PRESTAMO)
- `TARJETA_CREDITO` → `CreditCardScoringTransportStrategy`

### Benefits

1. **Type Safety**: Each strategy works with its specific DTO
2. **Efficiency**: Credit cards don't send unnecessary loan fields
3. **Maintainability**: Adding new product types only requires new strategy + DTO
4. **Testability**: Each strategy can be unit tested independently
5. **Clean Code**: No switch statements or conditional logic in message building

---

## Installation

### Prerequisites

- **Java 17** or later ([OpenJDK](https://openjdk.java.net/) or Oracle JDK)
- **Maven 3.8.1** or later (included Maven Wrapper available)
- **MongoDB 4.4** or later (local or remote instance)
- **Kafka 2.8+** (optional, for async scoring; currently uses mock adapter)

### Dependencies

Core dependencies managed by Maven:

```xml
<!-- Framework -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>

<!-- Database -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>

<!-- Async Processing (Kafka) -->
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>

<!-- Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Logging -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <scope>provided</scope>
</dependency>
```

Full dependency list is in [pom.xml](pom.xml).

### Configuration

#### Step 1: Environment Variables

Set up your local environment:

```bash
# MongoDB
export MONGODB_URI=mongodb://localhost:27017/RIntellix
export MONGODB_DATABASE=RIntellix

# Kafka (optional)
export KAFKA_BOOTSTRAP_SERVERS=localhost:9092
```

#### Step 2: Application Properties

Review and adjust [`src/main/resources/application.properties`](src/main/resources/application.properties):

```properties
# Server
spring.application.name=ms-core-data
server.port=8081

# MongoDB
spring.mongodb.uri=mongodb://localhost:27017/RIntellix
spring.data.mongodb.database=RIntellix

# Kafka
spring.kafka.bootstrap-servers=localhost:9092
scoring.kafka.topic.generation=GenerateScoring

# Logging
logging.level.es.NTTEnterprise.RIntellix.ms_core_data=DEBUG
```

#### Step 3: Start MongoDB

Using Docker Compose (Linux/macOS):

```bash
# Start MongoDB and Kafka
docker-compose up -d mongodb

# Verify MongoDB is running
docker-compose ps
```

Or manually:

```bash
# macOS with Homebrew
brew services start mongodb-community

# Linux (systemd)
sudo systemctl start mongod

# Verify
mongo --version
```

#### Step 4: Build Project

```bash
# Clone repository and navigate to root
cd ms-core-data

# Build with Maven Wrapper (Linux/macOS)
./mvnw clean package

# Build with Maven Wrapper (Windows)
.\mvnw.cmd clean package

# Or with system Maven
mvn clean package
```

**Expected output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: XX.XXX s
```

### Starting the Application

```bash
# Using Maven Wrapper (Linux/macOS)
./mvnw spring-boot:run

# Using Maven Wrapper (Windows)
.\mvnw.cmd spring-boot:run

# Using system Maven
mvn spring-boot:run

# Or after building
java -jar target/ms-core-data-0.0.1-SNAPSHOT.jar
```

Service runs on: **http://localhost:8081**

Verify it's running:
```bash
curl http://localhost:8081/api/requests
```

---

## Usage

### REST API Overview

#### Request Endpoints

**List all requests:**
```bash
GET http://localhost:8081/api/requests

# Optional filters
GET http://localhost:8081/api/requests?partyName=John%20Doe
GET http://localhost:8081/api/requests?requestStatus=APPROVED
```

**Response:**
```json
[
  {
    "id": "req_12345",
    "partyId": "party_001",
    "partyName": "John Doe",
    "requestType": "PERSONAL_LOAN",
    "requestedAmount": 50000.00,
    "targetedInterestRate": 5.5,
    "status": "APPROVED"
  }
]
```

**Get request details:**
```bash
GET http://localhost:8081/api/requests/{requestId}
```

**Response:**
```json
{
  "id": "req_12345",
  "partyId": "party_001",
  "partyName": "John Doe",
  "requestType": "PERSONAL_LOAN",
  "requestedAmount": 50000.00,
  "termMonths": 60,
  "targetedInterestRate": 5.5,
  "purpose": "HOME_IMPROVEMENT",
  "status": "APPROVED",
  "party": {
    "id": "party_001",
    "name": "John Doe",
    "email": "john@example.com",
    "person": {
      "age": 35,
      "maritalStatus": "MARRIED",
      "education": "UNIVERSITY",
      "dependents": 2
    }
  }
}
```

#### Scoring Endpoints

**Get active scoring for request:**
```bash
GET http://localhost:8081/api/requests/{requestId}/scoring
```

**Response:**
```json
{
  "id": "scoring_001",
  "requestId": "req_12345",
  "riskScore": 750,
  "riskLevel": "LOW",
  "features": {
    "demographics": {
      "age": 35,
      "gender": "MALE",
      "maritalStatus": "MARRIED",
      "education": "UNIVERSITY"
    },
    "financial": {
      "annualIncome": 100000.00,
      "dti": 0.35,
      "previousLoansCount": 2,
      "previousDefaultsCount": 0
    },
    "loan": {
      "amount": 50000.00,
      "term": 60,
      "interestRate": 5.5,
      "ltv": 0.65
    }
  },
  "topFeatures": ["annualIncome", "dti", "previousDefaults"]
}
```

#### Simulation Endpoints

**Create simulation:**
```bash
POST http://localhost:8081/api/simulations
Content-Type: application/json

{
  "requestId": "req_12345",
  "changeType": "WHAT_IF",
  "templateData": {
    "loanAmount": 60000.00,
    "interestRate": 6.0,
    "term": 72
  }
}
```

**Response:**
```json
{
  "id": "sim_001",
  "requestId": "req_12345",
  "createdAt": "2026-03-22T10:30:00Z",
  "archived": false,
  "simulationData": {
    "loanAmount": 60000.00,
    "interestRate": 6.0,
    "term": 72
  }
}
```

**List simulations:**
```bash
GET http://localhost:8081/api/simulations
GET http://localhost:8081/api/simulations?requestId=req_12345&archived=false
```

**Get simulation details:**
```bash
GET http://localhost:8081/api/simulations/{simulationId}
```

**Update simulation:**
```bash
PUT http://localhost:8081/api/simulations/{simulationId}
Content-Type: application/json

{
  "templateData": {
    "loanAmount": 65000.00,
    "interestRate": 6.2,
    "term": 84
  }
}
```

**Archive simulation:**
```bash
PATCH http://localhost:8081/api/simulations/{simulationId}
Content-Type: application/json

{
  "archived": true
}
```

**Delete archived simulation:**
```bash
DELETE http://localhost:8081/api/simulations/{simulationId}

# Returns 400 Bad Request if simulation is not archived
```

### Error Handling

All errors return structured JSON responses:

```json
{
  "timestamp": "2026-03-22T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Simulation with ID 'sim_999' not found",
  "path": "/api/simulations/sim_999"
}
```

| Scenario | HTTP Status |
|----------|------------|
| Entity not found | `404 Not Found` |
| Invalid request data | `400 Bad Request` |
| Cannot delete unarchived simulation | `400 Bad Request` |
| Server error | `500 Internal Server Error` |

---

## Contributing

### Development Setup

1. **Fork and clone** the repository
2. **Follow installation steps** above
3. **Review development guidelines**: [Copilot Instructions](.github/copilot-instructions.md)
4. **Use provided agents** for feature generation and component development

### Project Structure

```
src/
├── main/java/es/NTTEnterprise/RIntellix/ms_core_data/
│   ├── MsCoreDataApplication.java
│   ├── application/              # Use cases & orchestration
│   │   ├── constraints/          # Custom validation annotations
│   │   ├── dtos/                 # Data Transfer Objects
│   │   │   ├── input/
│   │   │   └── output/
│   │   ├── mappers/              # DTO ↔ Entity mappers
│   │   ├── usecases/             # Application services
│   │   └── validators/           # Custom validators
│   ├── domain/                   # Pure business logic
│   │   ├── entities/             # Domain entities
│   │   ├── enums/                # Domain enumerations
│   │   ├── exceptions/           # Domain exceptions
│   │   └── ports/                # Port interfaces
│   │       ├── input/            # Inbound ports
│   │       └── output/           # Outbound ports
│   ├── infraestructure/          # Technical implementation
│   │   ├── adapters/             # Port implementations
│   │   │   ├── input/            # REST controllers
│   │   │   └── output/           # External service adapters
│   │   ├── config/               # Spring configuration
│   │   ├── entities/             # MongoDB entities
│   │   ├── mappers/              # Infrastructure mappers
│   │   ├── projections/          # MongoDB projections
│   │   └── repository/           # Spring Data repositories
│   └── utils/                    # Utilities
│       └── LogMessage.java       # Centralized log templates
└── main/resources/
    ├── application.properties     # Configuration
    └── logback-spring.xml         # Logging setup
```

### Coding Standards

- **Language**: Java 17 with Spring Boot 4.0.3
- **Architecture**: Hexagonal (Ports & Adapters)
- **Dependency Injection**: Constructor injection (no `@Autowired`)
- **Logging**: Use `LogMessage` constants + SLF4J
- **Documentation**: Comprehensive Javadoc for public APIs
- **Tests**: Given/When/Then structure with JUnit 5
- **Version Control**: [Commit Message Convention](.github/prompts/COMMIT_MESSAGE_GENERATOR.md)

See [.github/copilot-instructions.md](.github/copilot-instructions.md) for complete development guidelines including layer separation, constructor injection patterns, entity design, and testing structure.

### Testing

```bash
# Run unit tests
./mvnw test

# Run all tests (unit + integration)
./mvnw verify

# Run specific test class
./mvnw test -Dtest=RequestApplicationServiceTest
```

### Build & Verification

```bash
# Clean build with all tests
./mvnw clean verify

# Build without tests
./mvnw clean package -DskipTests

# Check code quality
./mvnw clean test
```

---

## Troubleshooting

### MongoDB Connection Issues

**Error:** `com.mongodb.MongoSocketOpenException: Exception opening socket`

**Solutions:**
- Verify MongoDB is running: `mongo --version`
- Check connection URI: `spring.mongodb.uri=mongodb://localhost:27017/RIntellix`
- Verify database exists: `connect to MongoDB and run db.version()`
- If using Docker: `docker-compose ps` (check container status)

### Port Already in Use

**Error:** `Address already in use: bind`

**Solutions:**
- Change port in `application.properties`: `server.port=8082`
- Or kill existing process: 
  ```bash
  # macOS/Linux
  lsof -ti:8081 | xargs kill -9
  
  # Windows
  netstat -ano | findstr :8081
  taskkill /PID <PID> /F
  ```

### Build Failures

**Error:** `Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin`

**Solutions:**
- Verify Java version: `java -version` (must be 17+)
- Clear Maven cache: `./mvnw clean`
- Rebuild: `./mvnw clean package`

### Kafka-Related Issues (Optional Feature)

Currently, asynchronous scoring uses a mock adapter. Kafka integration is pending. See [KAFKA_INTEGRATION_GUIDE.md](documentation/KAFKA_INTEGRATION_GUIDE.md) for setup when ready.

---

## Documentation

- **[Full API Reference](README.md#usage)** - Detailed endpoint documentation
- **[Architecture Guide](documentation/CONSISTENCY_ALIGNMENT_REPORT.md)** - Detailed architecture decisions
- **[Async Scoring Design](documentation/TECHNICAL_GUIDE_ASYNC_SCORING.md)** - Scoring generation flow
- **[Kafka Integration](documentation/KAFKA_INTEGRATION_GUIDE.md)** - When Kafka is ready
- **[Developer Guidelines](.github/copilot-instructions.md)** - Code standards and conventions

---

## Summary

**ms-core-data** provides a production-ready microservice for credit risk analysis with:
- Clean hexagonal architecture maintaining separation of concerns
- Comprehensive REST API for requests, scorings, and simulations
- MongoDB persistence with contract hierarchy support
- Async scoring generation ready for Kafka integration
- Structured error handling and logging

**Get started:** Install MongoDB, build with Maven, and run `./mvnw spring-boot:run`. The API is ready at `http://localhost:8081`.

---

**Last Updated:** March 22, 2026  
**Maintained By:** Development Team  
**Framework Version:** Spring Boot 4.0.3 + Java 17
