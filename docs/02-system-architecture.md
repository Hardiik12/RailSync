# RailSync — System Architecture

## 1. Architecture Overview

RailSync is a modular monolithic full-stack application.

The initial architecture consists of:

```text
React + Vite
      │
      │ REST / WebSocket
      ↓
Spring Boot
      │
      ├── Railway Domain Services
      │
      ├── Algorithm Engine
      │
      ├── Analytics Services
      │
      └── Event Engine
      │
      ↓
PostgreSQL
```

The system is intentionally NOT split into microservices.

The objective is to keep the DSA implementation, railway-domain logic, database, API, and frontend easy to understand and demonstrate.

---

# 2. High-Level System

```text
                         ┌─────────────────────┐
                         │     RAILSYNC UI     │
                         │                     │
                         │ React + Vite        │
                         │ Tailwind CSS        │
                         │ Framer Motion       │
                         └──────────┬──────────┘
                                    │
                         REST / WebSocket
                                    │
                                    ↓
                  ┌────────────────────────────────┐
                  │       SPRING BOOT API          │
                  │                                │
                  │ Controllers                    │
                  │ DTOs                           │
                  │ Services                       │
                  └───────────────┬────────────────┘
                                  │
               ┌──────────────────┼───────────────────┐
               │                  │                   │
               ↓                  ↓                   ↓
       Railway Domain       Algorithm Engine      Event Engine
               │                  │                   │
               │        ┌─────────┼─────────┐         │
               │        │         │         │         │
               │       M1        M2        M3        │
               │       M4        M5        M6        │
               │                  │                   │
               └──────────────────┼───────────────────┘
                                  ↓
                         Repository Layer
                                  │
                                  ↓
                            PostgreSQL
```

---

# 3. Architectural Layers

RailSync has six logical layers.

## Layer 1 — Presentation

Technology:

- React
- Vite
- Tailwind CSS
- Framer Motion

Responsibilities:

- display railway information
- accept user input
- execute algorithms through APIs
- visualize algorithm traces
- visualize railway graphs
- display analytics
- display live events

The frontend must NOT contain the authoritative implementation of M1–M6 algorithms.

---

# 4. API Layer

Technology:

Spring Boot REST controllers.

Responsibilities:

- HTTP request handling
- validation
- DTO conversion
- response formatting
- authentication/authorization if added later
- error handling

Controllers must remain thin.

Controllers must not contain algorithm implementations.

Example:

```text
KMPController
      ↓
KMPService
      ↓
KMPAlgorithm
```

---

# 5. Application Service Layer

Responsibilities:

- coordinate domain operations
- prepare algorithm input
- invoke algorithms
- transform algorithm output into application results
- coordinate repositories
- coordinate event processing

Example:

```text
RailwaySearchService
        ↓
TrainRepository
StationRepository
AlertRepository
        ↓
KMPAlgorithm
```

The application service connects the railway domain to the algorithm layer.

---

# 6. Algorithm Layer

This layer contains the actual DSA implementations.

Structure:

```text
algorithm/
├── m1/
│   ├── kmp/
│   ├── zfunction/
│   ├── rabinkarp/
│   └── ahoCorasick/
│
├── m2/
│   ├── suffixArray/
│   ├── sais/
│   ├── lcp/
│   ├── kasai/
│   └── suffixAutomaton/
│
├── m3/
│   ├── levenshtein/
│   ├── damerau/
│   ├── bitmask/
│   ├── matrixChain/
│   └── optimalBST/
│
├── m4/
│   ├── fordFulkerson/
│   ├── edmondsKarp/
│   ├── dinic/
│   ├── matching/
│   └── minCut/
│
├── m5/
│   ├── sat/
│   ├── threeSat/
│   ├── clique/
│   ├── independentSet/
│   └── vertexCover/
│
└── m6/
    ├── randomizedQuickSort/
    ├── reservoirSampling/
    ├── millerRabin/
    ├── blellochScan/
    ├── parallelReduce/
    └── brent/
```

---

# 7. Algorithm Layer Rules

Algorithms must be domain-independent wherever practical.

Bad:

```java
KMPAlgorithm.searchTrainNumbers(...)
```

Good:

```java
KMPAlgorithm.search(String text, String pattern)
```

Railway-specific logic belongs above the algorithm layer.

Example:

```text
RailwaySearchService
        ↓
extract train numbers
        ↓
KMPAlgorithm.search(...)
        ↓
SearchResult
```

This allows the same algorithm to be tested independently.

---

# 8. Algorithm Result Architecture

