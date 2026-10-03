# RailSync – Intelligent Railway Operations Platform

**Document:** 01 – Project Specification  
**Version:** 1.0  
**Project Type:** Academic Railway Operations Simulation & Analytics Platform  
**Primary Focus:** Data Structures & Algorithms + Full-Stack Engineering  
**Backend:** Java + Spring Boot  
**Frontend:** React + Vite + Tailwind CSS + Framer Motion  
**Database:** PostgreSQL  

---

# 1. Project Overview

RailSync is a full-stack railway operations simulation and analytics platform designed to demonstrate how advanced **Data Structures and Algorithms (DSA)** can be applied to realistic railway-management problems.

The system models:

- Trains
- Stations
- Platforms
- Routes
- Trips
- Passengers
- Tickets
- Service alerts
- Maintenance records
- Railway documents
- Operational events
- Railway network capacity
- Scheduling constraints
- Continuous event streams

The primary purpose of RailSync is **not** to create a generic railway CRUD application.

The core objective is to connect each required DSA algorithm to a concrete railway-domain problem and provide:

1. A working algorithm implementation.
2. A railway-specific use case.
3. A backend API.
4. Interactive frontend visualization.
5. Execution tracing.
6. Complexity information.
7. Runtime benchmarking.
8. Automated tests.
9. Cross-validation against related algorithms where applicable.

The platform should therefore function as both:

> **A railway operations simulation platform**

and

> **An interactive DSA laboratory demonstrating real algorithmic applications.**

---

# 2. Problem Statement

Railway systems generate and process large amounts of structured and unstructured information.

Examples include:

- train schedules
- station information
- service alerts
- passenger records
- ticket codes
- railway documents
- platform assignments
- route networks
- passenger flows
- operational events
- maintenance information
- scheduling constraints

Different operational problems require different algorithmic techniques.

RailSync addresses these problems by implementing the prescribed DSA modules and connecting them to railway scenarios.

The system must demonstrate how:

- string algorithms accelerate railway information searching,
- suffix structures support document indexing,
- dynamic programming solves bounded optimization problems,
- network flow analyzes railway capacity,
- NP-complete models represent complex scheduling constraints,
- randomized and parallel algorithms process large railway datasets and streams.

---

# 3. Primary Objectives

## 3.1 DSA Objectives

Implement the complete prescribed algorithm set:

### M1 – CO1: String Algorithms

- KMP
- Z-Function
- Rabin-Karp
- Aho-Corasick

### M2 – CO2: Suffix Structures

- Suffix Array
- SA-IS
- LCP
- Kasai
- Suffix Automaton

### M3 – CO3: Advanced Dynamic Programming

- Levenshtein Distance
- Damerau-Levenshtein Distance
- Bitmask DP
- Matrix-Chain Multiplication
- Optimal BST

### M4 – CO4: Network Flow

- Ford-Fulkerson
- Edmonds-Karp
- Dinic
- Bipartite Matching
- König's Theorem
- Max-Flow Min-Cut

### M5 – CO5: NP-Completeness & Approximation

- SAT
- 3-SAT
- 3-SAT → CLIQUE
- CLIQUE → Independent Set
- Independent Set → Vertex Cover
- Vertex Cover 2-Approximation

### M6 – CO6: Randomized & Parallel Algorithms

- Randomized QuickSort
- Reservoir Sampling
- Miller-Rabin
- Blelloch Scan
- Parallel Reduce
- Brent's Theorem

---

# 4. Railway-Domain Mapping

Every algorithm must have a specific railway application.

## M1 – String Algorithms

| Algorithm | Railway Application |
|---|---|
| KMP | Search train numbers, station names and service alerts |
| Z-Function | Detect repeated operational text patterns |
| Rabin-Karp | Search ticket and booking codes |
| Aho-Corasick | Detect multiple alert keywords in service messages |

---

## M2 – Suffix Structures

| Algorithm | Railway Application |
|---|---|
| Suffix Array | Index railway documents and service information |
| SA-IS | Large-scale railway text indexing |
| LCP | Identify repeated text between railway documents |
| Kasai | Construct LCP information efficiently |
| Suffix Automaton | Substring-based railway document search |

---

## M3 – Dynamic Programming

| Algorithm | Railway Application |
|---|---|
| Levenshtein | Correct station/passenger text input |
| Damerau-Levenshtein | Correct transposed characters in station/passenger input |
| Bitmask DP | Optimize small bounded route/service combinations |
| Matrix-Chain Multiplication | Optimize railway-data operation ordering |
| Optimal BST | Optimize access to frequently requested station/service records |

---

## M4 – Network Flow

