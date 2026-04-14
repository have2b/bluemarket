# Blue Market — Project Planning Document

> **Version:** 2.0 | **Last Updated:** 2026-04-11

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [System Architecture](#2-system-architecture)
3. [Microservices Breakdown](#3-microservices-breakdown)
4. [Development Phases](#4-development-phases)
5. [Testing Strategy](#5-testing-strategy)
6. [Deployment Strategy](#6-deployment-strategy)
7. [Timeline & Milestones](#7-timeline--milestones)
8. [Risk Management](#8-risk-management)
9. [Team Structure](#9-team-structure)

- [Appendix A: Service Contracts](#appendix-a-service-contracts)
- [Appendix B: Definition of Done](#appendix-b-definition-of-done)

---

## 1. Project Overview

### 1.1 Project Name

**Blue Market** — E-commerce + Charity Platform

### 1.2 Project Vision

A modern e-commerce platform where sellers list products, vendors manage storefronts, and customers shop while supporting charitable causes. Seamless donation mechanics (round-ups, vendor pledges) and a dedicated charity marketplace make giving a natural part of every transaction.

### 1.3 Target Scale

| Metric         | Value                             |
| -------------- | --------------------------------- |
| Active Users   | 10,000 – 100,000                  |
| Orders per Day | 1,000 – 10,000                    |
| Growth Path    | Horizontally scalable per service |

### 1.4 Tech Stack

The platform uses a **polyglot microservices architecture** — each service's technology is chosen to match its specific domain and performance requirements.

**Core Services**

| Service                   | Framework              | Language        | Rationale                                                                                       |
| ------------------------- | ---------------------- | --------------- | ----------------------------------------------------------------------------------------------- |
| **User & Identity**       | Quarkus                | Java 21         | Reactive stack + virtual threads for high-concurrency auth/token validation with minimal memory |
| **Catalog & Inventory**   | ASP.NET Core           | .NET 10         | Rich DDD modeling; EF Core for complex relational product/inventory/pledge rules                |
| **Order & Checkout**      | Fiber                  | Go              | Raw throughput for burst traffic (flash sales); scales horizontally in milliseconds             |
| **Donation & Payment**    | Axum                   | Rust            | Compile-time memory safety + zero data races — mandatory for a financial ledger                 |
| **Feature Flags Service** | LaunchDarkly / Unleash | SaaS or Go      | Centralized feature toggles, A/B test assignment, canary rollouts per service                   |
| **Real-time Service**     | Fiber / Node.js        | Go / JavaScript | WebSocket server, Kafka → in-memory broadcast, fallback SSE for 5K+ concurrent users            |
| **ML Ranking Service**    | FastAPI                | Python          | Recommendation engine, search ranking (LambdaMART), A/B test evaluation                         |
| **Admin Dashboard API**   | GraphQL                | Node.js / Go    | Unified API for seller vetting, charity management, dispute resolution, financial reporting     |

**Shared Infrastructure**

| Component               | Technology                                |
| ----------------------- | ----------------------------------------- |
| Databases               | PostgreSQL (per service), Redis (caching) |
| Search                  | Elasticsearch (Catalog Service)           |
| Message Broker          | Apache Kafka (primary) / RabbitMQ (alt)   |
| Inter-Service Sync      | gRPC + Protobuf                           |
| API Gateway             | YARP / Envoy                              |
| Container Orchestration | Kubernetes (Multi-cloud)                  |
| Service Mesh            | Istio / Linkerd                           |
| CI/CD                   | GitHub Actions + ArgoCD                   |
| Monitoring              | Prometheus + Grafana                      |
| Logging                 | ELK Stack                                 |
| Tracing                 | Jaeger / OpenTelemetry                    |

### 1.5 Non-Functional Requirements

| Requirement              | Target                                                        |
| ------------------------ | ------------------------------------------------------------- |
| Availability             | 99.9% uptime (production)                                     |
| API Response Time        | < 200ms (P95)                                                 |
| Auth / Token Validation  | < 50ms                                                        |
| Checkout Flow            | < 30s end-to-end                                              |
| Scalability              | Horizontal auto-scale per service                             |
| Security                 | OAuth 2.0 + PKCE, JWT, RBAC, TLS 1.3                          |
| Compliance               | GDPR, CCPA, PCI-DSS                                           |
| **Real-time Updates**    | **WebSocket push < 500ms latency**                            |
| **Rate Limiting**        | **Per-endpoint + per-user quotas with graceful degradation**  |
| **Audit Trail**          | **7-year immutable ledger, 100% log integrity**               |
| **Feature Flag Rollout** | **Canary unsafe features to 5% / 25% / 100% user segments**   |
| **Search Ranking**       | **P95 < 200ms; personalized + freshness + seller tier boost** |

---

## 2. System Architecture

### 2.1 High-Level Architecture

```
                ┌───────────────────────────────────────────────┐
                │             API Gateway / BFF                 │
                │   (Auth, Routing, Rate Limiting, mTLS)        │
                └───────────────────┬───────────────────────────┘
                                    │  REST / gRPC
     ┌──────────────────────────────┼────────────────────────────────┐
     │                              │                                │
     ▼                              ▼                                ▼
┌──────────────────┐  ┌─────────────────────┐  ┌──────────────────────┐  ┌────────────────────┐
│ User & Identity  │  │ Catalog & Inventory │  │  Order & Checkout    │  │ Donation & Payment │
│ Quarkus/Java 21  │  │ ASP.NET Core/.NET10 │  │   Golang Fiber       │  │   Rust Axum        │
│   PostgreSQL     │  │ PostgreSQL + ES      │  │   PostgreSQL         │  │   PostgreSQL       │
└────────┬─────────┘  └──────────┬──────────┘  └──────────┬───────────┘  └─────────┬──────────┘
         │                       │                         │                        │
         └───────────────────────┴────── Apache Kafka ─────┴────────────────────────┘
                                           (Domain Events)
     ┌───────────────┐    ┌──────────────────┐    ┌───────────────────┐
     │ Shipping Svc  │    │ Notification Svc │    │  Analytics Svc    │
     │  PostgreSQL   │    │ PostgreSQL+Redis  │    │ PostgreSQL+CH     │
     └───────────────┘    └──────────────────┘    └───────────────────┘
```

### 2.2 Architecture Principles

Every service is structured with **Clean Architecture** + **Hexagonal Architecture (Ports & Adapters)**:

| Layer                         | Role                                                                                                          | Dependency Rule        |
| ----------------------------- | ------------------------------------------------------------------------------------------------------------- | ---------------------- |
| **Domain (Core)**             | Enterprise rules, base entities (`User`, `Order`, `Donation`). Zero framework dependencies.                   | None                   |
| **Application (Use Cases)**   | Business logic orchestrating domain entities. Defines Port interfaces for repositories and external services. | → Domain only          |
| **Infrastructure (Adapters)** | Implements Ports: DB repositories, Kafka producers/consumers, external API clients.                           | → Application + Domain |
| **Presentation / API**        | HTTP/gRPC controllers (Quarkus reactive routes, .NET controllers, Fiber handlers, Axum routers).              | → Application          |

### 2.3 Domain-Driven Design Bounded Contexts

```
┌─────────────────┬─────────────────┬─────────────────┬───────────────────────┐
│    Identity     │    Catalog      │     Order       │      Charity          │
│  (Users, Auth,  │  (Products,     │  (Cart, Orders, │  (Donations,          │
│   Roles,        │   Inventory,    │   Checkout,     │   Campaigns,          │
│   Vendor OB)    │   Pledges)      │   Shipping)     │   Fund Routing)       │
├─────────────────┼─────────────────┼─────────────────┼───────────────────────┤
│  [Quarkus]      │  [.NET 10]      │  [Go Fiber]     │  [Rust Axum]          │
└─────────────────┴─────────────────┴─────────────────┴───────────────────────┘
```

---

## 3. Microservices Breakdown

### 3.1 Core Services

| Service                 | Stack                  | Responsibilities                                                                                                                  | Database                   | Dependencies                        |
| ----------------------- | ---------------------- | --------------------------------------------------------------------------------------------------------------------------------- | -------------------------- | ----------------------------------- |
| **User & Identity**     | Quarkus / Java 21      | Auth, JWT, OAuth2, user profiles, vendor onboarding, charity org profiles                                                         | PostgreSQL                 | —                                   |
| **Catalog & Inventory** | ASP.NET Core / .NET 10 | Product CRUD, categories, Elasticsearch search, inventory locking, vendor-pledge rule engine                                      | PostgreSQL + Elasticsearch | User & Identity                     |
| **Order & Checkout**    | Golang Fiber           | Cart management, order aggregation, checkout state machine, shipping rate calc                                                    | PostgreSQL                 | Catalog (gRPC sync), User, Donation |
| **Donation & Payment**  | Rust Axum              | Payment gateway integration, donation calculation (round-ups, % splits), fund routing to charity wallets, transactional integrity | PostgreSQL                 | Order                               |
| **Shipping**            | TBD (Go or .NET)       | Carrier integrations (FedEx, UPS, USPS), tracking, rate calculation                                                               | PostgreSQL                 | Order                               |
| **Notification**        | TBD                    | Email, SMS, push notifications triggered by Kafka events                                                                          | PostgreSQL + Redis         | All (Kafka)                         |
| **Analytics**           | TBD                    | Reporting, metrics, dashboards                                                                                                    | PostgreSQL + ClickHouse    | All (Kafka)                         |

### 3.2 Supporting Services

| Service               | Technology                                            | Role                                                  |
| --------------------- | ----------------------------------------------------- | ----------------------------------------------------- |
| **API Gateway**       | YARP / Envoy                                          | Request routing, rate limiting, auth token validation |
| **Service Discovery** | Consul / Kubernetes DNS                               | Service registration and discovery                    |
| **Circuit Breaker**   | Resilience4j (Java), Polly (.NET), built-in (Go/Rust) | Fault tolerance per service                           |
| **Config / Secrets**  | Kubernetes ConfigMaps + Vault                         | Centralized, encrypted configuration                  |

### 3.3 Service Communication Patterns

#### Synchronous — gRPC (primary for internal service-to-service calls)

gRPC + Protobuf is the default for any call requiring an immediate response to proceed:

| Caller             | Callee                    | Purpose                                     |
| ------------------ | ------------------------- | ------------------------------------------- |
| API Gateway        | User & Identity (Quarkus) | Token validation on every request           |
| Order Service (Go) | Catalog Service (.NET 10) | Verify and lock inventory before payment    |
| Order Service (Go) | Donation & Payment (Rust) | Initiate payment + calculate donation split |

#### Asynchronous — Apache Kafka Domain Events

Used to decouple services, improve resilience, and implement the Saga Pattern:

| Event                       | Publisher                 | Consumers                      | Consumer Action                                           |
| --------------------------- | ------------------------- | ------------------------------ | --------------------------------------------------------- |
| `order.created`             | Order Service (Go)        | Notification, Shipping         | Send confirmation email; initiate shipment                |
| `order.paid`                | Donation & Payment (Rust) | Catalog (.NET), User (Quarkus) | Permanent inventory deduction; update philanthropic badge |
| `payment.failed`            | Donation & Payment (Rust) | Order Service (Go)             | Saga compensating transaction — release inventory         |
| `inventory.updated`         | Catalog Service (.NET)    | Order Service (Go)             | Refresh cart availability                                 |
| `user.registered`           | User & Identity (Quarkus) | Notification                   | Send welcome email                                        |
| `charity.donation.received` | Donation & Payment (Rust) | Analytics, User Service        | Update impact metrics                                     |

**Communication Flow Example (Order Placement):**

```
Order Service (Go Fiber)  ──gRPC──▶  Catalog Service (.NET 10)  [inventory lock]
        │
        ▼
Donation & Payment (Rust Axum)  ──── processes payment + routes donation ────
        │
        ▼  publishes ──▶  Kafka: order.paid
        │
        ├──▶ Catalog (.NET 10)     → permanent inventory deduction
        └──▶ User Service (Quarkus) → update Lifetime Philanthropic badge
```

---

## 4. Development Phases

### Phase 1: Foundation (Weeks 1–5)

**Objective:** Infrastructure, CI/CD pipeline, User & Identity Service, and Framework Services

#### Infrastructure Setup

- [ ] Kubernetes cluster (local: kind/k3s; cloud: EKS/GKE)
- [ ] PostgreSQL provisioned for user-service + audit tables
- [ ] Apache Kafka cluster (3-node, multi-AZ) + Dead Letter Queue setup
- [ ] API Gateway (YARP / Envoy) + Developer Portal (OpenAPI UI)
- [ ] Real-time Infrastructure: WebSocket server + Redis pub/sub for broadcasts
- [ ] Feature Flags Platform (LaunchDarkly / Unleash) + client SDKs in all stacks
- [ ] Audit Logging infrastructure: CDC, append-only tables, log shipment to ELK
- [ ] Monitoring stack: Prometheus + Grafana + ELK + Jaeger + custom dashboards for Feature Flags
- [ ] GitHub Actions CI/CD pipeline with ArgoCD + canary deployment setup

#### User & Identity Service (Quarkus / Java 21)

- [ ] Quarkus project scaffold — Clean Architecture layers (Domain, Application, Infrastructure, Presentation)
- [ ] User registration + email verification
- [ ] OAuth 2.0 + PKCE + JWT issuer (Quarkus OIDC)
- [ ] RBAC: `CUSTOMER`, `SELLER`, `CHARITY_ADMIN`, `PLATFORM_ADMIN`
- [ ] Reactive endpoints (RESTEasy Reactive)
- [ ] gRPC server for token validation calls from API Gateway
- [ ] Kafka producer: `user.registered`
- [ ] Unit + integration tests (JUnit 5 + Mockito + Quarkus Test + Testcontainers)

#### API Gateway with Developer Portal (F-005)

- [ ] OpenAPI / Swagger schema registry per service
- [ ] Developer portal: API docs, sandbox environment, API key management
- [ ] Rate limit configuration UI: per-endpoint quotas
- [ ] SDK auto-generation + changelog

#### Feature Flags Service Scaffold (F-006)

- [ ] LaunchDarkly / Unleash integration with all core services
- [ ] Feature flag admin UI: create, rollout %, track adoption
- [ ] Integration with User & Identity gRPC + all services

#### Audit Logging & Data Lineage (F-007)

- [ ] Append-only audit tables schema per service
- [ ] CDC (Change Data Capture) setup (Debezium / Kafka Connect)
- [ ] Correlation ID generation + propagation via OpenTelemetry
- [ ] ELK Stack integration for audit log searching

#### Deliverables

- Working CI/CD pipeline across all stacks
- User & Identity Service live and tested
- Auth system operational

---

### Phase 2: E-commerce Core (Weeks 6–11)

**Objective:** Catalog, Inventory, Order & Checkout services, Rate Limiting, and Real-time Notifications

#### Catalog & Inventory Service (ASP.NET Core / .NET 10)

- [ ] .NET project scaffold — Clean Architecture + EF Core
- [ ] Product CRUD (name, description, images/S3, price, variants)
- [ ] Category hierarchy (3 levels)
- [ ] Elasticsearch integration for search + real-time indexing
- [ ] Inventory management with soft-lock and release logic
- [ ] Vendor-pledge rules engine (percentage / fixed / 100%)
- [ ] gRPC server: `InventoryService.LockInventory`, `ConfirmDeduction`, `ReleaseInventory`
- [ ] Kafka consumer: `order.paid` → permanent inventory deduction
- [ ] Unit + integration tests (xUnit + Moq + Testcontainers + EF Core InMemory)

#### Order & Checkout Service (Golang Fiber)

- [ ] Go project scaffold — Clean Architecture layers
- [ ] Cart management (add, remove, update, merge guest→member)
- [ ] Checkout state machine (6-step flow)
- [ ] gRPC client → Catalog Service (inventory lock)
- [ ] gRPC client → Donation & Payment Service (payment initiation)
- [ ] Kafka producer: `order.created`, `order.updated`
- [ ] Shipping rate calculation (external carrier API calls)
- [ ] Unit + integration tests (Go `testing` + testify + Testcontainers + gRPC mocks)

#### Rate Limiting & Quotas (F-008)

- [ ] Global rate limiter (Envoy + Redis): 10,000 req/sec across all users
- [ ] Per-endpoint quotas: checkout (5/min), search (100/min), payment retries (3/24h)
- [ ] Seller-specific quotas: product uploads (100/day), API calls (10K/day)
- [ ] Adaptive throttling: progressive wait time under load
- [ ] Graceful degradation: 429 responses with Retry-After header

#### Real-time Notification Layer (F-009)

- [ ] WebSocket server (Go Fiber or Node.js)
- [ ] Kafka consumer → in-memory event broadcast via Redis pub/sub
- [ ] Live cart updates, inventory warnings, donation confirmations
- [ ] Fallback: Server-Sent Events (SSE) for browser compatibility
- [ ] Connection management: heartbeats, auto-reconnect with exponential backoff
- [ ] Unit + integration tests per service language

#### User Experience (Frontend)

- [ ] Product listing + detail pages
- [ ] Product search with filters
- [ ] Shopping cart
- [ ] Checkout flow (6 steps)
- [ ] Order history + tracking

#### Deliverables

- Product search and browsing functional
- Cart and order placement working end-to-end
- gRPC inter-service communication tested with contract tests

---

### Phase 3: Payments & Donations (Weeks 12–14)

**Objective:** Donation & Payment Service, charity features, and audit logging implementation

#### Donation & Payment Service (Rust / Axum)

- [ ] Rust project scaffold — Clean Architecture layers
- [ ] Stripe + PayPal gateway integration (PCI-DSS tokenization)
- [ ] Payment processing with full atomicity (no partial states)
- [ ] Donation calculation engine: round-ups, percentage splits, fixed amounts
- [ ] Fund routing to charity wallets (atomic transactions, zero data races)
- [ ] Refund handling + Saga compensating transactions
- [ ] Kafka producer: `order.paid`, `payment.failed`, `charity.donation.received`
- [ ] Unit + integration tests (cargo test + Axum `TestClient` + Testcontainers + proptest)

#### Audit Logging Implementation (F-007)

- [ ] Append-only audit tables: donation.audit, payment.audit, fund_routing.audit
- [ ] Change Data Capture (CDC) → Kafka for cross-service audit sync
- [ ] Correlation IDs across all RPCs/events (OpenTelemetry)
- [ ] Compliance UI: query audit history per donation, payment, user action
- [ ] 7-year retention for financial data (automated archival to S3)
- [ ] Full immutability: hash verification, no updates/deletes

#### Charity Features

- [ ] Charity org profile management (via User & Identity Service)
- [ ] Platform Admin: charity vetting workflow
- [ ] Checkout round-up donation UI
- [ ] Vendor-pledged product tagging (Catalog Service integration)
- [ ] Charity marketplace section
- [ ] Donation receipt generation (PDF + email)
- [ ] Impact Dashboard real-time updates (Kafka consumer in User Service)

#### Deliverables

- Payment processing fully operational
- Donation engine calculating and routing all types correctly
- Charity marketplace visible with vetted charities

---

### Phase 4: Advanced Features & Intelligence (Weeks 15–20)

**Objective:** ML-powered recommendations, fulfillment expansion, loyalty, testing framework, advanced analytics

#### 4a. Recommendation Engine (Weeks 15–16)

**Tech Stack:** Python FastAPI + scikit-learn / TensorFlow; separate microservice

- [ ] Content-based filtering: product similarity embeddings from descriptions/images
- [ ] Collaborative filtering: user-product interaction matrix, matrix factorization
- [ ] Contextual bandit: multi-armed bandit for real-time A/B testing recommendations
- [ ] Real-time ranking service: top-N cached recommendations per user segment (Redis)
- [ ] Kafka consumer: `order.paid`, `user.viewed_product` → index for training
- [ ] REST API: `/recommend?user_id={id}&context={checkout|homepage|search}`
- [ ] Metrics: CTR (click-through rate), conversion lift, diversity
- [ ] Fallback: simple trending/bestsellers if ML unhealthy
- [ ] Unit + integration tests (pytest + fixtures)

#### 4b. Multi-Vendor Fulfillment (FBA-lite) (Weeks 17–18)

**Responsible Service:** Catalog & Inventory (.NET 10) + Shipping Service

- [ ] Fulfillment center inventory tracking (separate from seller inventory)
- [ ] Inbound shipment management: tracking seller-to-FC shipments
- [ ] Commingled inventory model: same product from multiple sellers in same bin
- [ ] Return routing to fulfillment centers
- [ ] Commission on fulfillment: 2.5% per unit shipped
- [ ] Fulfillment provider integrations (3PL APIs): rate API calls, label generation
- [ ] Shipping Service routing: route orders to nearest FC based on zip code
- [ ] Unit + integration tests

#### 4c. Loyalty Program (Weeks 17–18)

**Tech Stack:** Go or .NET; parallel with FBA

- [ ] Points accrual: 1 point per $1 spent + 2x for charity purchases
- [ ] Tiered status: Bronze → Silver → Gold (lifetime spend thresholds)
- [ ] Redemption: 100 points = $1 off, exclusive perks per tier
- [ ] Gamification: badges ("Philanthropist" at $500 donated), leaderboards
- [ ] Smart matching: "Every point = 50¢ matched to chosen charity"
- [ ] Kafka producer: `loyalty.points_earned`, `loyalty.tier_upgraded`
- [ ] User Service consumer: update loyalty status display
- [ ] Unit + integration tests

#### 4d. A/B Testing Framework (Weeks 18–19)

**Tech Stack:** Feature Flags service + ML Ranking service + metrics aggregation

- [ ] Experiment platform: define variants (A / B / C), randomize users
- [ ] Metrics SDK: track custom events (add-to-cart, donation-view, checkout-complete, purchase)
- [ ] Statistical significance: detect winner at P < 0.05, power analysis
- [ ] Admin UI: run, pause, declare winner, roll out automatically
- [ ] Experiments: checkout round-up default (pre-checked vs. unchecked), button placement, recommendation ranking
- [ ] Integration: Feature Flags service % assignment + Metrics aggregation
- [ ] Unit + integration tests

#### 4e. Advanced Analytics & Reporting (Weeks 19–20)

**Tech Stack:** Kafka → ClickHouse (OLAP) + Grafana dashboards + DataDog/custom

- [ ] Cohort analysis: "Users registered in January → retention by week"
- [ ] Funnel analysis: "View product → add-to-cart → checkout → payment → donation"
- [ ] LTV prediction: "High-value customer indicators at day 1 → predict 12-month spend"
- [ ] Charity ROI: "Total raised by cause → unique donors → repeat donation rate"
- [ ] Seller health dashboard: sales trend, avg order value, repeat customer %, refund rate
- [ ] Custom events: track user behaviors across all services (Kafka events)
- [ ] Reports: exportable CSV/Excel, scheduled email delivery, BI tool integration
- [ ] Grafana dashboards: KPI cards, retention curves, cohort heatmaps, revenue trends

#### 4f. Advanced Product Search Ranking (Weeks 18–19, parallel with Analytics)

**Responsible Service:** Catalog & Inventory (.NET 10) + ML Ranking Service

- [ ] Personalized ranking: boost products by user interests (past purchases, browsing)
- [ ] Freshness boost: newer products ranked slightly higher
- [ ] Seller tier boost: verified/premium sellers ranked higher
- [ ] Engagement signals: products with high CTR / conversion get boost
- [ ] Seasonal relevance: "winter coats" boost in Dec–Jan
- [ ] Charity affinity: products from trusted sellers + matching charity get secondary boost
- [ ] ML ranking: LambdaMART / gradient boosted trees for learning-to-rank
- [ ] A/B test variants: popularity ranking vs. personalized ranking

#### Seller Portal Enhancement

- [ ] Fulfillment center onboarding: register as seller or setup FC partnership
- [ ] Loyalty program dashboard: points redeemed by tier, customer lifetime value
- [ ] Advanced analytics: cohort retention, customer LTV projection

#### Deliverables

- Recommendation engine live with 3+ ranking models in A/B test
- Multi-vendor fulfillment operational (pilot with 5 products)
- Loyalty program with 50+ enrolled customers
- Advanced analytics dashboards live across all roles
- A/B testing framework functional

---

### Phase 5: Polish, Scale & Launch (Weeks 21–26)

**Objective:** QA, security audit, feature completeness, production hardening, go-live

#### 5a. Admin Dashboard & Content Moderation (Weeks 21–22)

**Tech Stack:** React/Vue frontend + GraphQL API + Python moderation service

- [ ] Seller management: onboarding, verification, suspension, document upload tracking
- [ ] Charity management: vetting workflow, fund distribution tracking, impact metrics
- [ ] Dispute resolution: return/refund requests with evidence, timeline tracking
- [ ] Content moderation: auto-flag suspicious products (PII, banned keywords, URL safety)
- [ ] Human review: queue flagged products, approve/reject, appeals workflow
- [ ] Financial reporting: revenue splits, commission tracking, settlement reports (CSV)
- [ ] Incident response UI: real-time service health, alert config, pause auto-payments
- [ ] User management: KYC status, account holds, bulk actions
- [ ] Unit + integration tests (Jest/Cypress for frontend; Python/Go for backend)

#### 5b. Internationalization & Localization (Weeks 22–24)

**Scope:** 10+ countries, multi-language, multi-currency, regional compliance

- [ ] Frontend i18n: React-i18next or similar; extract all strings, translation CMS integration
- [ ] Backend: country-aware pricing, shipping rates, tax calculation per region
- [ ] Content localization: product descriptions, charity mission statements, error messages
- [ ] Currency support: real-time FX rates, display in user's local currency (via Stripe)
- [ ] Regional KYC: country-specific AML rules, tax ID validation
- [ ] Compliance: GDPR (EU), CCPA (CA), China data residency if expanding
- [ ] Payment gateways: Stripe + PayPal (global); add regional options (WeChat Pay, Alipay, etc.)
- [ ] Testing: multi-language UI tests; regional compliance checklist

#### 5c. Vendor Commission Automation & Payout (Weeks 23–24)

**Responsible Service:** Donation & Payment (Rust Axum) + Admin Dashboard

- [ ] Commission rules engine: flat %, tiered by volume, category-specific rates, charity pledges
- [ ] Payout batching: daily/weekly settlement cycles to seller bank accounts
- [ ] Escrow management: hold payments during return window; auto-release after deadline
- [ ] Tax reporting: 1099 generation for applicable sellers
- [ ] Stripe Connect / PayPal Payouts integration
- [ ] Fraud detection: flag abnormal commission patterns (e.g., 100% self-purchases)
- [ ] Transparent ledger: sellers see itemized commissions per order
- [ ] Unit + integration tests

#### 5d. Shipping Service & Notifications (Weeks 21–22)

**Tech Stack:** Go / .NET for Shipping; Node.js / Go for Notifications

- [ ] Shipping Service: carrier integrations (FedEx, UPS, USPS), tracking, rate calculation
- [ ] Notification Service: email templates (SendGrid), SMS (Twilio), push notifications
- [ ] Kafka consumers for all domain events → notification triggers

#### Testing & QA (Weeks 24–25)

- [ ] Cross-service integration tests (all 7+ services in one test scenario)
- [ ] E2E tests (Playwright): full purchase + donation + loyalty redeem + seller analytics
- [ ] Load testing: 5,000 concurrent users on checkout flow (k6 + Grafana k6 Cloud)
- [ ] Chaos Engineering: simulate network delays (300ms), service failures, Kafka degradation
- [ ] Security audit: OWASP ZAP, SonarQube, penetration test, credential scanning
- [ ] UAT with stakeholders: sellers test portal, charities verify fund routing, admins review dashboards

#### Monitoring & Observability (Weeks 24–25)

- [ ] Distributed tracing: all 4 core services + supporting services (Jaeger + OpenTelemetry)
- [ ] SLA alerting rules: payment failures, search latency, Kafka lag
- [ ] Per-service dashboards: QPM, latency percentiles (P50/P95/P99), error rates, GC/heap (JVM), Goroutines (Go)
- [ ] Cost monitoring: per service resource usage, scaling trends
- [ ] Feature flag health: adoption rate, error correlation, rollback triggers

#### Deployment (Weeks 25–26)

- [ ] Staging environment: production-like Kubernetes, multi-AZ, all data replicated
- [ ] Production deployment: blue-green strategy, canary for risky features (ML ranking)
- [ ] DNS + SSL: Cert-Manager + Let's Encrypt, auto-renewal
- [ ] Database hardening: PostgreSQL PITR backups per service, cross-region replication, failover drills
- [ ] Incident runbooks: payment failure response, service degradation escalation, data loss recovery
- [ ] Backup validation: monthly restore test from Production snapshot to recovery environment

#### Documentation (Weeks 25–26)

- [ ] OpenAPI / Swagger: auto-generated per service, sandbox endpoints
- [ ] Protobuf / gRPC contracts: Buf Schema Registry, breaking change detection in CI
- [ ] Kafka topic schema registry: Confluent Schema Registry or Apicurio setup
- [ ] Architecture Decision Records (ADRs): polyglot rationale, service boundaries, API versioning
- [ ] Operations runbook: alerts explained, common fixes, escalation paths
- [ ] Developer onboarding: local Docker Compose setup, test data seeding, branch strategy

#### Deliverables

- Production-ready system with all Phase 1–4 features operational
- Admin dashboard live and tested
- i18n support for 10+ languages; multi-currency operational
- Automated seller payouts running daily
- Full audit trail (7-year retention) verified
- Go-live readiness approved by stakeholders

---

### Phase 6: Post-Launch (Ongoing)

- Monitoring + incident response
- Feature iterations based on user feedback
- Performance optimization per service (JVM tuning, Go profiling, Rust flamegraphs)
- Security updates + dependency patches
- Kubernetes HPA tuning for sustained scale

---

## 5. Testing Strategy

### 5.1 Testing Pyramid

```
              ┌──────────────┐
              │   E2E Tests  │  ~50 scenarios (Playwright / Cypress)
              └──────┬───────┘
        ┌────────────┴────────────┐
        │   Integration Tests     │  ~200 tests (cross-service, Testcontainers)
        └────────────┬────────────┘
   ┌─────────────────┴──────────────────┐
   │           Unit Tests               │  ~1,000 tests (per service, mocked deps)
   └────────────────────────────────────┘
```

### 5.2 Testing Tools by Service

| Service                           | Unit Tests             | Integration Tests                           | Notes                                  |
| --------------------------------- | ---------------------- | ------------------------------------------- | -------------------------------------- |
| User & Identity (Quarkus/Java 21) | JUnit 5 + Mockito      | Quarkus Test + Testcontainers + RestAssured | `@QuarkusTest` for reactive endpoints  |
| Catalog & Inventory (.NET 10)     | xUnit + Moq            | Testcontainers + xUnit                      | EF Core InMemory for fast unit tests   |
| Order & Checkout (Go Fiber)       | Go `testing` + testify | Go + Testcontainers + gRPC mocks            | Table-driven test patterns             |
| Donation & Payment (Rust Axum)    | cargo test             | Axum `TestClient` + Testcontainers          | Property-based testing with `proptest` |

### 5.3 Other Test Types

| Type              | Scope                         | Tools                        | Frequency |
| ----------------- | ----------------------------- | ---------------------------- | --------- |
| Contract Tests    | gRPC + API compatibility      | Pact, Buf (Protobuf schemas) | Weekly    |
| E2E Tests         | Full user flows               | Playwright, Cypress          | Nightly   |
| Performance Tests | Load & stress (5K concurrent) | k6 + Grafana k6 Cloud        | Bi-weekly |
| Security Tests    | Vulnerability scanning        | OWASP ZAP, SonarQube         | Weekly    |

### 5.4 Test Environments

| Environment | Type   | Infrastructure                                                                    |
| ----------- | ------ | --------------------------------------------------------------------------------- |
| Dev         | Local  | Docker Compose (all 4 core services + Kafka + PostgreSQL + Redis + Elasticsearch) |
| Test        | Auto   | Kubernetes (shared cluster)                                                       |
| Staging     | Manual | Kubernetes (production-like, multi-AZ)                                            |
| Production  | Live   | Kubernetes (multi-AZ, multi-cloud)                                                |

---

## 6. Deployment Strategy

### 6.1 Kubernetes Architecture

```
┌────────────────────────────────────────────────────────────────────────────┐
│                        BLUE MARKET — KUBERNETES                            │
│  namespaces: blue-market-dev | blue-market-staging | blue-market-prod      │
├────────────────────────────────────────────────────────────────────────────┤
│  INGRESS: NGINX Ingress Controller | Cert-Manager | External DNS           │
├────────────────────────────────────────────────────────────────────────────┤
│  CORE SERVICE LAYER                                                        │
│  ┌─────────────────┐  ┌──────────────────┐  ┌─────────────────┐           │
│  │ User & Identity │  │ Catalog &        │  │ Order &         │           │
│  │ Quarkus/Java21  │  │ Inventory .NET10 │  │ Checkout Go     │           │
│  │  (2 pods)       │  │  (3 pods)        │  │  (3 pods)       │           │
│  └─────────────────┘  └──────────────────┘  └─────────────────┘           │
│  ┌─────────────────┐  ┌──────────────────┐  ┌─────────────────┐           │
│  │ Donation &      │  │ Shipping Svc     │  │ Notification Svc│           │
│  │ Payment Rust    │  │  (2 pods)        │  │  (2 pods)       │           │
│  │  (2 pods)       │  └──────────────────┘  └─────────────────┘           │
│  └─────────────────┘                                                       │
├────────────────────────────────────────────────────────────────────────────┤
│  DATA LAYER                                                                │
│  PostgreSQL (per svc) │ Redis (cache) │ Kafka (3-node) │ Elasticsearch     │
└────────────────────────────────────────────────────────────────────────────┘
```

### 6.2 CI/CD Pipeline

```
Per-Service GitHub Actions Pipeline:
  1.  Code Checkout
  2.  Lint & Format Check       (golangci-lint / dotnet format / clippy / checkstyle)
  3.  Unit Tests                 (parallel per service language)
  4.  Build Docker Image
  5.  Push to Registry           (GHCR)
  6.  Deploy to Dev              (auto, ArgoCD GitOps)
  7.  Integration Tests          (Testcontainers, gRPC contracts)
  8.  Deploy to Staging          (manual approval)
  9.  E2E Tests                  (Playwright)
  10. Deploy to Production       (manual approval, blue-green)
```

### 6.3 Deployment Strategies

| Strategy       | Use Case         | Implementation                                        |
| -------------- | ---------------- | ----------------------------------------------------- |
| **Blue-Green** | Major releases   | Instant traffic switchover; old env kept for rollback |
| **Canary**     | Gradual rollouts | 5% → 25% → 100% with Istio traffic splitting          |
| **Rolling**    | Minor updates    | Pod-by-pod replacement                                |
| **Rollback**   | Failure recovery | One-click revert via ArgoCD                           |

### 6.4 Multi-Cloud Setup

```
┌──────────────────────┐         ┌──────────────────────┐
│       AWS EKS        │◄──Sync─►│       GCP GKE        │
│     (Primary)        │         │     (Secondary)      │
│  All 4 core services │         │  Replica services    │
│  PostgreSQL (writer) │         │  PostgreSQL (reader) │
└──────────┬───────────┘         └───────────┬──────────┘
           └──────────────┬──────────────────┘
                          ▼
                 ┌───────────────┐
                 │  Cloudflare   │
                 │  (DNS + CDN)  │
                 └───────────────┘
```

---

## 7. Timeline & Milestones

| Phase                         | W1  | W2  | W3  | W4  | W5  | W6  | W7  | W8  | W9  | W10 | W11 | W12 | W13 | W14 | W15 | W16 | W17 | W18 | W19 | W20 | W21 | W22 | W23 | W24 | W25 | W26 |
| ----------------------------- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Phase 1: Foundation + Portals | ██  | ██  | ██  | ██  | ██  |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Phase 2: E-comm + Real-time   |     |     |     |     |     | ██  | ██  | ██  | ██  | ██  | ██  |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Phase 3: Payments + Audit     |     |     |     |     |     |     |     |     |     |     |     | ██  | ██  | ██  |     |     |     |     |     |     |     |     |     |     |     |     |
| Phase 4: Advanced Features    |     |     |     |     |     |     |     |     |     |     |     |     |     |     | ██  | ██  | ██  | ██  | ██  | ██  |     |     |     |     |     |     |
| Phase 5: Polish & Launch      |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     | ██  | ██  | ██  | ██  | ██  | ██  |

| Milestone                     | Week | Deliverables                                                                                                    |
| ----------------------------- | ---- | --------------------------------------------------------------------------------------------------------------- |
| **M1: Enhanced Foundation**   | 5    | Infrastructure with audit/real-time/feature flags, User & Identity Service, API Gateway Developer Portal, CI/CD |
| **M2: Commerce + Engagement** | 11   | Catalog, Order, Rate Limiting, WebSocket real-time layer, A/B test scaffold                                     |
| **M3: Payments + Trust**      | 14   | Donation & Payment, audit logging complete, full financial integrity                                            |
| **M4: Intelligence Layer**    | 20   | Recommendations, FBA, Loyalty, Analytics, Advanced Search Ranking operational                                   |
| **M5: Enterprise Ready**      | 26   | Admin Dashboard, i18n, payout automation, chaos tested, go-live **Production Ready**                            |

---

## 8. Risk Management

### 8.1 Risk Matrix

| Risk                                    | Probability | Impact     | Mitigation                                                                 |
| --------------------------------------- | ----------- | ---------- | -------------------------------------------------------------------------- |
| Polyglot complexity                     | High        | High       | Clear service boundaries; dedicated engineers per stack; contract tests    |
| Financial data race in Donation Service | Low         | Critical   | Rust Axum — race-free by compiler guarantee                                |
| gRPC / Protobuf schema drift            | Medium      | High       | Buf Schema Registry; automated contract tests (Pact + Buf CI)              |
| Kafka message loss                      | Low         | High       | Idempotent producers; consumer offset management; Dead Letter Queue        |
| Distributed transaction consistency     | Medium      | High       | Saga pattern with compensating events; Outbox pattern per service          |
| Checkout burst traffic                  | High        | High       | Golang Fiber + Kubernetes HPA for Order Service autoscaling                |
| Security vulnerabilities                | Low         | Critical   | OWASP ZAP + SonarQube in CI; monthly scans; quarterly pen tests            |
| Team skill gaps (polyglot)              | Medium      | Medium     | Dedicated engineers per stack; pair programming; shared architecture guild |
| **ML model quality**                    | **Medium**  | **High**   | **Rigorous train/test split; A/B test every model before production**      |
| **WebSocket connection storms**         | **Medium**  | **High**   | **Connection limits per user; graceful backoff; Redis backpressure**       |
| **Audit log bloat**                     | **Low**     | **Medium** | **Archival policy (S3); automated cleanup; indexed queries**               |
| **i18n compliance failures**            | **Medium**  | **High**   | **Regional legal review per language; payment gateway country validation** |

### 8.2 Contingency Plans

| Scenario                   | Plan                                                                             |
| -------------------------- | -------------------------------------------------------------------------------- |
| Database failure           | PITR recovery; read replicas per service; automated failover                     |
| Service down               | Circuit breakers (Resilience4j / Polly); fallback responses; K8s liveness probes |
| Payment processing failure | Queue payments for retry; Saga compensating events; manual override              |
| Kafka broker failure       | Replication factor 3; multi-AZ brokers; consumer group rebalancing               |
| DDoS attack                | Rate limiting at API Gateway; WAF; Cloudflare DDoS protection                    |

---

## 9. Team Structure

### 9.1 Recommended Team

| Role                               | Count   | Phase 1 | Phase 2 | Phase 3 | Phase 4 | Phase 5                    |
| ---------------------------------- | ------- | ------- | ------- | ------- | ------- | -------------------------- |
| Tech Lead / Architect              | 1       | ✓       | ✓       | ✓       | ✓       | ✓                          |
| Java / Quarkus Engineer            | 1–2     | ✓       | ✓       | ✓       | ✓       | ✓                          |
| .NET / C# Engineer                 | 1–2     | —       | ✓       | ✓       | ✓       | ✓                          |
| Go Engineer                        | 1–2     | —       | ✓       | ✓       | ✓       | ✓                          |
| Rust Engineer                      | 1       | —       | —       | ✓       | ✓       | ✓                          |
| **Real-time / WebSocket Engineer** | **1**   | **—**   | **✓**   | **✓**   | **✓**   | **✓**                      |
| **ML / Data Engineer**             | **1**   | **—**   | **—**   | **—**   | **✓**   | **✓**                      |
| **Frontend Engineers**             | **2–3** | **—**   | **✓**   | **✓**   | **✓**   | **✓** _(+1 for Dashboard)_ |
| **DevOps / Platform Engineer**     | **1–2** | **✓**   | **✓**   | **✓**   | **✓**   | **✓**                      |
| **QA / Test Automation Engineer**  | **1–2** | **—**   | **✓**   | **✓**   | **✓**   | **✓**                      |
| Product Manager                    | 1       | ✓       | ✓       | ✓       | ✓       | ✓                          |
| Designer                           | 1       | ✓       | ✓       | —       | —       | ✓ _(polish)_               |

**New Roles Added:**

- **Real-time / WebSocket Engineer** (Phase 2–5): WebSocket server, event broadcast, fallback SSE
- **ML / Data Engineer** (Phase 4–5): Recommendation models, analytics pipeline, A/B test evaluation
- **Extra Frontend** (+1 for Admin Dashboard in Phase 5)
- **Extra QA** (+1 for Phase 4–5 complexity)

**Headcount Summary:**

- Phase 1: 4–5 people
- Phase 2: 8–10 people
- Phase 3: 9–11 people
- Phase 4: 11–13 people
- Phase 5: 12–14 people

### 9.2 Sprint Structure

```
Sprint Duration: 2 weeks
├── Planning          (Day 1 — Monday)
├── Development       (Days 2–9)
│     └── Daily Standups (per service team; cross-team sync twice/week)
├── Code Review       (Ongoing via PR process)
├── Testing           (Days 10–11: unit + integration per service)
├── Demo              (Day 12)
└── Retrospective     (Day 13)
```

---

## Appendix A: Service Contracts

### A.1 API Gateway Routes

| Route                  | Service                        | Protocol |
| ---------------------- | ------------------------------ | -------- |
| `/api/auth/*`          | User & Identity (Quarkus)      | REST     |
| `/api/users/*`         | User & Identity (Quarkus)      | REST     |
| `/api/products/*`      | Catalog & Inventory (.NET 10)  | REST     |
| `/api/inventory/*`     | Catalog & Inventory (.NET 10)  | REST     |
| `/api/orders/*`        | Order & Checkout (Go Fiber)    | REST     |
| `/api/cart/*`          | Order & Checkout (Go Fiber)    | REST     |
| `/api/payments/*`      | Donation & Payment (Rust Axum) | REST     |
| `/api/donations/*`     | Donation & Payment (Rust Axum) | REST     |
| `/api/charity/*`       | User & Identity + Donation Svc | REST     |
| `/api/shipping/*`      | Shipping Service               | REST     |
| `/api/notifications/*` | Notification Service           | REST     |

### A.2 gRPC Service Definitions (Protobuf)

```protobuf
// inventory.proto — Catalog & Inventory Service (.NET 10)
service InventoryService {
  rpc LockInventory(LockRequest)       returns (LockResponse);
  rpc ReleaseInventory(ReleaseRequest) returns (ReleaseResponse);
  rpc ConfirmDeduction(DeductRequest)  returns (DeductResponse);
}

// payment.proto — Donation & Payment Service (Rust Axum)
service PaymentService {
  rpc ProcessPayment(PaymentRequest)          returns (PaymentResponse);
  rpc CalculateDonation(DonationCalcRequest)  returns (DonationCalcResponse);
  rpc RefundPayment(RefundRequest)            returns (RefundResponse);
}

// auth.proto — User & Identity Service (Quarkus)
service AuthService {
  rpc ValidateToken(TokenRequest)  returns (TokenResponse);
  rpc GetUserClaims(ClaimsRequest) returns (ClaimsResponse);
}
```

### A.3 Kafka Event Topics

```
blue-market.events
├── order.created              # Publisher: Order Service (Go)
│                              # Consumers: Notification → confirmation email
│                              #            Shipping   → initiate shipment
├── order.updated              # Publisher: Order Service (Go)
├── order.paid                 # Publisher: Donation & Payment (Rust)
│                              # Consumer:  Catalog (.NET 10) → permanent inventory deduction
│                              # Consumer:  User Service (Quarkus) → update philanthropic badge
├── payment.failed             # Publisher: Donation & Payment (Rust)
│                              # Consumer:  Order Service (Go) → Saga compensation
├── charity.donation.received  # Publisher: Donation & Payment (Rust)
│                              # Consumers: Analytics, User Service
├── inventory.updated          # Publisher: Catalog Service (.NET 10)
│                              # Consumer:  Order Service (Go) → refresh cart
├── user.registered            # Publisher: User & Identity (Quarkus)
│                              # Consumer:  Notification → welcome email
└── order.shipped              # Publisher: Shipping Service
                               # Consumers: Notification, Order Service
```

---

## Appendix B: Definition of Done

A service/feature is **Done** when:

1. ✅ Code complete and peer reviewed (PR approved by ≥ 1 reviewer)
2. ✅ Unit test coverage > 80%
3. ✅ Integration tests passing (Testcontainers)
4. ✅ gRPC / Kafka contracts validated (Buf / Pact)
5. ✅ No critical or security issues (SonarQube quality gate passed)
6. ✅ Deployed to staging environment
7. ✅ OpenAPI / Protobuf documentation updated
8. ✅ Product owner sign-off

---

## Appendix C: Enhanced Feature Specifications & SLOs

### C.1 API Gateway Developer Portal (F-005)

**Features:**

- OpenAPI / Swagger UI per service endpoint
- Real-time rate limit dashboard (quota usage, remaining)
- API key management (generate, revoke, rotate, expire)
- Billing & usage tracking per API key
- Sandbox environment (separate Kafka topic for test events)
- Webhook management for async integrations
- SDK auto-generation (Python, JavaScript, Go, etc.)

### C.2 Feature Flags Service (F-006)

**Tech Stack Options:** LaunchDarkly (SaaS) or Unleash (self-hosted)

**Integration Points:**

- Every core service calls feature flag SDK before executing risky code
- Admin UI: toggle flags, set % rollout, create user cohort exceptions
- Metrics: flag evaluation latency, adoption rate, error correlation

**Critical Flags (Phase 1):**

- `real_time_notifications_enabled` — safe to disable if WebSocket breaks
- `audit_logging_v2` — dual-write to new audit tables while validating
- `ml_ranking_enabled` — A/B test new ranking vs. Elasticsearch default

### C.3 Audit Logging Schema (F-007)

**Per-service audit table:**

```sql
CREATE TABLE audit_log (
  id BIGSERIAL PRIMARY KEY,
  entity_type VARCHAR(50),       -- "order", "donation", "product"
  entity_id BIGINT,
  action VARCHAR(50),            -- "created", "updated", "deleted", "paid"
  actor_id BIGINT,               -- user_id or system_id
  changes JSONB,                 -- { old_value, new_value }
  correlation_id UUID,           -- trace across services
  created_at TIMESTAMP,
  retention_expires_at TIMESTAMP -- 7 years for financial
);
```

**CDC Pipeline:** Kafka topic `audit-events` ← all services → centralized audit service → ELK

### C.4 Real-time Notification WebSocket Layer (F-009)

**Architecture:**

- Golang / Node.js WebSocket server
- Redis Pub/Sub for in-memory dispatch
- Kafka consumer → broadcast to subscribed clients
- Rooms: per-user, per-order, per-seller, broadcast

**Events:** cart.item_price_changed, order.status_changed, inventory.item_low_stock, donation.processed

**SLA:** < 500ms latency (P95)

### C.5 ML Ranking & Recommendation Engine (F-015/F-020)

**Python FastAPI + scikit-learn / TensorFlow Serving**

- Content-based filtering (item embeddings)
- Collaborative filtering (user-item matrix)
- Contextual bandit (online learning for A/B tests)
- LambdaMART ranking (for search results)
- Feature store (Feast / Redis)

**APIs:**

- `GET /api/ml/recommendations?user_id={id}&context=checkout`
- `POST /api/ml/search/rank` (rerank search candidates)

**SLA:** P95 latency < 200ms; fallback to trending if ML degrades

### C.6 Loyalty Program Data Model (F-017)

```sql
CREATE TABLE loyalty_account (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT UNIQUE,
  points_balance BIGINT,
  tier VARCHAR(50), -- BRONZE / SILVER / GOLD / PLATINUM
  lifetime_spend DECIMAL(12,2),
  created_at TIMESTAMP
);

CREATE TABLE loyalty_tiers (
  tier VARCHAR(50) PRIMARY KEY,
  lifetime_spend_threshold DECIMAL(12,2),
  perks JSONB -- { reward_multiplier, exclusive_access, etc. }
);
```

**Tier System:** Bronze ($0) → Silver ($500) → Gold ($2,000) → Platinum ($5,000)

### C.7 A/B Testing Framework (F-018)

**Experiment Lifecycle:**

1. Create Experiment (define A/B variants + metrics)
2. Assign Users (randomized cohorts, consistent hashing)
3. Collect Metrics (Kafka events + Analytics service)
4. Analyze (statistical test, P < 0.05, effect size)
5. Declare Winner & Rollout

### C.8 Admin Dashboard (F-021)

**Components:**

- Seller vetting workflow (auto-approve → manual review → rejection)
- Charity vetting & fund transfer verification
- Dispute resolution (return/refund with evidence tracking)
- Content moderation (auto-flag + human review)
- Financial reporting (revenue splits, commission tracking)

### C.9 i18n / l10n Implementation (F-023)

**Languages (Launch):** EN, ES, FR, DE, IT, JA, ZH, KO, PT (10 by launch)

**Backend:**

- Country → currency mapping
- Localized error messages per country
- Regional payment gateways: WeChat Pay (CN), Alipay (CN), iDEAL (NL)

**Frontend:** React-i18next, RTL support scaffolded (Arabic, Hebrew for future)

### C.10 Service-Level Objectives (SLOs)

| Service             | Availability | Latency (P95) | Error Rate         |
| ------------------- | ------------ | ------------- | ------------------ |
| User & Identity     | 99.95%       | < 50ms        | < 0.05%            |
| Catalog & Inventory | 99.9%        | < 200ms       | < 0.1%             |
| Order & Checkout    | 99.99%       | < 100ms       | < 0.01%            |
| Donation & Payment  | 99.99%       | < 150ms       | **zero tolerance** |
| API Gateway         | 99.95%       | < 30ms        | < 0.03%            |
| WebSocket Real-time | 99.5%        | < 500ms       | < 0.5%             |
| ML Ranking          | 99% (soft)   | < 200ms       | < 1% (fallback)    |

---

_Document Version: 3.0 | Updated: 2026-04-12 | All 15 Strategic Enhancements Integrated_