Each algorithm should return a structured result.

Conceptual structure:

```text
AlgorithmResult<T>

algorithmName
result
inputSize
executionTime
operationCount
timeComplexity
spaceComplexity
trace
metadata
```

Not every algorithm requires every field.

For example:

```text
KMPResult
- matches
- comparisons
- executionTime
- prefixTable
- trace
```

Graph algorithm:

```text
MaxFlowResult
- maxFlow
- residualGraph
- augmentingPaths
- phases
- minCut
- executionTime
```

DP:

```text
DPResult
- optimum
- dpTable
- decisions
- executionTime
```

---

# 9. Railway Domain Layer

Domain packages:

```text
domain/
├── station/
├── train/
├── route/
├── trip/
├── stopTime/
├── platform/
├── passenger/
├── ticket/
├── alert/
├── maintenance/
├── event/
└── document/
```

Each domain should contain appropriate:

- Entity
- Repository
- DTO
- Service
- Controller

Do not create unnecessary classes.

---

# 10. Core Domain Relationships

## Station

A station can have:

- many platforms
- many route stops
- many railway events
- many alerts
- many trains passing through

```text
Station 1 ───── * Platform

Station 1 ───── * StopTime

Station 1 ───── * RailwayEvent

Station 1 ───── * ServiceAlert
```

---

# 11. Train

A train can have:

- one source station
- one destination station
- many route stops
- many tickets
- many maintenance records
- many events
- many alerts

```text
Train 1 ───── * StopTime

Train 1 ───── * Ticket

Train 1 ───── * MaintenanceRecord

Train 1 ───── * RailwayEvent

Train 1 ───── * ServiceAlert
```

---

# 12. Route / Trip / Stop Time

Use separate concepts.

A route represents the ordered station sequence associated with a service.

A trip represents a scheduled operational instance of a train service.

A stop time represents the scheduled arrival/departure information at a station.

Conceptually:

```text
Train Service
      │
      ↓
Trip
      │
      ↓
Stop Times
      │
      ↓
Stations
```

Do not collapse these into one table unless there is a strong technical reason.

---

# 13. Platform

Platform belongs to a station.

```text
Station
   │
   └── Platform
```

Platform assignment belongs to an operational trip/stop context rather than being treated as a permanent train property.

This is important for M4 bipartite matching.

---

# 14. Passenger and Ticket

```text
Passenger
    │
    └── Ticket
          │
          ├── Train
          ├── Source Station
          └── Destination Station
```

A passenger can have multiple tickets.

A ticket belongs to one passenger.

A ticket references a particular train/service.

---

# 15. Railway Events

Railway events represent continuous operational activity.

Supported types:

```text
TRAIN_ARRIVED
TRAIN_DEPARTED
TRAIN_DELAYED
PASSENGER_ENTRY
PASSENGER_EXIT
PLATFORM_CHANGED
SERVICE_CANCELLED
SERVICE_RESCHEDULED
MAINTENANCE_STARTED
MAINTENANCE_COMPLETED
ALERT_CREATED
```

Event structure:

```text
RailwayEvent
├── id
├── eventType
├── timestamp
├── trainId
├── stationId
├── passengerCount
└── metadata
```

Not every event requires every field.

---

# 16. Event Processing Architecture

```text
Event Generator
      ↓
Event Service
      ↓
Persist Event
      ↓
Analytics
      ↓
Algorithm Consumers
      ↓
WebSocket
      ↓
React Live Dashboard
```

Example:

```text
PASSENGER_ENTRY
      ↓
RailwayEvent
      ↓
PostgreSQL
      ↓
Passenger Analytics
      ↓
Blelloch/Reduce demonstration
      ↓
Dashboard
```

---

# 17. Document Architecture

Railway documents are stored separately from structured railway entities.

```text
document/
├── RailwayDocument
├── DocumentRepository
├── DocumentService
└── DocumentSearchService
```

Documents may contain:

- title
- category
- content
- metadata
- timestamps

M2 consumes document content.

---

# 18. M1 Architecture

```text
                     M1
                      │
         ┌────────────┼─────────────┐
         │            │             │
        KMP           Z       Rabin-Karp
         │            │             │
         │            │             │
    railway text   events       tickets
         │            │             │
         └────────────┼─────────────┘
                      │
                Aho-Corasick
                      │
                 alert keywords
```

Services:

```text
M1SearchService
M1PatternService
M1AlertService
```

Algorithms remain separate.

---

# 19. M2 Architecture

