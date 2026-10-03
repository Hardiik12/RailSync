# RailSync — Master Agent Development Rules

> **This file is the primary instruction file for all AI coding agents working on RailSync.**

---

# 1. Project Identity

**Project Name:** RailSync

**Project Type:** Academic Railway Operations Simulation & Analytics Platform

**Primary Focus:** Data Structures & Algorithms applied to railway operations

**Architecture:** Full-stack modular monolith

**Backend:** Java + Spring Boot

**Frontend:** React + Vite + Tailwind CSS + Framer Motion

**Database:** PostgreSQL

RailSync is an academic railway operations and analytics platform designed to demonstrate how advanced Data Structures and Algorithms can be applied to realistic railway-domain problems.

The railway domain is the application context.

The DSA algorithms are a primary part of the system, not decorative features.

The project must remain:

- technically correct
- explainable
- testable
- reproducible
- visually demonstrable
- faithful to the prescribed DSA syllabus
- consistent with the documented architecture

---

# 2. Documentation Is the Source of Truth

The `docs/` directory contains the authoritative project specification.

Before implementing or modifying a feature, read the relevant documentation.

Authoritative documents:

```text
docs/
├── 01-project-specification.md
├── 02-system-architecture.md
├── 03-database-design.md
├── 04-api-contract.md
├── 05-algorithm-contracts.md
├── 06-ui-architecture.md
├── 07-testing-strategy.md
└── 08-development-roadmap.md
```

Agents must not silently redesign the project when existing code conflicts with the documented architecture.

If a conflict is discovered:

1. Inspect the existing implementation.
2. Identify the conflict.
3. Determine whether the documentation or implementation is outdated.
4. Report the conflict.
5. Make the smallest justified change.

Do not arbitrarily replace documented decisions.

---

# 3. Mandatory Project Objective

RailSync must demonstrate the following six DSA modules:

```text
M1 — CO1 — String Algorithms
M2 — CO2 — Suffix Structures
M3 — CO3 — Advanced Dynamic Programming
M4 — CO4 — Network Flow
M5 — CO5 — NP-Completeness and Approximation
M6 — CO6 — Randomized and Parallel Algorithms
```

Every required algorithm must have:

```text
Manual Implementation
        ↓
Unit Tests
        ↓
Railway-Domain Application
        ↓
Backend Service
        ↓
REST/API Integration
        ↓
Frontend Interface
        ↓
Visualization / Trace where appropriate
        ↓
Benchmark / Metrics where appropriate
        ↓
Documentation
```

---

# 4. Non-Negotiable DSA Scope

The following algorithm mapping is frozen.

Do not add or remove algorithms without explicit approval.

---

# 5. M1 — CO1 — String Algorithms

## Required Algorithms

1. KMP
2. Z-Function
3. Rabin-Karp
4. Aho-Corasick

## Railway Applications

### KMP

Use for:

- train-number search
- station-name search
- service-information search
- operational text searching

### Z-Function

Use for:

- repeated operational pattern detection
- railway text pattern analysis

### Rabin-Karp

Use for:

- ticket-code search
- booking-code search
- rolling-hash text matching

### Aho-Corasick

Use for:

- multi-keyword railway-alert detection
- service-alert classification
- simultaneous keyword searching

---

# 6. M2 — CO2 — Suffix Structures

## Required Algorithms

1. Suffix Array
2. SA-IS
3. LCP
4. Kasai
5. Suffix Automaton

**Important:** The implementation scope uses **Suffix Automaton**.

Do not independently add Suffix Tree unless explicitly requested.

## Railway Applications

- railway-document indexing
- service-information search
- repeated service-description detection
- substring search
- document similarity analysis

## Dependency

The implementation should respect relationships between:

```text
Suffix Array
      ↓
LCP
      ↓
Kasai
```

SA-IS must be implemented as its own suffix-array construction algorithm.

Suffix Automaton is implemented independently as a substring-oriented structure.

---

# 7. M3 — CO3 — Advanced Dynamic Programming

## Required Algorithms

1. Levenshtein Distance
2. Damerau-Levenshtein Distance
3. Bitmask DP
4. Matrix-Chain Multiplication
5. Optimal Binary Search Tree

## Railway Applications

### Levenshtein

- station-name correction
- passenger-data correction suggestions

### Damerau-Levenshtein

- station-name correction with transpositions
- passenger-data correction

### Bitmask DP

- small-scale service combinations
- small-scale route combinations
- bounded optimization demonstrations

Bitmask DP must remain explicitly limited to small bounded instances.

### Matrix-Chain Multiplication

- railway-data operation ordering
- optimization of matrix/data transformation sequences

### Optimal BST