| Algorithm | Railway Application |
|---|---|
| Ford-Fulkerson | Passenger flow through railway connections |
| Edmonds-Karp | Capacity analysis using BFS-based augmenting paths |
| Dinic | Large railway passenger-flow networks |
| Bipartite Matching | Assign trains/services to eligible platforms |
| König's Theorem | Analyze platform-service conflicts |
| Max-Flow Min-Cut | Identify critical railway network bottlenecks |

---

## M5 – NP-Completeness

| Algorithm | Railway Application |
|---|---|
| SAT | Railway scheduling constraints |
| 3-SAT | Boolean representation of scheduling constraints |
| 3-SAT → CLIQUE | Transform scheduling constraints into graph form |
| CLIQUE → Independent Set | Service-conflict analysis |
| Independent Set → Vertex Cover | Identify conflicting railway resources |
| Vertex Cover 2-Approximation | Approximate critical railway connection coverage |

These algorithms are used as an **academic constraint-analysis and complexity demonstration**, not as a claim that RailSync solves national-scale railway scheduling optimally.

---

## M6 – Randomized & Parallel Algorithms

| Algorithm | Railway Application |
|---|---|
| Randomized QuickSort | Rank train/service records |
| Reservoir Sampling | Sample continuous railway-event streams |
| Miller-Rabin | Large-number primality demonstration |
| Blelloch Scan | Calculate cumulative passenger statistics |
| Parallel Reduce | Aggregate railway data |
| Brent's Theorem | Analyze theoretical parallel processing performance |

---

# 5. Core System Modules

RailSync consists of the following major modules.

## 5.1 Railway Operations Module

Responsible for:

- stations
- platforms
- trains
- routes
- trips
- passengers
- tickets
- service alerts
- maintenance records

---

## 5.2 Railway Event Module

Responsible for continuous operational events such as:

- train arrival
- train departure
- delay
- platform change
- service alert
- passenger movement
- maintenance event

Events are persisted and can also be streamed to the frontend.

---

## 5.3 Document Module

Responsible for railway documents such as:

- service information
- station information
- operational notices
- maintenance notices
- railway policies
- service descriptions

These documents provide the input for M2 suffix-based algorithms.

---

## 5.4 Algorithm Engine

Contains all manually implemented DSA algorithms.

The algorithm engine must remain independent from railway-specific business logic.

Example:

```text
KMPAlgorithm
      ↓
RailwaySearchService
      ↓
REST API
      ↓
React KMP Visualization
```

The algorithm itself must not contain railway-specific database queries.

---

# 6. Functional Requirements

## FR-01 – Railway Data Management

The system shall allow retrieval and management of:

- stations
- platforms
- trains
- routes
- trips
- passengers
- tickets
- alerts
- maintenance records
- railway events
- railway documents

---

## FR-02 – Algorithm Execution

The system shall allow users to execute every prescribed algorithm through the frontend.

Each execution should provide:

- input
- execution result
- operation count
- execution time
- theoretical complexity
- optional trace
- visualization

---

## FR-03 – Algorithm Visualization

The frontend shall visualize algorithm execution wherever practical.

Examples:

- KMP → pattern matching movement
- Aho-Corasick → trie/failure links
- Suffix Array → suffix ordering
- DP → matrix/table
- Network Flow → graph and residual capacities
- SAT reductions → clause/graph transformation
- Reservoir Sampling → stream/sample state
- Blelloch Scan → upsweep/downsweep tree

---

## FR-04 – Benchmarking

Users shall be able to execute algorithms with benchmarking enabled.

The system should report measured:

```text
Execution Time
Operation Count
Input Size
```

Theoretical complexity must be displayed separately.

The system must never fabricate benchmark values.

---

## FR-05 – Execution Trace

Algorithms should support optional trace generation.

Example:

```json
{
  "step": 12,
  "action": "COMPARE",
  "state": {
    "textIndex": 8,
    "patternIndex": 3
  },
  "description": "Comparing pattern[3] with text[8]"
}
```

Trace generation must support configurable limits to prevent extremely large responses.

---

## FR-06 – Railway Search

The system shall provide algorithm-powered railway search.

Examples:

```text
Train Number Search
Station Search
Service Alert Search
Ticket Code Search
```

The appropriate DSA algorithm should be explicitly shown in the interface.

---

## FR-07 – Document Analysis

Users shall be able to select railway documents and execute suffix-based analysis.

The system shall support:

- suffix-array construction
- SA-IS construction
- LCP analysis
- Kasai LCP generation
- substring search using suffix automaton

---

## FR-08 – Passenger Flow Analysis

The system shall model railway network edges with capacities.

Users shall be able to execute:

- Ford-Fulkerson
- Edmonds-Karp
- Dinic
- Max-Flow Min-Cut

The resulting flow and bottlenecks must be visualized.

---

## FR-09 – Platform Assignment