```text
RailwayDocument
      ↓
Text Preprocessor
      ↓
Index Builder
      │
      ├── Suffix Array
      ├── SA-IS
      ├── LCP/Kasai
      └── Suffix Automaton
      ↓
Document Search
      ↓
Similarity / Repetition Analysis
```

The index must not be rebuilt unnecessarily for every query.

---

# 20. M3 Architecture

```text
Railway Data
     │
     ├── correction
     │      ↓
     │  Levenshtein
     │  Damerau
     │
     ├── small optimization
     │      ↓
     │  Bitmask DP
     │
     ├── operation optimization
     │      ↓
     │  Matrix Chain
     │
     └── access optimization
            ↓
        Optimal BST
```

---

# 21. M4 Architecture

The railway network is represented as a graph.

```text
Station
   ↓
Graph Node

Railway Connection
   ↓
Graph Edge

Passenger Capacity
   ↓
Edge Capacity
```

Flow engine:

```text
FlowNetwork
     │
     ├── Ford-Fulkerson
     ├── Edmonds-Karp
     └── Dinic
```

Matching engine:

```text
Train nodes
     ↕
Feasible assignment edges
     ↕
Platform nodes
```

---

# 22. M5 Architecture

```text
Railway Constraints
        ↓
Constraint Model
        ↓
Boolean Representation
        ↓
SAT / 3-SAT
        ↓
Reduction Engine
        ├── CLIQUE
        ├── Independent Set
        └── Vertex Cover
        ↓
Approximation Analysis
```

The reduction engine must preserve the semantics of the transformation.

Every transformation should have tests.

---

# 23. M6 Architecture

```text
Railway Data / Event Stream
            │
      ┌─────┴────────┐
      │              │
 Randomized       Parallel
      │              │
 QuickSort      Scan / Reduce
 Reservoir      Brent Analysis
 Miller-Rabin
```

---

# 24. Frontend Architecture

Recommended:

```text
frontend/
├── src/
│   ├── app/
│   ├── layouts/
│   ├── pages/
│   ├── components/
│   ├── features/
│   ├── algorithms/
│   ├── charts/
│   ├── graphs/
│   ├── hooks/
│   ├── services/
│   ├── api/
│   ├── types/
│   └── utils/
```

---

# 25. Frontend Feature Structure

Use feature-oriented organization.

Example:

```text
features/
├── trains/
├── stations/
├── tickets/
├── alerts/
├── documents/
├── liveEvents/
└── algorithms/
```

Algorithm UI:

```text
algorithms/
├── m1/
├── m2/
├── m3/
├── m4/
├── m5/
└── m6/
```

---

# 26. M1 Frontend

```text
M1Page
 ├── AlgorithmSelector
 ├── DatasetSelector
 ├── PatternInput
 ├── SearchControls
 ├── SearchResult
 ├── MatchVisualizer
 ├── AlgorithmTrace
 └── ComplexityPanel
```

---

# 27. M2 Frontend

```text
M2Page
 ├── DocumentSelector
 ├── IndexBuilder
 ├── SearchInput
 ├── SuffixArrayViewer
 ├── LCPViewer
 ├── SimilarityResult
 └── PerformancePanel
```

---

# 28. M3 Frontend

```text
M3Page
 ├── AlgorithmSelector
 ├── InputEditor
 ├── DPTable
 ├── EditOperationViewer
 ├── OptimizationResult
 └── ComplexityPanel
```

---

# 29. M4 Frontend

M4 requires a graph visualization component.

```text
M4Page
 ├── GraphBuilder
 ├── SourceSelector
 ├── DestinationSelector
 ├── AlgorithmSelector
 ├── NetworkGraph
 ├── FlowAnimation
 ├── ResidualGraph
 ├── MinCutViewer
 └── FlowMetrics
```

The graph should support:

- nodes
- directed edges
- capacity labels
- flow labels
- highlighting
- animation

---

# 30. M5 Frontend

```text
M5Page
 ├── ConstraintEditor
 ├── CNFViewer
 ├── SATResult
 ├── ReductionVisualizer
 ├── GraphVisualizer
 ├── ApproximationResult
 └── ComplexityPanel
```

---

# 31. M6 Frontend

```text
M6Page
 ├── AlgorithmSelector
 ├── StreamSimulator
 ├── DataGenerator
 ├── SamplingVisualizer
 ├── ScanVisualizer
 ├── ReductionTree
 ├── BenchmarkPanel
 └── BrentAnalysis
```

---

# 32. API Architecture

API prefix:

```text
/api
```

Domain APIs:

