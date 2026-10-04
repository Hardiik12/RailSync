# RailSync — Academic Railway Operations Simulation & Analytics Platform

RailSync is an academic railway operations and decision-support platform designed to demonstrate how advanced Data Structures and Algorithms (DSA) are applied to realistic railway-domain operational problems.

---

## 🛠️ Prerequisites

Ensure your development machine has the following installed:

- **Java Development Kit (JDK):** Version 21
- **Apache Maven:** Version 3.8+
- **Node.js:** Version 18+
- **npm:** Version 9+
- **PostgreSQL Database Server:** Version 14+ (running on port 5432)

---

## 🚀 Quickstart & Setup Guide

### 1. PostgreSQL Database Setup

1. Start your local PostgreSQL server on port `5432`.
2. Create a superuser or standard user `postgres` with password `postgres` (or adjust `backend/src/main/resources/application.yml`).
3. Create the persistent database `railsync_db`:

```bash
# Using psql / createdb CLI:
createdb -U postgres railsync_db
```

Alternatively via `psql`:
```sql
CREATE DATABASE railsync_db;
```

---

### 2. Spring Boot Backend Setup & Startup

The backend is built with **Java 21**, **Spring Boot 3.3.5**, **Flyway**, and **PostgreSQL**.

1. Navigate to the `backend` directory:
   ```bash
   cd backend
   ```

2. Run backend tests to verify environment setup:
   ```bash
   mvn clean test
   ```

3. Start the Spring Boot backend application:
   ```bash
   mvn spring-boot:run
   ```

   - **Backend Service Base URL:** `http://localhost:8080`
   - **Flyway Migrations:** Flyway automatically runs database migrations (`V1__init_schema.sql`, `V2__seed_synthetic_data.sql`, etc.) on application startup.

---

### 3. Vite + React Frontend Setup & Startup

The frontend control center is built with **React 18**, **Vite 6**, **Tailwind CSS**, and **Lucide Icons**.

1. Navigate to the `frontend` directory in a second terminal:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the Vite development server:
   ```bash
   npm run dev
   ```

   - **Frontend Base URL:** `http://localhost:5173`
   - **Vite Proxy:** All API requests matching `/api/*` automatically proxy to `http://localhost:8080`.

---

## 🔍 API & Endpoint Verification

### Test Domain REST Endpoints

1. **Station Directory API:**
   ```bash
   curl -s http://localhost:8080/api/stations
   ```

2. **Railway Documents API:**
   ```bash
   curl -s http://localhost:8080/api/documents
   ```

### Test Representative Algorithm Endpoints (M1–M5)

- **M1 String (KMP):**
  ```bash
  curl -s -X POST http://localhost:8080/api/m1/kmp \
    -H "Content-Type: application/json" \
    -d '{"text": "Vijayawada Junction", "pattern": "Vijayawada", "traceEnabled": true}'
  ```

- **M2 Suffix Array:**
  ```bash
  curl -s -X POST http://localhost:8080/api/m2/suffix-array \
    -H "Content-Type: application/json" \
    -d '{"text": "RAILSYNC", "traceEnabled": true}'
  ```

- **M3 Dynamic Programming (Levenshtein):**
  ```bash
  curl -s -X POST http://localhost:8080/api/m3/levenshtein \
    -H "Content-Type: application/json" \
    -d '{"source": "Vijayawada", "target": "Vijaywada", "traceEnabled": true}'
  ```

- **M4 Network Flow (Dinic Max-Flow):**
  ```bash
  curl -s -X POST http://localhost:8080/api/m4/dinic \
    -H "Content-Type: application/json" \
    -d '{"vertexCount": 2, "source": 0, "sink": 1, "edges": [{"u": 0, "v": 1, "capacity": 10.0, "uName": "S", "vName": "T"}], "traceEnabled": true}'
  ```

- **M5 NP-Completeness (SAT Solver):**
  ```bash
  curl -s -X POST http://localhost:8080/api/m5/sat \
    -H "Content-Type: application/json" \
    -d '{"variables": ["A", "B"], "stringClauses": [["A", "B"], ["!A", "B"]], "traceEnabled": true}'
  ```

---

## 📊 Data Model & Origin Hierarchy

RailSync maintains strict segregation between synthetic baseline entities and public snapshot records:

- **PUBLIC_DATA:** Imported snapshot records from validated public GeoJSON/JSON data sources (e.g., `OPEN_RAIL_2026`). Features coordinates, zone information, and original data source metadata.
- **SYNTHETIC:** Deterministic synthetic station and network topology records generated for academic simulation and benchmarking.

*Note: Railway network edges between public stations remain explicitly tagged as `SYNTHETIC` unless backed by an authoritative track topology dataset.*

---

## 🖥️ Browser Control Center Navigation

Access the following routes in your web browser (`http://localhost:5173`):

- **Platform Dashboard:** `http://localhost:5173/dashboard`
- **Station Directory:** `http://localhost:5173/operations/stations`
- **Railway Network & Flow:** `http://localhost:5173/operations/network`
- **Railway Documents Index:** `http://localhost:5173/operations/documents`
- **Module 1 (String Algorithms):** `http://localhost:5173/dsa/m1` (`/kmp`, `/z-function`, `/rabin-karp`, `/aho-corasick`)
- **Module 2 (Suffix Structures):** `http://localhost:5173/dsa/m2` (`/suffix-array`, `/sa-is`, `/lcp`, `/kasai`, `/suffix-automaton`)
- **Module 3 (Advanced DP):** `http://localhost:5173/dsa/m3` (`/levenshtein`, `/damerau`, `/bitmask`, `/matrix-chain`, `/optimal-bst`)
- **Module 4 (Network Flow):** `http://localhost:5173/dsa/m4` (`/ford-fulkerson`, `/edmonds-karp`, `/dinic`, `/bipartite-matching`, `/konig`, `/max-flow-min-cut`)
- **Module 5 (NP-Completeness & Approximation):** `http://localhost:5173/dsa/m5` (`/sat`, `/3sat`, `/3sat-to-clique`, `/clique-to-independent-set`, `/independent-set-to-vertex-cover`, `/vertex-cover-2approx`)

---

## 🛑 Scope & Project Limitations

1. **Academic Simulation:** RailSync is an academic simulation and algorithmic decision-support platform, not an active railway signaling or dispatch controller.
2. **DSA Scope:** Modules M1 through M5 contain 100% manual Java implementations. Module M6 (Parallel & Randomized) is explicitly out of scope and absent.
3. **No External Live API Dependency:** System operates deterministically offline using PostgreSQL synthetic and public snapshot records.

---

## 📐 Project Documentation

Authoritative documentation is located in the `docs/` folder:

- [`01-project-specification.md`](docs/01-project-specification.md) — DSA & Domain Scope
- [`02-system-architecture.md`](docs/02-system-architecture.md) — Monolith Architecture & Modules
- [`03-database-design.md`](docs/03-database-design.md) — Relational Schema & Entities
- [`04-api-contract.md`](docs/04-api-contract.md) — REST API Endpoints & Envelopes
- [`05-algorithm-contracts.md`](docs/05-algorithm-contracts.md) — Algorithm Signatures & Trace Specification
- [`06-ui-architecture.md`](docs/06-ui-architecture.md) — React Frontend Control Center Design
- [`07-testing-strategy.md`](docs/07-testing-strategy.md) — Backend & Frontend Test Suite Guidelines
- [`08-development-roadmap.md`](docs/08-development-roadmap.md) — Development Phases & Milestone Progress