- frequently accessed station/service record analysis
- access-frequency optimization demonstration

---

# 8. M4 — CO4 — Network Flow

## Required Algorithms

1. Ford-Fulkerson
2. Edmonds-Karp
3. Dinic
4. Bipartite Matching
5. König's Theorem
6. Max-Flow Min-Cut

## Railway Applications

- passenger flow
- railway network capacity
- platform assignment
- service/platform conflict analysis
- bottleneck identification

## Graph Architecture

M4 algorithms should share common graph abstractions:

```text
Graph
Edge
FlowNetwork
ResidualGraph
```

Flow algorithms operate on in-memory graph structures.

The database stores railway network data but does not replace the algorithmic graph implementation.

## Dependency

```text
Graph Model
      ↓
Ford-Fulkerson
      ↓
Edmonds-Karp
      ↓
Dinic
      ↓
Matching / Min-Cut
```

---

# 9. M5 — CO5 — NP-Completeness and Approximation

## Required Algorithms / Transformations

1. SAT
2. 3-SAT
3. 3-SAT → CLIQUE
4. CLIQUE → INDEPENDENT-SET
5. INDEPENDENT-SET → VERTEX-COVER
6. Vertex-Cover 2-Approximation

## Railway Applications

- scheduling constraints
- service conflicts
- platform constraints
- conflict-graph analysis

## Important Scope Rule

M5 is primarily a theoretical and experimental laboratory.

Do not claim:

> RailSync solves railway scheduling optimally.

Use language such as:

> RailSync models railway scheduling constraints and demonstrates SAT, reductions, conflict graphs, and approximation techniques.

All reductions must be inspectable.

The system should expose:

```text
Input Instance
      ↓
Transformation
      ↓
Output Instance
      ↓
Correctness Relationship
```

---

# 10. M6 — CO6 — Randomized and Parallel Algorithms

## Required Algorithms

1. Randomized QuickSort
2. Reservoir Sampling
3. Miller-Rabin
4. Blelloch Scan
5. Parallel Reduce
6. Brent's Theorem

## Railway Applications

### Randomized QuickSort

- train/service record ranking

### Reservoir Sampling

- continuous railway-event stream sampling

### Miller-Rabin

- numerical/algorithmic primality demonstration

Miller-Rabin must not be presented as if railway operations inherently require primality testing.

### Blelloch Scan

- cumulative passenger statistics
- prefix-style aggregation

### Parallel Reduce

- railway-data aggregation

### Brent's Theorem

- theoretical parallel-performance analysis

Brent's theorem must distinguish theoretical analysis from actual parallel hardware execution.

---

# 11. Core Engineering Principle

Every required DSA algorithm must exist as an actual implementation.

Do not replace required algorithms with:

- Java standard-library implementations
- third-party algorithm libraries
- database full-text search
- graph libraries
- external optimization libraries
- hidden framework implementations

Infrastructure libraries are allowed.

The required DSA algorithms must be manually implemented.

---

# 12. Algorithm Architecture

Every algorithm must follow:

```text
Algorithm Layer
      ↓
Railway Service Layer
      ↓
REST/API Layer
```

Example:

```text
KMPAlgorithm
      ↓
RailwaySearchService
      ↓
KMPController
```

Do not put algorithm logic directly into:

- React components
- controllers
- database repositories
- entity classes

---

# 13. Generic Algorithm Contract

Algorithms should follow the common abstraction:

```java
public interface Algorithm<I, O> {

    O execute(I input);

    String getName();

    Complexity getComplexity();
}
```

Where practical, algorithm execution should support:

```text
Result
Metrics
Optional Trace
```

---

# 14. Algorithm Requirements

Every algorithm must provide:

- input definition
- output definition
- core implementation
- edge-case handling
- time complexity
- space complexity
- unit tests
- execution metrics where applicable
- optional trace/visualization data where applicable
- railway-domain adapter
- REST endpoint where applicable

---

# 15. No Hardcoded Algorithm Results

Never hardcode:

- maximum-flow values
- matching results
- edit distances
- search matches
- DP results
- benchmark results
- approximation results
- execution times
- suffix structures
- SAT results
- reduction outputs

All algorithm outputs must be calculated at runtime.

Synthetic railway data may be seeded.

Algorithm results must never be seeded as fake outputs.

---

# 16. Complexity Must Be Honest

Every algorithm page must display its theoretical complexity.

Always distinguish:

```text
Theoretical Complexity
Measured Execution Time
Measured Operation Count
Input Size
```

For example:

```text
KMP

Time Complexity: O(n + m)
Space Complexity: O(m)

Measured Runtime: actual runtime
Operation Count: actual count
```

