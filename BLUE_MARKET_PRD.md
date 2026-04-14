# Blue Market — Product Requirements Document (PRD)

> **Version:** 2.0 | **Status:** Active | **Date:** 2026-04-11

---

## Table of Contents

1. [Executive Summary](#1-executive-summary)
2. [Product Vision](#2-product-vision)
3. [User Personas](#3-user-personas)
4. [Market Opportunity](#4-market-opportunity)
5. [Core Features](#5-core-features)
6. [Feature Specifications](#6-feature-specifications)
7. [User Flows](#7-user-flows)
8. [Data Requirements](#8-data-requirements)
9. [Non-Functional Requirements](#9-non-functional-requirements)
10. [Success Metrics](#10-success-metrics)
11. [Glossary](#11-glossary)

---

## 1. Executive Summary

### 1.1 What is Blue Market?

Blue Market is a next-generation e-commerce platform that natively integrates charitable giving into every transaction. It serves four user types — **Customers, Vendors/Sellers, Charity Organizations, and Platform Admins** — across a polyglot microservices architecture built for scale, financial integrity, and resilience.

### 1.2 Problem Statement

| #   | Problem                                               | Impact                       |
| --- | ----------------------------------------------------- | ---------------------------- |
| 1   | Charitable giving friction in e-commerce              | Low donation adoption        |
| 2   | Sellers lack platforms connecting commerce and causes | Missed brand differentiation |
| 3   | Donors don't know where money goes                    | Eroded trust                 |
| 4   | No unified charity-commerce marketplace               | Fragmented user experience   |

### 1.3 Solution

Blue Market combines a full-featured e-commerce platform with built-in charity infrastructure — donation round-ups, vendor-pledged products, and a dedicated charity marketplace — making giving a natural part of every transaction.

### 1.4 Value Proposition

| Stakeholder   | Value                                                                      |
| ------------- | -------------------------------------------------------------------------- |
| **Buyers**    | Shop while making a difference; transparent, per-order impact tracking     |
| **Sellers**   | Differentiate through purpose; build a loyal, values-aligned customer base |
| **Charities** | New fundraising channel; direct connection to active shoppers              |
| **Platform**  | Unique market position; sustainable revenue with social impact             |

---

## 2. Product Vision

### 2.1 Mission

> _"Making charitable giving an integral part of every shopping experience."_

### 2.2 Product Pillars

| Pillar                   | Description                                               |
| ------------------------ | --------------------------------------------------------- |
| **Seamless Integration** | Charity feels natural, not forced, at every touchpoint    |
| **Radical Transparency** | Every donated dollar tracked, visible, and verifiable     |
| **Community First**      | Built by and for conscious shoppers and social causes     |
| **Seller Empowerment**   | Simple tools for businesses to align commerce with values |

### 2.3 Future Vision (18 Months)

- AI-powered cause matching based on purchase history
- Social impact dashboard for communities
- Global charity partnerships program
- White-label solution for cause-based organizations

---

## 3. User Personas

### 3.1 Primary Personas

| Persona                                       | Role                | Key Goals                                                          | Key Pain Points                                         |
| --------------------------------------------- | ------------------- | ------------------------------------------------------------------ | ------------------------------------------------------- |
| **Emma** (28, Marketing Manager, SF)          | Conscious Shopper   | Shop ethically; track impact; support aligned businesses           | Doesn't trust where donations go; tired of greenwashing |
| **Marcus** (42, Small Business Owner, Austin) | Impact Seller       | Grow customer base; differentiate; support charity with sales      | Platform fees eat margins; complex charity admin        |
| **Sofia** (35, Development Director, Denver)  | Charity Coordinator | Diversify fundraising; reach younger donors; transparent reporting | Reliance on few major donors; dated fundraising methods |
| **David** (45, Platform Trust Manager)        | Platform Admin      | Vet charities; resolve disputes; monitor system + payment gateways | Fraudulent charity claims; payment gateway failures     |

> _"I want to know exactly where my money is going."_ — Emma
>
> _"I want my business to mean something more than profit."_ — Marcus
>
> _"I need new ways to engage donors tired of gala invitations."_ — Sofia

### 3.2 Secondary Personas

| Persona                        | Age | Goal                           | Opportunity                                                  |
| ------------------------------ | --- | ------------------------------ | ------------------------------------------------------------ |
| **Alex** – Deal Hunter         | 24  | Maximize value on tight budget | Round-up feels like "free" good deeds                        |
| **Jennifer** – Corporate Buyer | 48  | Source CSR gifts in bulk       | Enterprise marketplace + impact reporting for CSR compliance |

---

## 4. Market Opportunity

### 4.1 Market Size

| Segment              | TAM   | SAM   | SOM   |
| -------------------- | ----- | ----- | ----- |
| E-commerce Platforms | $6.5T | $150B | $500M |
| Charity Technology   | $25B  | $5B   | $100M |
| Integrated Solution  | —     | —     | $200M |

### 4.2 Target Segments

1. **SMB Sellers** — $50K–$2M revenue, 1–10 employees
2. **Mid-market Brands** — $2M–$50M revenue, 10–200 employees
3. **Charitable Organizations** — All sizes, registered 501(c)(3)
4. **Conscious Consumers** — Ages 18–45, mid-to-upper income

### 4.3 Competitive Analysis

| Competitor | Strengths        | Weakness                   | Blue Market Advantage            |
| ---------- | ---------------- | -------------------------- | -------------------------------- |
| Amazon     | Scale, logistics | Zero charity integration   | Purpose-built giving engine      |
| Shopify    | Easy setup       | No native charity features | Native charity layer at checkout |
| eBay       | Auction model    | Declining relevance        | Modern UX + cause focus          |
| GoodMarket | Charity focus    | Weak commerce features     | Full e-commerce + charity        |

### 4.4 Market Timing

- Post-pandemic shift to purpose-driven brands (85% of consumers)
- Gen Z demand for authentic corporate responsibility
- Increased regulatory focus on nonprofit transparency

---

## 5. Core Features

### 5.1 Feature Overview

| Feature                                  | Customer      | Seller         | Charity Admin      | Platform Admin    |
| ---------------------------------------- | ------------- | -------------- | ------------------ | ----------------- |
| **Authentication & Identity**            |               |                |                    |                   |
| Registration / Login                     | ✓             | ✓              | ✓                  | ✓                 |
| Social Login (OAuth)                     | ✓             | ✓              | ✓                  | —                 |
| MFA / TOTP                               | ✓             | ✓              | ✓                  | ✓ (required)      |
| **E-commerce Core**                      |               |                |                    |                   |
| Product Browsing & Search                | ✓             | —              | ✓                  | ✓                 |
| Shopping Cart                            | ✓             | —              | —                  | —                 |
| Checkout Flow                            | ✓             | —              | —                  | —                 |
| Order Tracking                           | ✓             | ✓              | ✓                  | ✓                 |
| Payment Processing                       | ✓             | —              | —                  | ✓                 |
| **Charity Features**                     |               |                |                    |                   |
| Round-up Donation at Checkout            | ✓             | —              | ✓                  | ✓                 |
| Vendor-Pledged Products                  | —             | ✓              | ✓                  | ✓                 |
| Charity Marketplace                      | ✓             | —              | ✓                  | ✓                 |
| Campaign Management                      | —             | —              | ✓                  | ✓                 |
| Donation Receipts                        | ✓             | —              | ✓                  | ✓                 |
| Impact Dashboard                         | ✓ (lifetime)  | ✓ (by product) | ✓ (total received) | ✓ (platform-wide) |
| **Seller Tools**                         |               |                |                    |                   |
| Seller Dashboard                         | —             | ✓              | —                  | —                 |
| Product Management                       | —             | ✓              | ✓                  | ✓                 |
| Analytics & Reporting                    | Limited       | ✓              | ✓                  | ✓                 |
| Payout Management                        | —             | ✓              | ✓                  | ✓                 |
| **NEW: Advanced Features**               |               |                |                    |                   |
| **API Gateway & Developer Portal**       | —             | ✓              | —                  | ✓                 |
| **Rate Limiting & Quotas**               | (enforced)    | (enforced)     | (enforced)         | ✓                 |
| **Real-time WebSocket Notifications**    | ✓             | ✓              | ✓                  | ✓                 |
| **Feature Flags & Progressive Rollouts** | (transparent) | (transparent)  | (transparent)      | ✓                 |
| **Audit Logging & Data Lineage**         | ✓ (view own)  | ✓ (view own)   | ✓ (view own)       | ✓ (all)           |
| **Recommendation Engine**                | ✓             | —              | —                  | ✓                 |
| **Multi-Vendor Fulfillment (FBA)**       | ✓             | ✓              | —                  | ✓                 |
| **Loyalty Program**                      | ✓             | ✓              | ✓                  | ✓                 |
| **A/B Testing & Experiments**            | (transparent) | (transparent)  | —                  | ✓                 |
| **Advanced Analytics**                   | Limited       | ✓              | ✓                  | ✓                 |
| **Advanced Search Ranking**              | ✓             | —              | —                  | ✓                 |
| **Admin Dashboard**                      | —             | —              | ✓                  | ✓                 |
| **Content Moderation**                   | ✓ (reports)   | ✓ (appeals)    | —                  | ✓                 |
| **Internationalization (i18n)**          | ✓             | ✓              | ✓                  | ✓                 |
| **Vendor Commission Automation**         | —             | ✓              | —                  | ✓                 |
| **Admin**                                |               |                |                    |                   |
| Charity Vetting                          | —             | —              | —                  | ✓                 |
| Dispute Resolution                       | —             | —              | —                  | ✓                 |
| System Health Monitor                    | —             | —              | —                  | ✓                 |

### 5.2 Feature Priority Matrix

| Priority | Features                                            | Effort | Value    |
| -------- | --------------------------------------------------- | ------ | -------- |
| **P0**   | User auth, product catalog, shopping cart, checkout | High   | Critical |
| **P0**   | Payment processing (Stripe/PayPal)                  | High   | Critical |
| **P0**   | Round-up donation at checkout                       | Medium | High     |
| **P1**   | Vendor-pledged charity products                     | Medium | High     |
| **P1**   | Charity marketplace section                         | High   | High     |
| **P1**   | Seller dashboard + analytics                        | High   | High     |
| **P2**   | Impact dashboard                                    | Medium | Medium   |
| **P2**   | Advanced seller analytics                           | High   | Medium   |
| **P3**   | Mobile app                                          | High   | Medium   |

---

## 6. Feature Specifications

### 6.1 User Authentication & Management

> **Responsible Service:** User & Identity Service — Quarkus, Java 21
> Reactive stack + Java 21 virtual threads handle thousands of concurrent auth/token-validation requests with minimal memory footprint.

#### F-001: User Registration

| Field                 | Value                                                                        |
| --------------------- | ---------------------------------------------------------------------------- |
| **Actors**            | Guest → Customer / Seller / Charity Admin / Platform Admin                   |
| **Main Flow**         | Sign Up → email + password + name → email verification link → account active |
| **Alt Flow: Social**  | OAuth with Google / Apple / Facebook                                         |
| **Alt Flow: Seller**  | Additional business verification step after registration                     |
| **Alt Flow: Charity** | Charity profile creation; awaits Platform Admin vetting                      |

**Acceptance Criteria:**

- ✓ Email uniqueness enforced at DB level
- ✓ Password: 8+ chars, 1 uppercase, 1 number; hashed with bcrypt (cost 12)
- ✓ Verification email delivered within 5 seconds
- ✓ Account locked until email verified

#### F-002: Authentication & Authorization

| Specification    | Value                                                        |
| ---------------- | ------------------------------------------------------------ |
| Protocol         | OAuth 2.0 with PKCE                                          |
| Tokens           | JWT — Access: 15 min / Refresh: 7 days                       |
| Password Hashing | bcrypt (cost factor 12)                                      |
| MFA              | TOTP — optional for customers; mandatory for Platform Admins |
| Roles            | `CUSTOMER`, `SELLER`, `CHARITY_ADMIN`, `PLATFORM_ADMIN`      |

**Acceptance Criteria:**

- ✓ 5 failed login attempts → 15-min lockout
- ✓ JWT silent refresh without re-authentication
- ✓ Session invalidated immediately on password change
- ✓ Full RBAC enforced on every endpoint via Quarkus reactive security

---

### 6.2 Product & Catalog

> **Responsible Service:** Catalog & Inventory Service — ASP.NET Core, .NET 10
> DDD with EF Core handles complex relational mapping between products, vendors, inventory, and vendor-pledge rules.

#### F-010: Product Browsing

**Features:** 3-level category hierarchy, featured/recommended carousels, new arrivals, trending products, "Shop for Good" charity section.

| Requirement        | Value                              |
| ------------------ | ---------------------------------- |
| Category page load | < 500ms                            |
| Items per page     | 24 (infinite scroll available)     |
| Image loading      | Lazy — triggered at viewport entry |

**Acceptance Criteria:**

- ✓ Responsive: mobile / tablet / desktop
- ✓ Product card shows: image, name, price, rating, charity badge
- ✓ Quick view modal without page navigation

#### F-011: Product Search

| Specification | Value                                              |
| ------------- | -------------------------------------------------- |
| Engine        | Elasticsearch (integrated in Catalog Service)      |
| Indexing      | Real-time on product create/update via Kafka event |
| Ranking       | TF-IDF with charity product boost                  |
| Autocomplete  | Debounced 300ms                                    |

**Features:** Spell correction, fuzzy matching, filters (category, price, rating, charity tag), sort (relevance, price asc/desc, newest, best-selling), search history for logged-in users.

**Acceptance Criteria:**

- ✓ Search results returned < 200ms (P95)
- ✓ Charity-only filter shows verified products exclusively
- ✓ "No results" page suggests related products

#### F-012: Inventory Management

| Rule                | Value                                                                   |
| ------------------- | ----------------------------------------------------------------------- |
| Inventory lock      | Held synchronously via gRPC call from Order Service                     |
| Lock timeout        | Released after 15 min if checkout not completed                         |
| Permanent deduction | Triggered by consuming `order.paid` Kafka event (async)                 |
| Vendor-pledge rules | Configurable per product: 100% proceeds / % per sale / fixed $ per item |

**Acceptance Criteria:**

- ✓ Inventory locked via gRPC before payment step proceeds
- ✓ Permanent deduction only on confirmed `order.paid` event
- ✓ Vendor pledge rules stored with product; enforced by Donation Service

---

### 6.3 Shopping & Checkout

> **Responsible Service:** Order & Checkout Service — Golang Fiber
> Built for raw throughput and extreme concurrency; scales horizontally in milliseconds during flash sales.

#### F-020: Shopping Cart

| Rule                  | Value                                     |
| --------------------- | ----------------------------------------- |
| Max unique items      | 50                                        |
| Max quantity per item | 99                                        |
| Guest cart retention  | 30 days                                   |
| Member cart retention | Unlimited                                 |
| Inventory hold        | 15 min after add-to-cart (gRPC soft-lock) |

**Features:** Add/remove/update items, save for later (wishlist), real-time price + shipping estimate, charity contribution preview.

**Acceptance Criteria:**

- ✓ Cart badge updates on all pages in real time
- ✓ Guest cart merges with member cart on login
- ✓ Out-of-stock items flagged with clear warning
- ✓ Estimated delivery date displayed

#### F-021: Checkout Flow

6-step process, entirely owned by Order & Checkout Service (Golang Fiber):

| Step                 | Action                                               | Notes                                               |
| -------------------- | ---------------------------------------------------- | --------------------------------------------------- |
| 1 — Cart Review      | Edit quantities, promo codes, gift wrap              | Donation opt-in shown here                          |
| 2 — Shipping Address | Select/add address, toggle billing                   | Address validated via external API                  |
| 3 — Shipping Method  | Carrier options + delivery estimates, store pickup   | Rates from Shipping Service                         |
| 4 — Payment          | Credit/debit (Stripe), Apple/Google Pay, PayPal      | Tokenized by Donation & Payment Service (Rust Axum) |
| 5 — Order Review     | Full summary, terms checkbox, "Place Order"          | Donation line item confirmed                        |
| 6 — Confirmation     | Order number, email, tracking link, donation receipt | `order.created` Kafka event published               |

**Acceptance Criteria:**

- ✓ Checkout completes end-to-end in < 30s
- ✓ Inventory locked via gRPC from Catalog Service before payment
- ✓ Donation amount verified server-side by Donation & Payment Service
- ✓ `order.created` Kafka event published on successful placement

---

### 6.4 Charity Features

> **Responsible Service:** Donation & Payment Service — Rust Axum
> Absolute memory safety and zero data races — guaranteed at compile time. The financial ledger of the platform.

#### F-030: Round-up Donations

```
┌─────────────────────────────────────────────────────────────┐
│  Your Order Total: $47.23                                   │
│                                                             │
│  ☑ Round up to $48.00 → Donate $0.77                      │
│     Supporting: [Global Education Fund ▼]                   │
│                                                             │
│  "Help provide school supplies for 3 students"             │
└─────────────────────────────────────────────────────────────┘
```

| Rule         | Value                                                             |
| ------------ | ----------------------------------------------------------------- |
| Default      | Round-up enabled (user can opt out)                               |
| Calculation  | Client-side preview; server-side verification by Donation Service |
| Fund routing | Rust Axum routes funds atomically to charity wallet               |
| Receipt      | Separate line item on order confirmation + email                  |

**Default Causes:** Global Education Fund · Environmental Conservation · Hunger Relief · Healthcare Access · Community Development

**Acceptance Criteria:**

- ✓ User can change preferred cause at checkout
- ✓ Impact statement shown (e.g., "$0.77 = 1 day of clean water")
- ✓ Donation receipt included in confirmation email
- ✓ No data race possible — guaranteed by Rust compiler

#### F-031: Vendor-Pledged Products

| Tag Type                     | Description                                           |
| ---------------------------- | ----------------------------------------------------- |
| 100% proceeds                | Seller absorbs costs; entire sale price donated       |
| Percentage (e.g., 10%)       | Split per sale, routed atomically by Donation Service |
| Fixed amount (e.g., $1/item) | Fixed donation per unit sold                          |

**Requirements:** Verified charity partner required · Commitment binding for 90 days · Fundraising progress bar on product page.

**Acceptance Criteria:**

- ✓ Only Platform Admin-vetted charities selectable as pledge targets
- ✓ Pledge splits calculated atomically in Rust Axum (no rounding errors)
- ✓ Fraud detection flags anomalous pledge claims for Admin review

#### F-032: Charity Marketplace

**Sections:** Featured Causes · Shop by Cause · Top Fundraisers · New from Charities · Verified Charity Sellers

| Cause Category        | Description                                 |
| --------------------- | ------------------------------------------- |
| Education & Youth     | Schools, scholarships, educational programs |
| Environment & Animals | Conservation, wildlife protection           |
| Health & Medical      | Medical research, healthcare access         |
| Hunger & Poverty      | Food security, poverty relief               |
| Disaster Relief       | Emergency response, recovery support        |
| Arts & Culture        | Arts programs, cultural preservation        |
| Community & Social    | Local community development                 |
| Human Rights          | Advocacy and protection                     |

**Acceptance Criteria:**

- ✓ Each cause shows: total raised, donor count, active products
- ✓ Fundraising thermometer for active campaigns
- ✓ Monthly giving option available
- ✓ Shareable cause pages with full impact stats

#### F-033: Impact Dashboard

> User Service (Quarkus, Java 21) consumes the `order.paid` Kafka event to update the customer's Lifetime Philanthropic Impact badge in real time.

```
┌──────────────────────────────────────────────┐
│  YOUR IMPACT JOURNEY                         │
│                                              │
│  Total Donated:       $347.50                │
│  Orders w/ Donations: 23                     │
│  CO₂ Offset:          45 kg                  │
│  Lives Impacted:      12                     │
│                                              │
│  [Share Your Impact]  [Get Monthly Report]   │
└──────────────────────────────────────────────┘
```

| View           | Content                                                                  |
| -------------- | ------------------------------------------------------------------------ |
| Customer       | Lifetime donations, breakdown by cause, impact statements                |
| Seller         | Total raised through charity products, by cause, engagement rate         |
| Charity Admin  | Total received, donation trends, donor demographics (aggregated), export |
| Platform Admin | Platform-wide totals, charity performance, fraud flags                   |

---

### 6.5 Seller Portal

#### F-040: Seller Registration & Verification

**Steps:** Business info (name, type, Tax ID) → Contact details → Address → Bank account → Document upload → Terms acceptance

| Tier         | Products  | Platform Fee | Features                               |
| ------------ | --------- | ------------ | -------------------------------------- |
| Starter      | ≤ 50      | 2%           | Basic listing & order management       |
| Professional | Unlimited | 1.5%         | Analytics, promotions, charity tagging |
| Enterprise   | Unlimited | Custom       | Dedicated support, full API access     |

**Acceptance Criteria:**

- ✓ Automated business name/ID validation on submission
- ✓ Manual document review completed within 48 hours
- ✓ Payout access restricted until full verification complete

#### F-041: Product Management

| Field       | Specification                                 |
| ----------- | --------------------------------------------- |
| Title       | max 200 chars                                 |
| Description | rich text, max 5,000 chars                    |
| Images      | 1–8, max 5 MB each (Amazon S3)                |
| Video       | optional, max 500 MB                          |
| Variations  | size, color, custom attributes                |
| Charity tag | optional; requires verified charity selection |

**Bulk Operations:** CSV import/export · Bulk edit · Bulk pricing updates · Inventory sync via API

#### F-042: Order Management

**Features:** Filterable/sortable order list · bulk label printing · mark-as-shipped · partial shipment · return/refund workflow · customer message thread.

#### F-043: Analytics & Reporting

| Metric         | Description                                           |
| -------------- | ----------------------------------------------------- |
| Today's Sales  | vs. yesterday with % change                           |
| Orders         | by status: pending / processing / shipped / delivered |
| Revenue        | gross / platform fees / net                           |
| Top Products   | best sellers by volume and revenue                    |
| Charity Impact | donations raised by product/campaign                  |

**Reports:** Sales by period · Product performance · Inventory status · Payout history · Tax documents
**Exports:** CSV/Excel · Scheduled email reports · API access for BI tools

### 6.6 Recommendation Engine (F-015)

> **Responsible Service:** ML Ranking Service — Python FastAPI

#### F-089: Personalized Product Recommendations

**Algorithm Models (A/B tested):**

- **Content-Based:** Product embeddings from descriptions + images
- **Collaborative Filtering:** User-item interaction matrix
- **Contextual Bandit:** Real-time multi-arm optimization for A/B tests
- **Trending:** Fallback if user new, popular products + engagement signal

**Endpoints:**

- `GET /api/products/{id}/similar` → 6 similar products
- `GET /api/recommendations/homepage` → 3 carousels (personalized, trending, charity-featured)
- `GET /api/search/{query}/ranked` → reranked search results

**Metrics Tracked:** CTR, conversion lift, diversity score

**Acceptance Criteria:**

- ✓ Recommendations < 200ms (P95); fallback to trending if ML down
- ✓ Monthly model retraining from historical Kafka events
- ✓ Privacy-first: no cross-user data leak

---

### 6.7 Real-time WebSocket Notifications (F-009)

#### F-082: Live Cart & Inventory Updates

**Events Broadcast:**

- User adds product → cart badge updates immediately
- Product price changes → "Price Drop! Was $50, now $40"
- Product goes out of stock → out-of-stock badge

**Acceptance Criteria:**

- ✓ WebSocket established < 1 second
- ✓ Updates broadcast < 500ms after Kafka event (P95)
- ✓ Fallback to polling if WebSocket unavailable (SSE)
- ✓ Auto-reconnect with exponential backoff (1s → 2s → 4s → 30s max)

#### F-083: Order Status Push Notifications

**Events:**

- `order.created` → "Order #123 received!"
- `order.shipped` → "Your order is on the way!"
- `order.delivered` → "Delivery confirmed. Thanks!"
- `donation.processed` → "Your $0.77 donation is helping 3 kids!"

---

### 6.8 API Gateway & Developer Portal (F-005)

#### F-084: API Key Management

| Feature          | Description                                                  |
| ---------------- | ------------------------------------------------------------ |
| Key generation   | Dev portal: generate API keys (OAuth 2.0 client credentials) |
| Rate limit tiers | Standard (100 req/min), Pro (1,000), Enterprise (custom)     |
| Usage dashboard  | Real-time requests, response times, error rates              |
| Quota reset      | Monthly; unused doesn't roll over                            |
| Key rotation     | Schedule expiration + auto-generate replacement              |

**Acceptance Criteria:**

- ✓ API keys issued immediately
- ✓ Rate limit enforced < 1ms (Envoy)
- ✓ 429 response includes Retry-After header
- ✓ Usage dashboard updates < 5 seconds

---

### 6.9 Feature Flags & Progressive Rollouts (F-006)

#### F-085: Feature Flag Admin Interface

| Action             | Description                                          |
| ------------------ | ---------------------------------------------------- |
| Create Flag        | Define name, description, default value              |
| Percentage Rollout | Roll out 5% → 25% → 100% over time                   |
| User Cohorts       | Target specific users (user_id, seller_tier, region) |
| Track Adoption     | % users experiencing flag, error correlation         |
| Rollback           | Disable instantly if errors detected                 |

**Critical Flags:**

- `real_time_notifications_enabled` — disable if WebSocket fails
- `ml_ranking_v2_enabled` — A/B test new ranking
- `loyalty_points_2x_enabled` — experimental 2x multiplier
- `audit_logging_comprehensive` — dual-write to new audit tables

**Acceptance Criteria:**

- ✓ Flag evaluation < 10ms (locally cached)
- ✓ Rollback < 30 seconds
- ✓ Percentage rollout uses consistent hashing

---

### 6.10 Audit Logging & Compliance (F-007)

#### F-086: Immutable Audit Trail

**Audited Events:**
| Entity | Events |
|--------|--------|
| User | registration, password change, role change, MFA toggle |
| Order | created, paid, shipped, refunded |
| Donation | calculated, routed, received, disputed |
| Payment | attempted, succeeded, failed, refunded |
| Charity | verified, fund withdrawal |

**Acceptance Criteria:**

- ✓ Append-only; impossible to update/delete
- ✓ Hash chain: each record includes hash of previous (detect tampering)
- ✓ All services publish `audit-events` Kafka topic
- ✓ Compliance queries < 5 seconds
- ✓ 7-year retention for financial data

#### F-087: Compliance Dashboard

| Query                                       | Output                        |
| ------------------------------------------- | ----------------------------- |
| "Audit history for order #123"              | Full event chain              |
| "All donations for charity X since Q4 2026" | CSV export                    |
| "User consent records for retention"        | Verify GDPR right-to-deletion |
| "1099 transactions for seller Y"            | Auto-generate tax docs        |

---

### 6.11 Rate Limiting & Quotas (F-008)

#### F-088: Multi-Level Rate Limiting

```
API Gateway (Envoy)
├─ Global: 10,000 req/sec (prevent DDoS)
├─ Per-endpoint:
│  ├─ /api/checkout: 5 attempts/min/user (prevent spam)
│  ├─ /api/search: 100 queries/min/user (prevent scraping)
│  └─ /api/payments/retry: 3 per 24h (fraud prevention)
├─ Per-seller API: 1K (Starter) → 10K (Pro) → custom (Enterprise)
└─ Graceful: 429 + Retry-After header
```

**Acceptance Criteria:**

- ✓ Rate limit enforced < 1ms (Envoy)
- ✓ Redis tracks quota (atomic increment)
- ✓ Quota resets UTC midnight
- ✓ Abuse: 10× over quota → temp suspension

---

### 6.12 Multi-Vendor Fulfillment (F-016)

#### F-091: Fulfillment Center Management

| Feature              | Description                                     |
| -------------------- | ----------------------------------------------- |
| Inventory Model      | Sellers store in Blue Market's FCs              |
| Inbound Tracking     | Sellers ship to FC; we track arrival + scanning |
| Commingled Inventory | Same product from multiple sellers in same bin  |
| Fulfillment Fee      | 2.5% per unit shipped from FC                   |
| Return Routing       | Returns → FC for inspection + restocking        |

**Fulfillment Center Selection:**

- Customer zip 90210 (LA) → FC-West
- Customer zip 10001 (NY) → FC-East
- Customer zip 60601 (Chicago) → FC-Central

---

### 6.13 Loyalty Program (F-017)

#### F-093: Points Accrual & Redemption

| Action                      | Points          |
| --------------------------- | --------------- |
| Purchase $1                 | 1 point         |
| Charity donation (round-up) | 2x points       |
| Vendor-pledged product      | 1.5x points     |
| Referral                    | 50 bonus points |
| Birthday bonus              | 25 points/year  |

**Tier System:**

| Tier     | Lifetime Spend | Perks                                   |
| -------- | -------------- | --------------------------------------- |
| BRONZE   | $0             | Baseline (1x multiplier)                |
| SILVER   | $500           | 1.2x multiplier, early sale access      |
| GOLD     | $2,000         | 1.5x, free shipping, exclusive products |
| PLATINUM | $5,000         | 2x, premium shipping, concierge support |

**Redemption:** 100 points = $1 off; 500 = $6; 1,000 = $12

#### F-094: Gamification

**Badges:**

- 🌟 "Getting Started" — 1st purchase
- 🎁 "Generous Soul" — $100 donated
- ♻️ "Green Shopper" — 10 eco products
- 🏅 "Philanthropist" — $500 donated; Platinum unlock

---

### 6.14 A/B Testing Framework (F-018)

#### F-095: Experiment Management

| Experiment       | Hypothesis                    | Metrics                     | Winner         |
| ---------------- | ----------------------------- | --------------------------- | -------------- |
| Round-up Default | Pre-checked → ↑ donation rate | Donation %, AOV, engagement | +5% donation   |
| Button Color     | Green beats blue              | CTR, conversion by age      | Gen Z prefers  |
| Search Ranking   | ML beats popularity           | Search→purchase conversion  | +3% conversion |
| Loyalty 2x       | 2x multiplier → ↑ repeat rate | Repeat %, LTV               | +10% repeat    |

**Acceptance Criteria:**

- ✓ Experiment config reproducible
- ✓ Users assigned consistently (hashed user_id + seed)
- ✓ Statistical significance (t-test, P < 0.05)
- ✓ Minimum 14-day runtime

---

### 6.15 Advanced Analytics (F-019)

#### F-096: Cohort Analysis

| Cohort   | W1   | W2  | W3  | W4  |
| -------- | ---- | --- | --- | --- |
| Jan 2026 | 100% | 45% | 28% | 18% |
| Feb 2026 | 100% | 52% | 35% | 22% |

#### F-097: Funnel Analysis

```
View Product (100K) → Add Cart (45K) → Checkout (27K)
→ Payment (20K) → Order (16K) → Donation (5K)
```

Biggest dropout: Step 2→3 (only 60% proceed).

#### F-098: LTV Prediction

**Model:** Predict 12-month LTV from Day 1 signals

- AOV $100 → high LTV
- Charity donation included → high LTV
- Eco-friendly products → high LTV
- Urban + desktop → high LTV

---

### 6.16 Advanced Search Ranking (F-020)

#### F-100: Personalized Search Results

**Ranking Factors:**

- Relevance (55%): TF-IDF from Elasticsearch
- Personalization (25%): past purchases, browsing
- Freshness (10%): newer products ranked higher
- Engagement (5%): CTR + conversion signals
- Seller Tier (3%): verified sellers boosted
- Charity Tag (2%): charity products boosted

**Acceptance Criteria:**

- ✓ Ranking computed < 200ms (P95)
- ✓ A/B test vs. baseline Elasticsearch
- ✓ Personalization: no privacy leak
- ✓ Fraud detection: flag artificial CTR inflation

---

### 6.17 Admin Dashboard (F-021)

#### F-101: Seller Vetting Workflow

```
Registration → KYC Check → Document Review → Auto/Manual Decision
                              ↓
                    AUTO APPROVED / REJECTED / NEEDS INFO
```

**Dashboard:** applications, bulk actions, search by business name, deadline alerts

#### F-102: Charity Vetting & Fund Transfer

```
Registration → Lookup IRS database → Auto-VERIFIED / Manual Entry
              ↓
         Platform Admin Review
         ├─ VERIFIED → funds routing enabled
         ├─ REJECTED → reason + appeal
         └─ FLAGGED → request more docs
```

#### F-103: Dispute & Refund Resolution

```
Customer initiates return (reason, evidence)
    ↓ (Seller has 24h to respond)
Admin review + decision (BUYER / SELLER / PARTIAL WIN)
    ↓
Execution (refund via Stripe, audit trail)
```

---

### 6.18 Content Moderation (F-022)

#### F-104: Fraud Detection

**Auto-flagging:**

- Banned keywords in title
- Unsafe images (computer vision)
- Impossible impact claims
- Seller with 100+ sales, 0 reviews
- High-risk charity claims

**Manual Review:** Flagged products → APPROVED / REJECTED / NEEDS INFO

---

### 6.19 Internationalization (F-023)

#### F-106: Multi-Language Support

**Languages (Launch):** EN, ES, FR, DE, IT, JA, ZH-CN, KO, PT-BR, AR (planned)

**Frontend:** React-i18next; all strings extractable; RTL support scaffolded

**Backend:** Country → currency mapping, localized errors, regional KYC rules

#### F-107: Multi-Currency Support

| Region | Currency | Method    |
| ------ | -------- | --------- |
| US     | USD      | native    |
| UK     | GBP      | Stripe FX |
| EU     | EUR      | Stripe FX |
| Japan  | JPY      | Stripe FX |

#### F-108: Regional Payment Methods

| Region | Methods                                        |
| ------ | ---------------------------------------------- |
| US/CA  | Card, Apple Pay, Google Pay, PayPal, ACH       |
| EU     | Card, PayPal, iDEAL, Bancontact, EPS           |
| Japan  | Card, PayPal, convenience store, bank transfer |
| China  | Alipay, WeChat Pay, UnionPay                   |

---

### 6.20 Vendor Commission Automation (F-024)

#### F-109: Commission Calculation

```
ORDER: $100
├─ Product: $50 (seller keeps)
├─ Platform Fee (2% standard): $2
├─ Charity Pledge (10% for pledged item): $10
├─ Fulfillment (if FBA): $2.50
└─ Stripe Fee (3% + $0.30): $3
    ↓
SELLER PAYOUT: $100 − $2 − $10 − $2.50 − $3 = $82.50
CHARITY RECEIVES: $10
PLATFORM NET: $2 + $3 = $5
```

**Dynamic Tiers:** Starter (2%) → Professional (1.5%) → Enterprise (1%)

#### F-110: Payout Batch Scheduling

```
Daily Cycle (UTC midnight):
├─ Collect seller payables from previous day
├─ Aggregate by seller (min $10)
├─ Initiate Stripe Connect transfers
└─ Notify sellers, audit log entry
```

**Failure Handling:** Invalid account → hold, notify, retry next day

---

## 7. User Flows

### 7.1 Customer Purchase Flow

```
[Homepage] → [Browse/Search] → [Product Detail] → [Add to Cart]
                                                         │
                                                         ▼
[Order Confirmation] ← [Checkout Complete] ← [Payment*] ← [Cart Review]
       │                                          │
       ▼                                          ▼
[Impact Dashboard             ]    [Shipping Address → Method → Donation Opt-in]
[updated via Kafka order.paid ]
```

_\*Payment tokenized and processed by Donation & Payment Service (Rust Axum)_

### 7.2 Checkout Donation Flow

```
Cart Total: $47.23
      ▼
Step 1:  ☑ Round up → $48.00  ($0.77 donated to Global Education Fund)
      ▼
Step 4:  Order Total: $48.00
         ├─ Items & Shipping: $47.23
         └─ Donation:          $0.77
      ▼
Step 6:  Order #BM-2026-001234 CONFIRMED
         $0.77 donated to Global Education Fund
         [View Impact Dashboard]  [Share on Social]
```

### 7.3 Service Interaction Flow (Order Placement)

```
Customer places order
        │
        ▼
Order Service (Go Fiber)  ──gRPC──▶  Catalog Service (.NET 10)
  [inventory lock check]               [lock confirmed / rejected]
        │
        ▼
Donation & Payment Service (Rust Axum)
  [processes payment + calculates + routes donation atomically]
        │
        ▼  publishes ──▶  Kafka: order.paid
        │
        ├──▶ Catalog Service (.NET 10)   → permanent inventory deduction
        └──▶ User Service (Quarkus)      → update Lifetime Philanthropic badge
```

---

## 8. Data Requirements

### 8.1 Core Data Models

| Entity          | Key Fields                                       | Owner Service                  |
| --------------- | ------------------------------------------------ | ------------------------------ |
| User            | id, email, passwordHash, role, createdAt         | User & Identity (Quarkus)      |
| Seller          | id, userId, businessName, verified, tier         | User & Identity (Quarkus)      |
| CharityOrg      | id, name, registrationId, verified, walletId     | User & Identity (Quarkus)      |
| Product         | id, sellerId, name, price, charityTag, inventory | Catalog & Inventory (.NET 10)  |
| Order           | id, userId, status, total, donationAmount        | Order & Checkout (Go Fiber)    |
| OrderItem       | id, orderId, productId, quantity, price          | Order & Checkout (Go Fiber)    |
| Donation        | id, orderId, amount, campaignId, status          | Donation & Payment (Rust Axum) |
| CharityCampaign | id, charityId, name, goal, startDate, endDate    | Donation & Payment (Rust Axum) |

```
User ──────────▶ Seller ──────▶ Product
 └──────────▶ Order ──────▶ OrderItem ──▶ Product
               └──▶ Donation ──▶ CharityCampaign ──▶ CharityOrg
```

### 8.2 Data Retention Policy

| Data Type           | Retention                        | Reason                   |
| ------------------- | -------------------------------- | ------------------------ |
| User PII            | Until account deletion + 30 days | Legal compliance         |
| Transaction Records | 7 years                          | Tax / legal requirements |
| Donation Records    | Permanent                        | Impact tracking          |
| Session Logs        | 90 days                          | Security audit           |
| Analytics Data      | 2 years                          | Business intelligence    |
| System Logs         | 1 year                           | Troubleshooting          |

### 8.3 Privacy & Compliance

| Regulation  | Key Requirements                                                           |
| ----------- | -------------------------------------------------------------------------- |
| **GDPR**    | Consent management, right to deletion, data portability for EU users       |
| **CCPA**    | California consumer privacy rights and opt-out                             |
| **PCI-DSS** | Payment card data — delegated to Stripe/PayPal; raw card data never stored |
| **SOC 2**   | Security controls for charity fund custody and routing                     |

---

## 9. Non-Functional Requirements

### 9.1 Performance

| Metric                        | Target  | Responsible Service                         |
| ----------------------------- | ------- | ------------------------------------------- |
| API Response (P95)            | < 200ms | All services                                |
| Auth / Token Validation (P95) | < 50ms  | User & Identity (Quarkus + virtual threads) |
| Search Results (P95)          | < 200ms | Catalog Service (Elasticsearch)             |
| Checkout Flow End-to-End      | < 30s   | Order Service (Go Fiber)                    |
| Page Load (P95)               | < 2s    | Frontend + CDN                              |
| Concurrent Users              | 5,000+  | Kubernetes horizontal autoscale             |

### 9.2 Availability

| Environment | Uptime SLA | RTO     | RPO      |
| ----------- | ---------- | ------- | -------- |
| Production  | 99.9%      | 4 hours | 1 hour   |
| Staging     | 99%        | 8 hours | 24 hours |

### 9.3 Scalability

- **Stateless services:** All four core services are stateless; Kubernetes HPA handles autoscaling
- **Quarkus (User Service):** Reactive stack + Java 21 virtual threads for high-concurrency auth with a minimal memory footprint
- **Golang Fiber (Order Service):** Built for burst traffic — scales horizontally in milliseconds during flash sales
- **Rust Axum (Donation Service):** Zero GC pauses, zero data races — deterministic, predictable latency for financial transactions
- **Caching:** Redis for sessions and product data (80% cache hit rate target)

### 9.4 Security

| Area                     | Implementation                                                                  |
| ------------------------ | ------------------------------------------------------------------------------- |
| Authentication           | OAuth 2.0 with PKCE, JWT, TOTP MFA                                              |
| Authorization            | RBAC (4 roles) enforced per service, per endpoint                               |
| Data Protection          | AES-256 at rest, TLS 1.3 in transit                                             |
| Payment Security         | PCI-DSS delegated to Stripe/PayPal tokenization; no raw card data stored        |
| Financial Integrity      | Rust compiler guarantees — no data races or memory unsafety in Donation Service |
| Vulnerability Management | SAST in CI (SonarQube), monthly scans, quarterly pen tests                      |
| Incident Response        | 24-hour notification SLA, 4-hour containment SLA                                |

---

## 10. Success Metrics

### 10.1 Business Metrics

| Metric                      | 6-Month Target | 12-Month Target |
| --------------------------- | -------------- | --------------- |
| GMV                         | $500K          | $5M             |
| Active Sellers              | 50             | 500             |
| Active Buyers               | 2,000          | 20,000          |
| Total Donations Raised      | $25K           | $250K           |
| Average Donation Rate       | 30%            | 40%             |
| Customer Acquisition Cost   | $15            | $10             |
| Lifetime Value per Customer | $150           | $200            |

### 10.2 Technical Metrics

| Metric                | Target                    |
| --------------------- | ------------------------- |
| System Uptime         | 99.9%                     |
| API Availability      | 99.95%                    |
| Mean Response Time    | < 100ms                   |
| Error Rate            | < 0.1%                    |
| Deployment Frequency  | 2–3× per week per service |
| Lead Time for Changes | < 4 hours                 |
| Change Failure Rate   | < 5%                      |

### 10.3 User Engagement Metrics

| Metric                      | Target              |
| --------------------------- | ------------------- |
| Monthly Active Users (MAU)  | 10,000 (by month 6) |
| Daily Active Users (DAU)    | 2,000               |
| Visit → Purchase Conversion | 3%                  |
| Cart Abandonment Rate       | < 60%               |
| Checkout Completion Rate    | > 70%               |
| NPS Score                   | > 40                |

### 10.4 Charity Impact Metrics

| Metric                   | Target              |
| ------------------------ | ------------------- |
| Verified Charities       | 25+                 |
| Active Causes            | 50+                 |
| Donation Transactions    | 10,000+ annually    |
| Average Donation         | $2.50               |
| Repeat Donors            | 40%                 |
| Impact Reports Generated | Monthly per charity |

---

## 11. Glossary

| Term                       | Definition                                                                          |
| -------------------------- | ----------------------------------------------------------------------------------- |
| **BFF**                    | Backend for Frontend — API pattern optimized per client type                        |
| **Charity Tag**            | Product marked as contributing a portion of sale to a vetted charity                |
| **Clean Architecture**     | Layer-based design: Domain → Application → Infrastructure → Presentation            |
| **gRPC**                   | Binary RPC protocol over HTTP/2 using Protobuf; primary internal sync communication |
| **GMV**                    | Gross Merchandise Value — total sales value before deductions                       |
| **Hexagonal Architecture** | Ports & Adapters pattern — isolates domain logic from frameworks and infrastructure |
| **OrderPaidEvent**         | Kafka domain event published by Donation Service after successful payment           |
| **Round-up**               | Rounds checkout total to nearest dollar; difference donated to selected charity     |
| **Saga Pattern**           | Distributed transaction pattern using compensating events over Kafka                |
| **Seller Tier**            | Membership level with tiered platform fees and feature access                       |
| **Virtual Threads**        | Java 21 feature used by Quarkus for lightweight, high-concurrency auth              |
| **TAM / SAM / SOM**        | Total / Serviceable / Obtainable Market                                             |

---

## Document History

| Version | Date           | Changes                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| ------- | -------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1.0     | 2026-03-26     | Initial draft                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
| 2.0     | 2026-04-11     | Polyglot tech stack applied; Platform Admin persona added; service ownership per feature; service interaction flows added; compact format                                                                                                                                                                                                                                                                                                                                                    |
| **3.0** | **2026-04-12** | **All 15 Strategic Enhancements integrated: Real-time WebSocket (F-009), API Gateway Developer Portal (F-005), Feature Flags (F-006), Audit Logging (F-007), Rate Limiting (F-008), Recommendation Engine (F-015), Multi-Vendor FBA (F-016), Loyalty Program (F-017), A/B Testing (F-018), Advanced Analytics (F-019), Advanced Search Ranking (F-020), Admin Dashboard (F-021), Content Moderation (F-022), i18n/l10n (F-023), Vendor Commission Automation (F-024). Total features: 110+** |

---

_End of PRD | Version 3.0 — Production Complete_