```text
/api/trains
/api/stations
/api/platforms
/api/routes
/api/trips
/api/passengers
/api/tickets
/api/alerts
/api/maintenance
/api/events
/api/documents
```

DSA APIs:

```text
/api/m1/*
/api/m2/*
/api/m3/*
/api/m4/*
/api/m5/*
/api/m6/*
```

---

# 33. API Separation Rule

Domain API:

```text
GET /api/stations
```

Algorithm API:

```text
POST /api/m1/kmp
```

Do not combine them into:

```text
/api/stations/search?algorithm=kmp
```

for the primary algorithm laboratory.

The algorithm lab should expose the algorithm explicitly.

Railway services may internally choose an algorithm where appropriate.

---

# 34. API Request/Response Flow

Example M1:

```text
React
 ↓
POST /api/m1/kmp
 ↓
KMPController
 ↓
KMPService
 ↓
KMPAlgorithm
 ↓
KMPResult
 ↓
KMPService
 ↓
KMPResponseDTO
 ↓
React
```

---

# 35. Database Architecture

Use PostgreSQL.

Recommended migration tool:

- Flyway

Database structure:

```text
stations
platforms
trains
routes
trips
stop_times
passengers
tickets
service_alerts
maintenance_records
railway_events
railway_documents
```

Additional algorithm-specific persisted data should only be added when necessary.

Do not store every algorithm execution by default.

---

# 36. Algorithm Execution Persistence

Algorithm execution history may be introduced later.

If enabled:

```text
algorithm_execution
├── id
├── module
├── algorithm
├── input_size
├── execution_time
├── operation_count
├── created_at
└── metadata
```

Large trace payloads should not automatically be stored in PostgreSQL.

Return traces through the API when appropriate.

---

# 37. WebSocket Architecture

WebSocket endpoint:

```text
/ws/events
```

Possible channels:

```text
railway-events
alerts
train-updates
passenger-flow
```

The initial implementation may use a single event stream with event-type filtering.

---

# 38. Event Flow

```text
Event Generator
      ↓
EventService
      ↓
EventRepository
      ↓
EventPublisher
      ↓
WebSocket
      ↓
React Event Store
      ↓
Dashboard
```

---

# 39. Error Architecture

Backend exception hierarchy should distinguish:

```text
ValidationException
AlgorithmInputException
AlgorithmExecutionException
ResourceNotFoundException
UnsupportedOperationException
```

Global exception handler:

```text
@RestControllerAdvice
```

All API errors should use a consistent structure.

---

# 40. Validation

Validate:

- empty patterns
- invalid station IDs
- invalid train IDs
- invalid graph edges
- negative capacities
- invalid source/destination
- invalid DP inputs
- malformed CNF
- invalid sampling size
- invalid processor count
- invalid document input

---

# 41. Algorithm Input Limits

Because some algorithms can become computationally expensive, the API should enforce reasonable limits.

Examples:

- Bitmask DP: bounded number of elements
- exact NP-complete demonstrations: small instances
- exhaustive graph search: small graphs
- suffix structures: configurable document size
- DP matrices: bounded dimensions

The UI should explain the limit rather than silently failing.

---

# 42. Performance Architecture

Benchmarking must be separated from ordinary business operations.

Use:

```text
Algorithm
+
BenchmarkRunner
```

rather than embedding benchmarking code throughout services.

Benchmark result:

```text
algorithm
inputSize
executionTime
operationCount
memoryEstimate
```

Where accurate memory measurement is difficult, label it as an estimate or omit it.

---

# 43. Logging

Log:

- request identifiers
- algorithm name
- module
- execution status
- errors
- timing where useful

Do NOT log:

- full passenger records
- sensitive contact information
- full ticket details unnecessarily

---

# 44. Seed Data Architecture

Seed data should be deterministic.

Use:

```text
database/
├── migrations/
└── seed/
    ├── stations
    ├── trains
    ├── routes
    ├── platforms
    ├── passengers
    ├── tickets
    ├── alerts
    ├── maintenance
    ├── documents
    └── events
```

A fixed seed should produce reproducible algorithm demonstrations.

---

# 45. Synthetic Event Generator

The event generator should be configurable.

Parameters:

```text
eventRate
duration
stationDistribution
trainDistribution
passengerRate
delayProbability
alertProbability
```

Example:

```text
events/second = 20
duration = 60 seconds
```

Result:

approximately 1,200 generated events.

The exact count may vary when concurrency is involved and must be reported from actual execution.

---

# 46. Algorithm Trace Architecture