Do not claim one algorithm is universally faster based on one local benchmark.

Do not manufacture performance improvements.

Do not fabricate benchmark data.

---

# 17. Execution Metrics

Where appropriate, execution metadata should contain:

```text
algorithm
module
inputSize
executionTimeNanos
operationCount
timeComplexity
spaceComplexity
traceEnabled
timestamp
```

Measured metrics must come from actual execution.

---

# 18. Trace System

Algorithm tracing must be optional.

Default:

```text
traceEnabled = false
```

When enabled, traces should contain structured information such as:

```text
step
action
state
description
```

Example:

```json
{
  "step": 4,
  "action": "COMPARE",
  "state": {
    "textIndex": 7,
    "patternIndex": 3
  },
  "description": "Comparing text[7] with pattern[3]"
}
```

All trace-producing algorithms must respect:

```text
maxTraceSteps
```

Never allow unlimited trace generation.

If the trace limit is reached, the trace may be truncated while the underlying algorithm continues where safe.

---

# 19. Randomized Algorithm Rules

Randomized algorithms must support deterministic seeds.

Example:

```text
seed = 42
```

The same input and seed should produce reproducible behavior wherever the algorithm design permits.

This is required for:

- testing
- debugging
- demonstrations
- benchmark comparisons

---

# 20. Approximation Algorithm Rules

Never label an approximation result as optimal unless optimality has independently been established.

For Vertex Cover 2-Approximation, display:

- approximate solution
- solution size
- approximation guarantee
- optional exact solution for small graphs
- comparison against exact optimum where available

Clearly label the result as an approximation.

Do not fabricate an optimal result.

---

# 21. NP-Completeness Rules

M5 must remain bounded and educational.

Do not allow unrestricted exponential computation.

Use configured limits for:

- SAT
- 3-SAT
- CLIQUE
- Independent Set
- Vertex Cover

For small test cases, brute-force reference implementations may be used inside tests.

Production implementations must still demonstrate the required algorithms and transformations.

---

# 22. Data Rules

Use PostgreSQL as the persistent database.

Use deterministic synthetic railway data for reproducibility.

Core entities include:

```text
Stations
Platforms
Trains
Routes
Route Stops
Trips
Stop Times
Passengers
Tickets
Service Alerts
Maintenance Records
Railway Events
Railway Documents
Network Edges
```

Recommended seed scale:

```text
30–50 Stations
100+ Platforms
50+ Trains
50+ Routes
300+ Route Stops
100+ Trips
500+ Stop Times
5,000+ Passengers
10,000+ Tickets
500+ Service Alerts
100+ Maintenance Records
20,000+ Railway Events
100+ Railway Documents
100+ Network Edges
```

The exact values may change during implementation.

The seed must remain:

- deterministic
- reproducible
- internally consistent
- relationally valid

---

# 23. Passenger Data Rules

Do not use real passenger personal information.

Synthetic data only.

Do not commit:

- real names from real passengers
- government IDs
- Aadhaar numbers
- phone numbers belonging to real people
- real addresses
- real ticket information
- confidential railway data

---

# 24. External Railway API Rule

The core application must not depend on an external railway API.

The project must work completely using:

```text
Synthetic Data
+
PostgreSQL
+
Internal Simulation
```

External data imports may be added later without changing the core architecture.

Do not make an external service a prerequisite for the application to run.

---

# 25. Railway Domain Rules

RailSync is an academic railway simulation and decision-support platform.

It is not:

- an official railway control system
- an actual railway dispatch system
- a safety-certified railway system
- an official Indian Railways platform
- a real-world train-control system

Use realistic railway concepts while clearly identifying simulated data and decisions.

---

# 26. Backend Technology Rules

Preferred backend:

```text
Java
Spring Boot
PostgreSQL
Maven
```

Use the following separation:

```text
Controller
Service
Repository
DTO
Entity
Algorithm
Mapper where required
```

Controllers should remain thin.

Business logic belongs in services.

Persistence belongs in repositories.

Algorithm logic belongs in algorithm classes.

Do not expose database entities directly as public API contracts when DTOs are appropriate.

---

# 27. Backend Architecture

Use:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

Algorithm execution:

```text
Controller
    ↓
Algorithm Service
    ↓
Algorithm Implementation
```

Railway-specific algorithm execution:

```text
Railway Data
    ↓
Railway Service / Adapter
    ↓
Algorithm Input
    ↓
Algorithm
    ↓
Algorithm Result
```

---

# 28. Frontend Technology Rules

Preferred frontend:

```text
React
Vite
Tailwind CSS
Framer Motion
React Router
```

Frontend responsibilities:

