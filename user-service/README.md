# User Service

A microservice responsible for user management, authentication, and profile operations in the Blue Market platform. Built with **Quarkus**, the Supersonic Subatomic Java Framework, following a **Clean Hexagonal Architecture** pattern.

## Overview

The User Service handles:

- User registration and account management
- User authentication and JWT token generation
- User profile management
- Role-based access control
- Secure password management

For more information about Quarkus, visit: <https://quarkus.io/>

## Project Structure

The service follows a **layered clean architecture** with a straightforward **API → Service → Repository** flow:

```
src/main/java/com/bluemarket/
│
├── domain/                          <-- Domain Layer
│   ├── model/                       <-- Domain models (User) — JPA entities using Panache
│   ├── repository/                  <-- Repository interfaces (data access contracts)
│   │   └── IUserRepository          <-- Repository interface defining contract
│   └── exception/                   <-- Domain-specific exceptions
│
├── application/                     <-- Application Layer
│   ├── service/                     <-- Business logic & orchestration
│   │   └── UserService              <-- Service methods (business rules, use cases)
│   ├── response/                    <-- Output DTOs (Response objects)
│   └── request/                     <-- Input DTOs (if needed for commands)
│
├── infrastructure/                  <-- Infrastructure Layer
│   ├── persistence/
│   │   └── repository/              <-- Repository implementations
│   │       └── UserRepository       <-- Implements gateway interface
│   ├── security/                    <-- Authentication & authorization (future)
│   └── config/                      <-- Quarkus CDI Beans/Producers
│
└── presentation/                    <-- Presentation Layer
    └── rest/                        <-- REST API endpoints
        └── UserResources            <-- REST controller (@Path, @GET, @POST)
```

### Architectural Flow

**Request Flow:** `REST Controller` → `Service` → `Repository` → `Domain Model`

```
UserResources (API)
    ↓ [calls]
UserService (Business Logic)
    ↓ [uses]
UserRepository (Data Access)
    ↓ [operates on]
User (Domain Model/Entity)
```

### Layers Overview

**Domain Layer (Core)**

- **Models:** JPA entities (e.g., `User`) using Panache Active Record pattern
- **Gateways:** Repository interfaces defining data access contracts
- **Exceptions:** Domain-specific exceptions

**Application Layer (Business Logic)**

- **Services:** Orchestrates business workflows and use cases
  - Implements gateway interfaces through dependency injection
  - Contains business rules and validation logic
  - Returns DTOs for API responses
- **Response/Request DTOs:** Input/output contracts for API

**Infrastructure Layer (Technical Implementation)**

- **Repositories:** Concrete implementations of gateway interfaces
  - Uses Panache or native queries for database operations
  - No explicit mappers needed (domain models serve as entities)
- **Security:** JWT, authentication, authorization handling
- **Configuration:** CDI beans and framework setup

**Presentation Layer (HTTP Interface)**

- **REST Controllers:** Exposes API endpoints
- **Dependency Injection:** Injects services to handle business logic
- **Request/Response Mapping:** Converts between HTTP and DTOs

## Prerequisites

- Java 21+
- Gradle 7.0+ (included via gradlew)
- PostgreSQL 15+ (for persistence)
- GraalVM (optional, for native compilation)

## Running the Application

### Development Mode

Start the application in development mode with live coding enabled:

```shell script
./gradlew quarkusDev
```

The application will be available at `http://localhost:8080`

> **Dev UI:** Quarkus Dev UI is accessible at <http://localhost:8080/q/dev/> in development mode only. Use it to browse endpoints, execute queries, and test API calls.

### Production Build

Package the application as a JAR file:

```shell script
./gradlew build
```

This generates a `quarkus-run.jar` file in the `build/quarkus-app/` directory. Dependencies are automatically included in `build/quarkus-app/lib/`.

Run the packaged application:

```shell script
java -jar build/quarkus-app/quarkus-run.jar
```

### Über-JAR Build

To create a single executable JAR with all dependencies bundled:

```shell script
./gradlew build -Dquarkus.package.jar.type=uber-jar
```

Run the über-JAR:

```shell script
java -jar build/*-runner.jar
```

### Native Executable Build

Build a native executable for improved startup time and reduced memory usage:

```shell script
./gradlew build -Dquarkus.native.enabled=true
```

If GraalVM is not installed, build the native executable in a container:

```shell script
./gradlew build -Dquarkus.native.enabled=true -Dquarkus.native.container-build=true
```

Execute the native binary:

```shell script
./build/user-service-1.0.0-SNAPSHOT-runner
```

For detailed information about native builds, see the [Quarkus Gradle Tooling Guide](https://quarkus.io/guides/gradle-tooling).