Traces must be optional.

Do not collect expensive step-by-step traces during normal production-style execution unless requested.

Use:

```text
traceEnabled = true
```

for the Algorithm Lab.

Example:

```text
AlgorithmRequest
├── input
├── traceEnabled
└── benchmarkEnabled
```

---

# 47. Security Boundary

The Algorithm Lab may expose powerful computational operations.

The backend must validate:

- maximum input size
- graph size
- matrix size
- document size
- number of variables
- number of clauses
- sample size

Prevent intentionally enormous requests from consuming unlimited server resources.

---

# 48. Deployment Architecture

Initial deployment:

```text
Frontend
   ↓
Vercel

Backend
   ↓
Render / equivalent Java host

Database
   ↓
Managed PostgreSQL
```

Deployment is not part of the first development phase.

Local development must work without cloud dependencies.

---

# 49. Development Environment

Recommended:

```text
Node.js
Java 21
Maven
PostgreSQL
Git
Docker optional
```

Exact versions may be pinned after environment verification.

---

# 50. Repository Structure

Final target:

```text
RailSync/
│
├── AGENTS.md
│
├── README.md
│
├── docs/
│   ├── 01-project-specification.md
│   ├── 02-system-architecture.md
│   ├── 03-database-design.md
│   ├── 04-api-contract.md
│   ├── 05-algorithm-contracts.md
│   ├── 06-ui-architecture.md
│   ├── 07-testing-strategy.md
│   └── 08-development-roadmap.md
│
├── .agents/
│   ├── rules/
│   └── skills/
│
├── frontend/
│
├── backend/
│
├── database/
│
└── scripts/
```

---

# 51. Agent Ownership

## Foundation Agent

May modify:

```text
frontend/
backend/
database/
configuration
```

during foundation phase.

---

## M1 Agent

Primary ownership:

```text
backend/.../algorithm/m1
backend/.../service/m1
backend/.../controller/m1
frontend/.../algorithms/m1
tests
```

Do not modify M2–M6.

---

## M2 Agent

Primary ownership:

```text
algorithm/m2
service/m2
controller/m2
frontend/algorithms/m2
tests
```

---

## M3 Agent

Primary ownership:

```text
algorithm/m3
service/m3
controller/m3
frontend/algorithms/m3
tests
```

---

## M4 Agent

Primary ownership:

```text
algorithm/m4
service/m4
controller/m4
frontend/algorithms/m4
frontend/graphs
tests
```

---

## M5 Agent

Primary ownership:

```text
algorithm/m5
service/m5
controller/m5
frontend/algorithms/m5
tests
```

---

## M6 Agent

Primary ownership:

```text
algorithm/m6
service/m6
controller/m6
frontend/algorithms/m6
tests
```

---

# 52. Shared Components

Shared components require extra caution.

Examples:

```text
AlgorithmResult
AlgorithmTrace
BenchmarkResult
GraphNode
GraphEdge
API error model
```

Changes to these require:

1. dependency inspection
2. affected-module tests
3. full integration tests

---

# 53. Architecture Decision Records

Important decisions should be recorded under:

```text
docs/adr/
```

Examples:

```text
ADR-001-modular-monolith.md
ADR-002-java-algorithm-engine.md
ADR-003-postgresql.md
ADR-004-websocket-events.md
ADR-005-synthetic-railway-data.md
```

---

# 54. Architecture Verification Checklist

Before moving from foundation to M1:

[ ] React starts successfully

[ ] Spring Boot starts successfully

[ ] PostgreSQL connection works

[ ] Database migrations work

[ ] Seed data loads

[ ] GET /api/stations works

[ ] GET /api/trains works

[ ] Frontend can call backend

[ ] API error handling works

[ ] WebSocket foundation works

[ ] Unit testing works

[ ] Integration testing works

[ ] Project builds from clean checkout

[ ] No M1–M6 algorithm has been replaced with a library

---

# 55. Architectural Definition of Done

The architecture is considered established when:

```text
Frontend
   ↓
API
   ↓
Service
   ↓
Algorithm / Domain
   ↓
Repository
   ↓
PostgreSQL
```

works cleanly for at least one complete vertical slice.

The first vertical slice should be:

```text
Station
   ↓
GET /api/stations
   ↓
React Station List
```

After that, build the first DSA vertical slice:

```text
Station Search
   ↓
POST /api/m1/kmp
   ↓
KMP Algorithm
   ↓
Search Result
   ↓
React Visualization
```

This proves the architecture before the remaining algorithms are built.