- data visualization
- algorithm input
- algorithm execution
- result display
- algorithm trace visualization
- railway network visualization
- charts
- operational dashboards

Do not implement core DSA algorithms in JavaScript when the backend Java implementation is required.

---

# 29. Frontend Algorithm Boundary

Correct:

```text
React
  ↓
REST API
  ↓
Java Algorithm
  ↓
Structured Result
  ↓
React Visualization
```

Incorrect:

```text
React
  ↓
JavaScript KMP implementation
```

The frontend visualizes backend algorithm results.

---

# 30. UI Design Rules

The RailSync interface should be:

- dark
- modern
- technical
- railway-operations oriented
- data-dense but readable
- futuristic without excessive decoration

Avoid:

- generic admin-template appearance
- excessive neon
- fake AI interfaces
- unnecessary animation
- visual clutter
- decorative elements that obscure operational data

Use Framer Motion only when animation provides meaningful feedback.

---

# 31. Algorithm Page Standard

Every algorithm page should follow:

```text
Algorithm Header
        ↓
Railway Use Case
        ↓
Input
        ↓
Controls
        ↓
Run
        ↓
Visualization
        ↓
Result
        ↓
Complexity
        ↓
Benchmark
        ↓
Trace
```

Reusable components should be preferred.

---

# 32. API Rules

Use REST APIs for standard request/response operations.

Use WebSocket where real-time event delivery is required.

API naming must follow the module structure.

Examples:

```text
POST /api/m1/kmp
POST /api/m1/z
POST /api/m1/rabin-karp
POST /api/m1/aho-corasick

POST /api/m2/suffix-array
POST /api/m2/sa-is
POST /api/m2/lcp
POST /api/m2/kasai
POST /api/m2/suffix-automaton

POST /api/m3/levenshtein
POST /api/m3/damerau
POST /api/m3/bitmask
POST /api/m3/matrix-chain
POST /api/m3/optimal-bst

POST /api/m4/ford-fulkerson
POST /api/m4/edmonds-karp
POST /api/m4/dinic
POST /api/m4/bipartite-matching
POST /api/m4/konig
POST /api/m4/max-flow-min-cut

POST /api/m5/sat
POST /api/m5/3sat
POST /api/m5/3sat-to-clique
POST /api/m5/clique-to-independent-set
POST /api/m5/independent-set-to-vertex-cover
POST /api/m5/vertex-cover-2approx

POST /api/m6/randomized-quicksort
POST /api/m6/reservoir-sampling
POST /api/m6/miller-rabin
POST /api/m6/blelloch-scan
POST /api/m6/parallel-reduce
POST /api/m6/brent
```

Do not create inconsistent endpoint naming.

Follow `docs/04-api-contract.md`.

---

# 33. Standard API Response

Successful responses should follow:

```json
{
  "success": true,
  "data": {},
  "meta": {
    "requestId": "req-123",
    "timestamp": "2026-10-02T18:20:31Z"
  }
}
```

Algorithm responses should expose sufficient information for visualization and benchmarking.

Typical fields:

```text
algorithm
inputSize
result
executionTime
operationCount
complexity
trace
```

Do not expose unnecessary internal implementation details.

---

# 34. API Error Contract

Use standardized error categories:

```text
INVALID_INPUT
RESOURCE_NOT_FOUND
ALGORITHM_ERROR
INPUT_TOO_LARGE
TRACE_LIMIT_EXCEEDED
UNSUPPORTED_OPERATION
DATABASE_ERROR
INTERNAL_ERROR
```

Never expose raw stack traces through the public API.

---

# 35. Database Rules

PostgreSQL stores persistent railway-domain information.

Examples:

```text
station
platform
train
route
route_stop
trip
stop_time
passenger
ticket
service_alert
maintenance_record
railway_event
railway_document
network_edge
```

The database must not replace the required DSA implementations.

Example:

```text
PostgreSQL
    ↓
Retrieve railway text
    ↓
Java KMP implementation
    ↓
Search result
```

Not:

```text
PostgreSQL Full Text Search
    ↓
pretend this is KMP
```

---

# 36. Algorithm State Rules

Algorithm state belongs in memory during execution.

Examples:

### KMP

```text
text
pattern
LPS
indices
matches
```

### Dinic

```text
vertices
edges
capacities
flows
level graph
current-edge pointers
```

### Suffix Automaton

```text
states
transitions
suffix links
lengths
```

Do not turn algorithm state into database entities unless historical execution storage is explicitly required.

---

# 37. Real-Time Event Architecture

Railway events are simulated.

Architecture:

```text
Event Generator
      ↓
Event Service
      ↓
Event Persistence
      ↓
WebSocket Publisher
      ↓
React WebSocket Client
      ↓
Live Event Feed
```