The system shall model train/service-to-platform eligibility as a bipartite graph.

Example:

```text
Train A ───── Platform 1
       └───── Platform 3

Train B ───── Platform 2

Train C ───── Platform 1
       └───── Platform 4
```

The matching algorithm determines a valid maximum assignment for the supplied graph.

---

## FR-10 – Scheduling Constraint Analysis

Users shall be able to define bounded scheduling constraints.

The system shall demonstrate:

```text
SAT
 ↓
3-SAT
 ↓
CLIQUE
 ↓
INDEPENDENT SET
 ↓
VERTEX COVER
```

The transformation steps must be inspectable.

---

## FR-11 – Streaming Analytics

The system shall simulate continuous railway events.

Reservoir Sampling should be able to sample from an event stream without storing the complete stream as the algorithm's sampling mechanism.

Blelloch Scan and Parallel Reduce should process suitable numerical datasets.

---

# 7. Non-Functional Requirements

## NFR-01 – Correctness

Algorithms must produce correct results for:

- normal inputs
- empty inputs
- single-element inputs
- duplicate data
- boundary cases
- invalid inputs where applicable

---

## NFR-02 – Reproducibility

Randomized algorithms must support deterministic seeds.

Example:

```text
seed = 42
```

Running the same randomized algorithm with the same seed should produce reproducible behavior.

---

## NFR-03 – Performance

The system should handle the defined synthetic dataset sizes without unnecessary performance degradation.

Large datasets should be:

- paginated
- aggregated
- sampled
- virtualized

where appropriate.

---

## NFR-04 – Maintainability

The project must use modular architecture.

Algorithm implementations must not be mixed with:

- React components
- database repositories
- controllers
- railway domain entities

---

## NFR-05 – Testability

Each algorithm must have independent unit tests.

Cross-validation must be implemented where multiple algorithms solve the same mathematical problem.

---

## NFR-06 – Observability

Algorithm execution should record:

- algorithm name
- input size
- execution time
- operation count
- timestamp
- trace status

---

# 8. Data Requirements

RailSync shall use **synthetic deterministic railway data**.

No real passenger personal information should be required.

Recommended initial dataset:

| Entity | Target |
|---|---:|
| Stations | 30–50 |
| Platforms | 100+ |
| Trains | 50+ |
| Routes | 50+ |
| Route Stops | 300+ |
| Trips | 100+ |
| Stop Times | 500+ |
| Passengers | 5,000+ |
| Tickets | 10,000+ |
| Service Alerts | 500+ |
| Maintenance Records | 100+ |
| Railway Events | 20,000+ |
| Documents | 100+ |
| Network Edges | 100+ |

The exact generated values may change during implementation, but the seed must remain deterministic and internally consistent.

---

# 9. System Boundaries

RailSync is an **academic railway operations simulation and analytics platform**.

It is not intended to:

- control real trains
- control railway signaling
- dispatch real trains
- access confidential railway systems
- process real passenger PII
- provide safety-critical railway decisions
- replace production railway infrastructure

All operational scenarios are simulated.

---

# 10. Technology Requirements

## Frontend

```text
React
Vite
Tailwind CSS
Framer Motion
React Router
```

## Backend

```text
Java
Spring Boot
REST API
WebSocket
JUnit 5
```

## Database

```text
PostgreSQL
Flyway
```

## Testing

```text
JUnit 5
Spring Boot Test
Mockito
MockMvc
Testcontainers
Vitest
React Testing Library
Playwright
```

---

# 11. Architectural Principle

The most important architectural rule is:

> **Algorithms must remain independent from railway business logic.**

Correct:

```text
KMPAlgorithm
      ↓
RailwaySearchService
      ↓
KMPController
      ↓
React
```

Incorrect:

```text
KMPAlgorithm
      ↓
PostgreSQL
      ↓
RailwayTrainEntity
```

The algorithm engine receives algorithmic input structures.

Railway services transform domain data into those structures.

---

# 12. Algorithm Implementation Rules

All required algorithms must be implemented manually.

Do not replace them with:

- Java library sorting
- database full-text search
- graph libraries
- external algorithm packages
- regex for required string algorithms
- PostgreSQL search as the core implementation
- third-party optimization libraries

Libraries may be used for infrastructure, testing, persistence and UI.

They must not replace the required DSA implementation.

---

# 13. Input Safety Requirements

Algorithms with potentially exponential or very large complexity must have explicit limits.

Examples:

### Bitmask DP

```text
n <= configured maximum
```

### SAT / 3-SAT

Use bounded educational instances.

### CLIQUE

Use small graph sizes for exhaustive demonstrations.

### Vertex Cover

Use bounded instances for exact comparison tests.

The application must reject inputs that exceed configured demonstration limits.

---

# 14. Core User Roles

