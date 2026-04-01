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

The service follows a hexagonal architecture (also known as clean architecture) with clear separation of concerns:

```
src/main/java/com/bluemarket/
│
├── domain/                          <-- Core Domain Layer
│   ├── model/                       <-- Entity classes (User, Profile)
│   ├── exception/                   <-- Domain-specific exceptions
│   └── gateway/                     <-- Data access interfaces (Repository contracts)
│
├── application/                     <-- Use Case Layer
│   ├── usecase/                     <-- Business logic & workflows
│   │   ├── RegisterUserUseCase
│   │   └── UpdateUserProfileUseCase
│   ├── request/                     <-- Input DTOs (Commands/Queries)
│   └── response/                    <-- Output DTOs (Response objects)
│
├── infrastructure/                  <-- Outer Layer (Implementation)
│   ├── persistence/                 <-- Database implementations
│   │   ├── entity/                  <-- Hibernate @Entity classes
│   │   ├── mapper/                  <-- Domain <-> Entity mappers
│   │   └── PostgreSQLUserRepository <-- Implements gateway interface
│   ├── security/                    <-- Authentication & authorization
│   │   └── JwtTokenGenerator        <-- JWT security logic
│   └── config/                      <-- Quarkus CDI Beans/Producers
│
└── presentation/                    <-- Outer Layer (Delivery)
    ├── rest/                        <-- REST API endpoints (@Path, @GET, @POST)
    └── mapper/                      <-- DTO <-> Domain mappers
```

### Architectural Layers Explained

**Domain Layer (Core)**

- Contains pure business logic independent of frameworks
- Defines entities (User, Profile) and business rules
- Declares gateway interfaces for data persistence
- Houses domain-specific exceptions

**Application Layer (Use Cases)**

- Orchestrates business workflows (RegisterUser, UpdateProfile)
- Defines input/output contract via request/response DTOs
- Uses dependencies from domain and infrastructure layers
- Remains framework-agnostic

**Infrastructure Layer**

- Implements domain gateway interfaces with concrete technologies (JPA, Hibernate)
- Handles database persistence and security concerns
- Manages external integrations
- Configures CDI beans and framework-specific setup

**Presentation Layer**

- Exposes REST API endpoints
- Handles HTTP request/response mapping
- Converts between DTOs and domain objects
- Framework-touching layer (@Quarkus, @JAX-RS)

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