Example events:

```text
TRAIN_ARRIVAL
TRAIN_DEPARTURE
TRAIN_DELAY
PLATFORM_CHANGE
SERVICE_ALERT
MAINTENANCE_EVENT
PASSENGER_EVENT
```

Do not connect to real railway control infrastructure.

---

# 38. Large Dataset Rules

Never blindly send large datasets to the browser.

Use:

- pagination
- filtering
- aggregation
- sampling
- virtualization

Example:

```text
20,000 events
      ↓
Backend filtering
      ↓
Relevant dataset
      ↓
Frontend
```

---

# 39. Testing Rules

Every algorithm requires unit tests.

Tests must include where applicable:

- normal case
- empty input
- single-element input
- duplicate data
- boundary cases
- invalid input
- large input
- known expected result
- railway-domain scenario

Recommended backend testing:

```text
JUnit 5
Spring Boot Test
Mockito
MockMvc
Testcontainers
AssertJ
```

Recommended frontend testing:

```text
Vitest
React Testing Library
jsdom
Playwright
```

---

# 40. Cross-Validation Rules

Where multiple algorithms solve equivalent problems, cross-validation is mandatory.

Examples:

```text
KMP
  ==
Z-Function
  ==
Rabin-Karp
```

for equivalent pattern-matching inputs.

```text
Ford-Fulkerson
  ==
Edmonds-Karp
  ==
Dinic
```

for equivalent flow networks.

```text
Maximum Flow
  ==
Minimum Cut Capacity
```

```text
Maximum Matching
  ==
Minimum Vertex Cover
```

where the relevant theorem applies.

M5 reductions must be cross-validated using small instances.

M6 parallel reductions must be compared against sequential reference implementations.

---

# 41. Reference Implementations

Simple brute-force/reference algorithms may be used inside tests.

Example:

```text
Naive Pattern Matching
        ↓
compare with
        ↓
KMP
```

This is acceptable for testing.

Reference implementations must not replace the production algorithms.

---

# 42. M4 Specific Rules

M4 algorithms must share graph abstractions where appropriate.

Flow algorithms must operate on the same conceptual graph model.

For equivalent inputs, cross-check:

```text
Ford-Fulkerson
Edmonds-Karp
Dinic
```

Verify:

```text
Max Flow = Min Cut Capacity
```

For matching:

```text
Maximum Matching
        ↓
König Analysis
        ↓
Minimum Vertex Cover
```

---

# 43. M5 Specific Rules

Every reduction must preserve the mathematical relationship.

Required chain:

```text
3-SAT
   ↓
CLIQUE
   ↓
INDEPENDENT SET
   ↓
VERTEX COVER
```

The implementation should expose enough intermediate information for the frontend to visualize the transformation.

Do not implement the complete chain as an opaque black box.

---

# 44. M6 Specific Rules

Randomized algorithms:

```text
Randomized QuickSort
Reservoir Sampling
Miller-Rabin
```

must support deterministic seeds where randomness is used.

Parallel algorithms:

```text
Blelloch Scan
Parallel Reduce
```

must clearly distinguish algorithmic structure from actual hardware parallelism.

Brent's theorem should report theoretical quantities such as:

```text
Work W
Span S
Processors P
Estimated Tₚ
```

Do not claim real parallel speedup unless it has actually been measured.

---

# 45. Input Safety

Algorithms with expensive complexity require explicit limits.

Examples:

```text
Bitmask DP
SAT
3-SAT
CLIQUE
Independent Set
Vertex Cover
```

The application must reject unsafe inputs rather than allowing uncontrolled computation.

Use:

```text
INPUT_TOO_LARGE
```

where appropriate.

---

# 46. Performance Rules

Performance must be measured, not assumed.

Do not:

- fabricate benchmarks
- hardcode runtime values
- claim universal superiority
- remove correctness checks for speed
- optimize before measuring

Theoretical complexity and measured performance must remain separate.

---

# 47. Logging Rules

Useful logs may include:

```text
request ID
endpoint
algorithm
input size
execution duration
error category
```

Never log:

- credentials
- passwords
- access tokens
- secrets
- real passenger PII

---

# 48. Git Rules

Primary branches:

```text
main
develop
```

Feature branches may include:

```text
feature/foundation
feature/database
feature/vertical-slice
feature/m1
feature/m2
feature/m3
feature/m4
feature/m5
feature/m6
feature/realtime
feature/analytics
feature/ui
```

Commit prefixes:

```text
feat
fix
test
refactor
docs
chore
perf
```

Examples:

```text
feat(m1): implement KMP algorithm

test(m1): add KMP edge cases

feat(api): expose KMP execution endpoint

fix(m4): correct residual capacity update

docs(m2): document suffix automaton
```