RailSync does not require complex authentication in the initial implementation.

The application can operate as an academic demonstration platform.

Primary conceptual users are:

### Operations Analyst

Uses:

- trains
- stations
- routes
- platforms
- alerts
- passenger flow

### DSA Student

Uses:

- algorithm visualizations
- traces
- benchmarks
- complexity information
- reductions

### Project Evaluator

Uses:

- dashboard
- module demonstrations
- algorithm comparisons
- railway use cases
- execution metrics

---

# 15. Success Criteria

RailSync is considered functionally successful when:

### Backend

- Spring Boot application starts successfully.
- PostgreSQL connects successfully.
- Database migrations execute successfully.
- Synthetic seed data loads successfully.
- REST APIs respond correctly.
- WebSocket events function correctly.

### Algorithms

All prescribed algorithms are implemented and tested.

### Frontend

Users can:

- navigate the railway operations dashboard,
- view railway data,
- execute algorithms,
- inspect results,
- visualize execution,
- inspect traces,
- view complexity,
- view measured benchmarks.

### Integration

At least one complete vertical slice must demonstrate:

```text
Database
   ↓
Spring Repository
   ↓
Railway Service
   ↓
Algorithm Service
   ↓
REST API
   ↓
React UI
   ↓
Visualization
```

---

# 16. Demonstration Flow

The final project demonstration should follow a coherent railway workflow.

### 1. Operations Dashboard

Show:

- active trains
- stations
- platforms
- alerts
- events
- passenger statistics

### 2. Railway Search

Demonstrate:

**KMP**

Search a station/train/service string.

### 3. Alert Detection

Demonstrate:

**Aho-Corasick**

Detect multiple alert keywords in service messages.

### 4. Document Analysis

Demonstrate:

**Suffix Array / SA-IS / LCP / Kasai / Suffix Automaton**

Analyze railway documents.

### 5. Data Correction

Demonstrate:

**Levenshtein / Damerau-Levenshtein**

Correct an incorrectly entered station name.

### 6. Platform Assignment

Demonstrate:

**Bipartite Matching**

Assign eligible services to platforms.

### 7. Passenger Flow

Demonstrate:

**Dinic + Max-Flow Min-Cut**

Analyze railway network capacity and identify a bottleneck cut.

### 8. Scheduling Constraints

Demonstrate:

**SAT → 3-SAT → CLIQUE → Independent Set → Vertex Cover**

Show the mathematical transformation pipeline.

### 9. Live Event Processing

Demonstrate:

**Reservoir Sampling**

Process a simulated railway event stream.

### 10. Analytics

Demonstrate:

- measured execution times
- operation counts
- theoretical complexity
- algorithm traces

---

# 17. Project Scope Restrictions

The following features are explicitly outside the initial project scope:

- Blockchain
- Cryptocurrency
- Generic AI chatbot
- Facial recognition
- IoT integration
- Payment gateway
- Real passenger PII
- Real railway control
- Kubernetes
- Microservices
- Complex authentication
- Unrelated recommendation systems
- Unrelated DSA algorithms

New features should not be added merely because they appear technically interesting.

Any scope expansion must be explicitly approved.

---

# 18. Development Philosophy

RailSync should be developed as a **DSA-first engineering project**.

The implementation priority is:

```text
Correct Algorithm
       ↓
Algorithm Tests
       ↓
Railway Adapter
       ↓
Backend API
       ↓
Frontend Visualization
       ↓
Benchmarking
       ↓
Integration
```

Not:

```text
Beautiful UI
       ↓
Random features
       ↓
Algorithms added later
```

The algorithmic correctness and railway-domain mapping are the project's primary technical requirements.

---

# 19. Definition of Done

An algorithm is considered complete only when all of the following exist:

- [ ] Manual implementation
- [ ] Input/output model
- [ ] Unit tests
- [ ] Edge-case tests
- [ ] Complexity documentation
- [ ] Operation counting where appropriate
- [ ] Optional execution trace
- [ ] Railway use-case adapter
- [ ] Service integration
- [ ] REST endpoint
- [ ] Frontend page
- [ ] Visualization where meaningful
- [ ] Benchmark support
- [ ] Documentation
- [ ] Cross-validation where applicable

---

# 20. Final Project Definition

**RailSync is an academic full-stack railway operations simulation and analytics platform that demonstrates the practical application of advanced Data Structures and Algorithms across railway search, document indexing, bounded optimization, network-flow analysis, scheduling constraints, randomized processing, and parallel computation.**

The project must remain faithful to the prescribed M1–M6 DSA mapping.

The railway domain provides the operational context.

The algorithms provide the computational core.

The full-stack architecture provides the engineering implementation.

The frontend provides visualization and interaction.

The database provides realistic structured data.

Together, these components form the RailSync platform.