---

# 49. Agent Boundaries

Agents must work within explicit task boundaries.

Examples:

### M1 Agent

May modify:

```text
M1 algorithm code
M1 tests
M1 service
M1 API
M1 frontend
shared infrastructure required by M1
```

Should not modify M4 or redesign the database.

### M2 Agent

May modify:

```text
M2 algorithm code
M2 tests
M2 document integration
M2 API
M2 frontend
```

### Frontend Agent

Primarily modifies:

```text
frontend/
```

Backend changes require explicit justification.

### Database Agent

Primarily modifies:

```text
database/
backend migrations
seed system
```

### Testing Agent

May add:

```text
tests
test infrastructure
test utilities
```

but must not silently redesign application architecture.

---

# 50. Agent Task Protocol

Before making substantial changes, the agent must determine:

```text
Goal
Files to change
Dependencies
Expected result
Acceptance criteria
```

The agent should work on the smallest appropriate scope.

Do not rewrite unrelated code.

---

# 51. Before Coding

Before implementation:

1. Read the relevant documentation.
2. Inspect the repository.
3. Search for existing implementations.
4. Check current tests.
5. Identify dependencies.
6. Determine the smallest required change.
7. Implement.
8. Run relevant tests.
9. Inspect the resulting diff.
10. Report the result.

Do not recreate existing functionality.

Do not overwrite working code without justification.

---

# 52. Change Control

Before modifying another module:

1. Inspect the existing implementation.
2. Identify dependencies.
3. Explain why the change is necessary.
4. Avoid unrelated refactoring.
5. Run affected tests.
6. Run the full test suite if shared contracts changed.

Do not rewrite working modules merely because a different coding style is preferred.

---

# 53. No Scope Creep

Do not add the following unless explicitly requested:

- unnecessary AI
- blockchain
- cryptocurrency
- unrelated machine learning
- unrelated DSA algorithms
- unnecessary microservices
- unnecessary external APIs
- chatbot features
- unrelated IoT components
- payment systems
- facial recognition
- real railway control
- complex authentication

The project must remain centered on:

```text
Railway Domain
+
M1
+
M2
+
M3
+
M4
+
M5
+
M6
```

---

# 54. Development Order

Follow this development order unless explicitly changed.

```text
Phase 0
Repository + Agent Setup
        ↓
Phase 1
Backend + Frontend Foundation
        ↓
Phase 2
Database + Synthetic Seed
        ↓
Phase 3
First Vertical Slice
        ↓
Phase 4
M1 — String Algorithms
        ↓
Phase 5
M2 — Suffix Structures
        ↓
Phase 6
M3 — Advanced DP
        ↓
Phase 7
M4 — Network Flow
        ↓
Phase 8
M5 — NP-Completeness
        ↓
Phase 9
M6 — Randomized + Parallel
        ↓
Phase 10
Real-Time Event System
        ↓
Phase 11
Analytics + Benchmarking
        ↓
Phase 12
Full Integration
        ↓
Phase 13
Hardening + Demo
```

Do not implement all modules simultaneously.

---

# 55. Phase 0

Phase 0 establishes the project environment.

Tasks:

```text
Create repository structure
Create AGENTS.md
Create .agents/rules
Create .agents/skills
Verify documentation
Verify Git configuration
Verify frontend/backend/database directories
```

Phase 0 must not begin implementing M1–M6.

---

# 56. Phase 1

Establish:

### Backend

```text
Spring Boot
Maven
PostgreSQL configuration
Flyway
REST
Validation
Exception handling
Logging
CORS
Health endpoint
```

### Frontend

```text
React
Vite
Tailwind
React Router
Framer Motion
API client
Application shell
Sidebar
Topbar
Routing
```

The phase is complete only when frontend and backend can run independently.

---

# 57. Phase 2

Implement:

```text
Database migrations
Schema
Foreign keys
Indexes
Synthetic deterministic seed
```

Verify:

```text
migrations succeed
seed succeeds
relationships are valid
seed is reproducible
```

---

# 58. Phase 3 — First Vertical Slice

The first vertical slice should prove the complete architecture.

Recommended sequence:

```text
Station Repository
        ↓
Station Service
        ↓
GET /api/stations
        ↓
React Stations Page
```

Then:

```text
KMP Algorithm
        ↓
KMP Service
        ↓
POST /api/m1/kmp
        ↓
React KMP Page
```

This phase proves:

```text
Database
Backend
Repository
Service
Algorithm Engine
REST
React
Testing
```

---

# 59. Phase 4 — M1

Implement sequentially:

```text
KMP
 ↓
Z-Function
 ↓
Rabin-Karp
 ↓
Aho-Corasick
```

For each:

```text
Core Algorithm
 ↓
Unit Tests
 ↓
Service
 ↓
REST
 ↓
Frontend
 ↓
Visualization
 ↓
Benchmark
 ↓
Integration
```

Cross-validate equivalent string-search results.

---

# 60. Phase 5 — M2

Implement:

```text
Suffix Array
 ↓
SA-IS
 ↓
LCP
 ↓
Kasai
 ↓
Suffix Automaton
```

Integrate with railway documents.

Verify:

```text
Suffix Array correctness
SA-IS correctness
LCP correctness
Kasai correctness
Substring search correctness
```

---

# 61. Phase 6 — M3

Implement:

```text
Levenshtein
 ↓
Damerau-Levenshtein
 ↓
Bitmask DP
 ↓
Matrix-Chain Multiplication
 ↓
Optimal BST
```

Enforce input limits for exponential algorithms.

---

# 62. Phase 7 — M4

Implement:

```text
Graph Model
 ↓
Ford-Fulkerson
 ↓
Edmonds-Karp
 ↓
Dinic
 ↓
Bipartite Matching
 ↓
König
 ↓
Max-Flow Min-Cut
```

Cross-validate flow algorithms.

Verify max-flow/min-cut relationship.

---

# 63. Phase 8 — M5

Implement:

```text
SAT
 ↓
3-SAT
 ↓
3-SAT → CLIQUE
 ↓
CLIQUE → Independent Set
 ↓
Independent Set → Vertex Cover
 ↓
Vertex Cover 2-Approximation
```

Keep instances bounded.

Make transformations inspectable.

---

# 64. Phase 9 — M6

Implement:

```text
Randomized QuickSort
 ↓
Reservoir Sampling
 ↓
Miller-Rabin
 ↓
Blelloch Scan
 ↓
Parallel Reduce
 ↓
Brent's Theorem
```

Randomized algorithms must support seeds.

Parallel algorithms must distinguish theoretical analysis from actual hardware execution.

---

# 65. Phase 10 — Real-Time Events

Implement:

```text
Railway Event Generator
        ↓
Event Service
        ↓
Persistence
        ↓
WebSocket Publisher
        ↓
React Live Feed
```

Events remain simulated.

---

# 66. Phase 11 — Analytics

Add:

```text
Algorithm execution history
Benchmark results
Execution metrics
Operation counts
Input-size comparisons
Analytics dashboards
```

Never fabricate benchmark data.

---

# 67. Phase 12 — Full Integration

Verify that:

```text
Operations
+
DSA Modules
+
Documents
+
Events
+
Analytics
+
Database
+
Frontend
```

work together.

Run:

```text
backend tests
frontend tests
integration tests
E2E tests
```

---

# 68. Phase 13 — Hardening

Final hardening includes:

- error handling
- validation
- accessibility
- responsive UI
- performance
- security basics
- logging
- documentation
- test coverage
- demo preparation

Do not introduce major new features during hardening.

---

# 69. Module Completion Rule

A module is not complete until all applicable items exist:

```text
[ ] Algorithm implementation
[ ] Unit tests
[ ] Edge-case tests
[ ] Complexity documentation
[ ] Railway-domain integration
[ ] REST API
[ ] Frontend interface
[ ] Visualization / trace
[ ] Benchmarking
[ ] Error handling
[ ] Integration tests
[ ] Documentation
```

---

# 70. Definition of Done — Algorithm

An individual algorithm is complete only when:

```text
[ ] Correct implementation
[ ] Correct input/output contract
[ ] Unit tests
[ ] Edge-case tests
[ ] Complexity documented
[ ] Metrics implemented where applicable
[ ] Trace implemented where useful
[ ] Railway use case integrated
[ ] Service integration
[ ] API integration
[ ] Frontend integration
[ ] Visualization where meaningful
[ ] Benchmark support where meaningful
[ ] Documentation
```

---

# 71. Definition of Done — Project

RailSync is complete when:

```text
[ ] Frontend works
[ ] Backend works
[ ] PostgreSQL works
[ ] Migrations work
[ ] Seed works
[ ] M1 complete
[ ] M2 complete
[ ] M3 complete
[ ] M4 complete
[ ] M5 complete
[ ] M6 complete
[ ] Real-time events work
[ ] Analytics work
[ ] Tests pass
[ ] E2E flow works
[ ] Documentation matches implementation
```

---

# 72. Final Demonstration Flow

The final demonstration should follow a coherent railway workflow.

## 1. Operations Dashboard

Show:

- active trains
- stations
- platforms
- alerts
- delayed trains
- passenger statistics
- live events

## 2. Station / Train Search

Demonstrate:

```text
KMP
```

## 3. Railway Alert Analysis

Demonstrate:

```text
Aho-Corasick
```

## 4. Railway Document Analysis

Demonstrate:

```text
Suffix Array
SA-IS
LCP
Kasai
Suffix Automaton
```

## 5. Station Correction

Demonstrate:

```text
Levenshtein
Damerau-Levenshtein
```

## 6. Platform Assignment

Demonstrate:

```text
Bipartite Matching
König's Theorem
```

## 7. Passenger Flow

Demonstrate:

```text
Dinic
Max-Flow
Min-Cut
```

## 8. Scheduling Constraint Analysis

Demonstrate:

```text
SAT
3-SAT
3-SAT → CLIQUE
CLIQUE → Independent Set
Independent Set → Vertex Cover
Vertex Cover 2-Approximation
```

## 9. Live Railway Event Stream

Demonstrate:

```text
Reservoir Sampling
```

## 10. Analytics

Show:

```text
Execution Time
Operation Count
Input Size
Theoretical Complexity
Benchmark History
```

---

# 73. Agent Communication Protocol

Before substantial implementation, report:

```text
Task
Files to be changed
Reason
Dependencies
Expected result
```

After implementation, report:

```text
Files changed
Implementation completed
Tests executed
Tests passed
Tests failed
Known limitations
Next recommended step
```

Never claim:

> Tests passed

unless the tests were actually executed.

Never claim:

> Implementation complete

unless the completion criteria have actually been satisfied.

---

# 74. Failure Handling

If an implementation fails:

1. Preserve the useful work.
2. Inspect the actual error.
3. Identify the root cause.
4. Fix the smallest necessary area.
5. Re-run the affected tests.
6. Report the result.

Do not:

- delete tests
- disable validation
- comment out failing code
- suppress errors
- replace algorithms with shortcuts
- claim success without verification

---

# 75. Architectural Change Rule

If a task appears to require an architectural change:

```text
STOP
↓
Inspect
↓
Identify dependency
↓
Explain required change
↓
Confirm consistency with docs
↓
Implement only if justified
```

Do not silently introduce:

- new frameworks
- new databases
- microservices
- new architecture patterns
- external dependencies
- unrelated infrastructure

---

# 76. Code Quality Rules

Prefer:

- meaningful names
- small focused classes
- clear interfaces
- testable methods
- explicit validation
- minimal duplication
- predictable behavior
- readable algorithms

Avoid:

- giant classes
- giant controllers
- hidden global state
- duplicated algorithm implementations
- unnecessary abstractions
- premature optimization
- clever code that is difficult to explain in a viva

Because RailSync is an academic project, every important algorithm should be understandable enough to explain during evaluation.

---

# 77. Viva / Demonstration Principle

Every major implementation decision should be explainable.

An evaluator should be able to ask:

> Why is this algorithm used here?

and the project should provide a clear answer.

The system should make visible:

```text
Railway Problem
      ↓
Algorithm Selection
      ↓
Algorithm Execution
      ↓
Result
      ↓
Complexity
      ↓
Practical Interpretation
```

Do not create features whose algorithmic purpose cannot be clearly explained.

---

# 78. Final Success Definition

RailSync succeeds when a reviewer can:

1. Open the railway operations interface.
2. Select a railway problem.
3. Select the corresponding DSA algorithm.
4. Run the algorithm on actual project data.
5. Observe the computed result.
6. Observe an understandable visualization or trace.
7. See complexity and execution metrics.
8. Understand why the algorithm belongs to the railway problem.
9. Inspect the implementation.
10. Verify the implementation through tests.

The DSA must be:

```text
Visible
Executable
Correct
Testable
Explainable
Integrated
```

---

# 79. Final Master Instruction

When making implementation decisions, prioritize:

```text
1. Correctness
2. Exact DSA scope
3. Documentation consistency
4. Architectural consistency
5. Testability
6. Railway-domain relevance
7. Honest performance measurement
8. Maintainability
9. UI quality
10. Demonstration quality
```

Never sacrifice algorithmic correctness for visual effects.

Never sacrifice architectural correctness for implementation speed.

Never fabricate results, benchmarks, datasets, tests, or integrations.

Never add unrelated features simply to make the project appear larger.

Keep RailSync centered on:

```text
RAILWAY OPERATIONS
       +
M1 String Algorithms
       +
M2 Suffix Structures
       +
M3 Dynamic Programming
       +
M4 Network Flow
       +
M5 NP-Completeness
       +
M6 Randomized / Parallel Algorithms
```

**Build only what is required, build it correctly, test it, integrate it, and make it explainable.**