# RailSync – Development Roadmap

**Document:** `docs/08-development-roadmap.md`  
**Project:** RailSync – Intelligent Railway Operations Platform  
**Development Environment:** Antigravity IDE  
**Backend:** Java + Spring Boot  
**Frontend:** React + Vite + Tailwind CSS  
**Database:** PostgreSQL  
**Architecture:** Modular Monolith  
**Status:** Implementation Roadmap

---

# 1. Purpose

This roadmap converts the RailSync architecture into an executable development sequence.

The goal is not:

```text
Build everything → hope it works
```

The goal is:

```text
Specification
    ↓
Foundation
    ↓
Vertical Slice
    ↓
M1
    ↓
M2
    ↓
M3
    ↓
M4
    ↓
M5
    ↓
M6
    ↓
Integration
    ↓
Testing
    ↓
Demo
```

Every phase must leave the repository in a working state.

---

# 2. Frozen Project Scope

Before implementation begins, the following scope is frozen.

## M1

```text
KMP
Z-Function
Rabin-Karp
Aho-Corasick
```

## M2

```text
Suffix Array
SA-IS
LCP
Kasai
Suffix Automaton
```

## M3

```text
Levenshtein
Damerau-Levenshtein
Bitmask DP
Matrix-Chain Multiplication
Optimal BST
```

## M4

```text
Ford-Fulkerson
Edmonds-Karp
Dinic
Bipartite Matching
König's Theorem
Max-Flow Min-Cut
```

## M5

```text
SAT
3-SAT
3-SAT → CLIQUE
CLIQUE → INDEPENDENT SET
INDEPENDENT SET → VERTEX COVER
Vertex-Cover 2-Approximation
```

## M6

```text
Randomized QuickSort
Reservoir Sampling
Miller-Rabin
Blelloch Scan
Parallel Reduce
Brent's Theorem
```

No additional DSA algorithm should be added merely to make the project appear larger.

---

# 3. Phase Overview

```text
PHASE 0  → Repository + Agent Setup
PHASE 1  → Backend Foundation
PHASE 2  → Database + Seed System
PHASE 3  → First Vertical Slice
PHASE 4  → M1 String Algorithms
PHASE 5  → M2 Suffix Structures
PHASE 6  → M3 Dynamic Programming
PHASE 7  → M4 Network Flow
PHASE 8  → M5 NP-Completeness
PHASE 9  → M6 Randomized + Parallel
PHASE 10 → Real-Time Event System
PHASE 11 → Analytics + Benchmarking
PHASE 12 → Full Integration
PHASE 13 → Hardening + Demo
```

---

# 4. Phase 0 – Repository and Agent Setup

## Objective

Create the project skeleton before writing business logic.

Structure:

```text id="wq8p3m"
RailSync/
├── AGENTS.md
├── README.md
├── docs/
├── .agents/
├── frontend/
├── backend/
├── database/
└── scripts/
```

---

# 5. Antigravity Initial Agent

The first agent should inspect:

```text id="r7m4z1"
AGENTS.md
docs/01-project-specification.md
docs/02-system-architecture.md
docs/03-database-design.md
docs/04-api-contract.md
docs/05-algorithm-contracts.md
docs/06-ui-architecture.md
docs/07-testing-strategy.md
```

It must not begin implementing M1–M6 yet.

Its first responsibility is validating that the repository structure matches the documentation.

---

# 6. Create Antigravity Rules

Create:

```text id="c4x8n2"
.agents/rules/
```

Recommended rules:

```text id="k7q3m5"
01-project-scope.md
02-architecture.md
03-dsa-implementation.md
04-api-contract.md
05-testing.md
06-ui.md
07-git.md
```

The rules should reinforce the root `AGENTS.md`.

---

# 7. Create Antigravity Skills

Create:

```text id="z8p2v4"
.agents/skills/
```

Recommended:

```text id="m3x7q9"
railsync-dsa
railsync-testing
railsync-ui
railsync-database
railsync-research
```

Each skill should contain:

```text
SKILL.md
```

with the specific workflow and quality requirements for that responsibility.

---

# 8. Phase 1 – Backend Foundation

## Objective

Create a clean Spring Boot foundation.

Build:

```text id="n4c7x1"
Spring Boot application
PostgreSQL configuration
Flyway
REST configuration
global exception handling
validation
logging
health endpoint
CORS
```

Health endpoint:

```text id="p9w3m2"
GET /api/health
```

Response:

```json id="4x1z8q"
{
  "success": true,
  "data": {
    "status": "UP"
  }
}
```

---

# 9. Backend Package Structure

Create:

```text id="v8q5m1"
backend/src/main/java/.../

├── config/
├── common/
├── domain/
├── repository/
├── service/
├── controller/
├── algorithm/
├── event/
└── exception/
```

Do not begin with a giant package containing everything.

---

# 10. Phase 1 – Frontend Foundation

Create:

```text id="x3n7k2"
React + Vite
Tailwind
React Router
Framer Motion
API client
AppShell
Sidebar
Topbar
routing
error boundary
loading states
```

Initial routes:

```text id="p1m8v4"
/dashboard
/dsa
/operations
```

At this stage they can contain placeholder states.

---

# 11. Phase 1 Checkpoint

Do not proceed until:

```text id="q6z2m9"
backend starts
frontend starts
frontend → backend connection works
PostgreSQL connection works
health endpoint works
CI build works
```

---

# 12. Phase 2 – Database

Implement Flyway migrations.

Order:

```text id="k4m7x1"
V1__create_station.sql
V2__create_platform.sql
V3__create_train.sql
V4__create_route.sql
V5__create_route_stop.sql
V6__create_trip.sql
V7__create_stop_time.sql
V8__create_passenger.sql
V9__create_ticket.sql
V10__create_service_alert.sql
V11__create_maintenance_record.sql
V12__create_railway_event.sql
V13__create_railway_document.sql
V14__create_network_edge.sql
V15__create_algorithm_execution.sql
```

Migration numbering can be adjusted if the implementation requires it, but the dependency order must remain valid.

---

# 13. Seed Generator

Create:

```text id="h5r9c3"
scripts/seed/
```

Seed data must be:

```text id="b7m2q8"
synthetic
deterministic
internally consistent
repeatable
```

Use a fixed seed.

---

# 14. Seed Data Relationships

The seed process must produce valid relationships:

```text id="u8n4p2"
Station
  ↓
Platform

Train
  ↓
Route
  ↓
Route Stops
  ↓
Trip
  ↓
Stop Times

Passenger
  ↓
Ticket
  ↓
Trip
```

Events must reference valid railway entities where applicable.

---

# 15. Phase 2 Checkpoint

Verify:

```text id="y2k8m4"
Flyway migrations pass
seed completes
foreign keys valid
repository queries work
dataset reproducible
```

Do not proceed if database integrity is unstable.

---

# 16. Phase 3 – First Vertical Slice

This is the most important architectural checkpoint.

Implement:

```text id="x7p4m2"
Station
   ↓
Repository
   ↓
Service
   ↓
Controller
   ↓
GET /api/stations
   ↓
React Stations Page
```

Then:

```text id="a5n8q1"
KMP
 ↓
KMP Service
 ↓
POST /api/m1/kmp
 ↓
React KMP Page
```

---

# 17. Vertical Slice Acceptance

The first slice must prove:

```text id="f3m9w2"
Database
✓

Spring Boot
✓

REST
✓

React
✓

Algorithm engine
✓

Testing
✓
```

If this slice fails, do not proceed to all six modules.

---

# 18. Phase 4 – M1

Implement M1 sequentially:

```text id="v2q8n5"
KMP
 ↓
Z-Function
 ↓
Rabin-Karp
 ↓
Aho-Corasick
```

For every algorithm:

```text id="m9x4c7"
Algorithm
↓
Unit tests
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
Integration test
```

---

# 19. M1 Integration

Build:

```text id="s5k8m2"
M1 Dashboard
```

with:

```text id="p7x3q9"
KMP
Z
Rabin-Karp
Aho-Corasick
```

Then add:

```text id="g2n6v4"
M1 comparison
```

---

# 20. M1 Railway Integration

Connect:

```text id="k8m3q5"
KMP
→ station/train search

Z
→ repeated operational patterns

Rabin-Karp
→ ticket/booking code search

Aho-Corasick
→ alert keyword detection
```

M1 is not complete until these adapters work.

---

# 21. M1 Checkpoint

Required:

```text id="j4p9x2"
All M1 unit tests pass
Cross-validation passes
All APIs pass
All visualizers work
Railway integration works
Benchmarks work
Trace works
```

Tag:

```text id="c7m2v8"
milestone/m1-complete
```

---

# 22. Phase 5 – M2

Implementation order:

```text id="q3n7m1"
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

---

# 23. M2 Dependency Order

Important:

```text id="b8x2p4"
Suffix Array
     ↓
LCP / Kasai
```

SA-IS is independently implemented.

Suffix Automaton is independently implemented.

Do not create circular dependencies.

---

# 24. M2 Railway Integration

Connect to:

```text id="y7m3k9"
Railway Documents
```

Workflow:

```text id="p4q8n2"
Document
 ↓
Index
 ↓
Suffix Structure
 ↓
Substring Analysis
 ↓
Repeated Description
```

---

# 25. M2 Checkpoint

Verify:

```text id="x8c4m1"
Suffix Array correctness
SA-IS == reference suffix array
LCP correctness
Kasai correctness
Suffix Automaton correctness
Document integration
Visualization
Benchmarks
```

Tag:

```text id="j2n7p5"
milestone/m2-complete
```

---

# 26. Phase 6 – M3

Implementation order:

```text id="m5q8x2"
Levenshtein
 ↓
Damerau-Levenshtein
 ↓
Bitmask DP
 ↓
Matrix Chain
 ↓
Optimal BST
```

---

# 27. M3 Railway Integration

```text id="p7m3k8"
Levenshtein
→ station/passenger-data correction

Damerau
→ transposition correction

Bitmask
→ small service combinations

Matrix Chain
→ ordered data-operation optimization

Optimal BST
→ frequently accessed record model
```

---

# 28. M3 Input Limits

Explicit limits must be implemented for:

```text id="q1x6n4"
Bitmask
SAT-like exponential behavior
large DP matrices
large trace outputs
```

The UI must communicate limits clearly.

---

# 29. M3 Checkpoint

Verify:

```text id="k8p2m5"
DP matrices
edge cases
reference solutions
API contract
visualizations
railway adapters
```

Tag:

```text id="f3n7q1"
milestone/m3-complete
```

---

# 30. Phase 7 – M4

M4 should be developed carefully because it has the highest graph complexity.

Order:

```text id="v8m2q4"
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

---

# 31. Shared M4 Graph Engine

Create:

```text id="m4-common"
algorithm/m4/common/
```

Containing:

```text id="x9q3p7"
Graph
GraphNode
GraphEdge
ResidualGraph
ResidualEdge
FlowResult
```

The shared graph model must be tested before implementing the flow algorithms.

---

# 32. M4 Railway Integration

Primary scenarios:

```text id="p4n8m2"
Passenger Flow
      ↓
Flow Network
      ↓
Dinic
      ↓
Bottleneck

Train
      ↓
Eligible Platforms
      ↓
Bipartite Matching
      ↓
Platform Assignment
```

---

# 33. M4 Verification

For every flow network:

```text id="s6k2x8"
Ford-Fulkerson
=
Edmonds-Karp
=
Dinic
```

Then:

```text id="m8p4q1"
Max Flow
=
Min Cut
```

For matching:

```text id="v2n7c5"
Maximum Matching
=
Minimum Vertex Cover
```

---

# 34. M4 Checkpoint

Tag:

```text id="r5x8m2"
milestone/m4-complete
```

Only after all mathematical validation passes.

---

# 35. Phase 8 – M5

M5 is a theory-heavy module and should not be implemented as a generic "AI scheduler."

Order:

```text id="q7m3x9"
SAT
 ↓
3-SAT
 ↓
3-SAT → CLIQUE
 ↓
CLIQUE → INDEPENDENT SET
 ↓
INDEPENDENT SET → VERTEX COVER
 ↓
Vertex-Cover 2-Approximation
```

---

# 36. M5 Transformation Pipeline

The UI and backend should expose:

```text id="p8n2k4"
3-SAT
  ↓
CLIQUE
  ↓
INDEPENDENT SET
  ↓
VERTEX COVER
```

Each transformation must be inspectable.

---

# 37. M5 Railway Context

Use small railway scheduling/conflict examples.

Example conceptual variables:

```text id="f4x8m1"
T1_P1
T1_P2
T2_P1
```

Meaning:

```text
Train 1 assigned Platform 1
```

Constraints can express:

```text id="v7q3n9"
train cannot occupy two platforms
platform cannot host conflicting trains
required service assignment
```

Do not claim the bounded implementation solves unrestricted real-world railway scheduling.

---

# 38. M5 Checkpoint

Required:

```text id="x3m7p2"
SAT correctness
3-SAT correctness
reduction correctness
complement graph correctness
vertex-cover relationship
2-approximation coverage
approximation bound tests
visual transformation
```

Tag:

```text id="n8q4v1"
milestone/m5-complete
```

---

# 39. Phase 9 – M6

Order:

```text id="p3x7m9"
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
Brent
```

---

# 40. M6 Randomness Rules

Every randomized implementation accepts:

```text id="m8q2v5"
seed
```

Tests must use deterministic seeds.

UI should expose:

```text id="x4n7p1"
Seed
Randomize Seed
```

---

# 41. M6 Railway Integration

```text id="v5m2q8"
QuickSort
→ train/service record ranking

Reservoir Sampling
→ continuous event sampling

Miller-Rabin
→ numerical algorithm demonstration

Blelloch Scan
→ cumulative passenger/event statistics

Parallel Reduce
→ railway data aggregation

Brent
→ parallel execution analysis
```

---

# 42. M6 Checkpoint

Verify:

```text id="j7x3p4"
randomness
reproducibility
parallel correctness
scan correctness
reduce correctness
benchmarking
visualization
```

Tag:

```text id="c9m2v8"
milestone/m6-complete
```

---

# 43. Phase 10 – Real-Time Event System

After M1–M6 are individually stable, implement:

```text id="x5q8n3"
Railway Event Generator
        ↓
Event Persistence
        ↓
Event Publisher
        ↓
WebSocket
        ↓
React Live Feed
```

---

# 44. Event Types

Initial:

```text id="v3m7k1"
TRAIN_ARRIVAL
TRAIN_DEPARTURE
TRAIN_DELAY
TRAIN_CANCELLED
PLATFORM_CHANGE
SERVICE_ALERT
MAINTENANCE_UPDATE
PASSENGER_FLOW_UPDATE
```

---

# 45. Event Simulation Controls

Create:

```text id="a8p2x6"
/operations/events
```

Controls:

```text id="z7m4q1"
Generate Event
Event Type
Train
Station
Delay
Publish
```

This is useful for live demos.

---

# 46. Phase 11 – Analytics

Create:

```text id="m5x8p2"
/analytics
```

Sections:

```text id="q3n7v1"
Railway Metrics
Algorithm Benchmarks
Flow Analysis
Event Analytics
```

---

# 47. Benchmark Dashboard

Display:

```text id="x8m2k4"
Algorithm
Input Size
Execution Time
Operation Count
Theoretical Complexity
Seed
```

Graphs should be generated from actual benchmark records.

---

# 48. Phase 12 – Full Integration

At this point:

```text id="r7p3m8"
Frontend
Backend
Database
M1
M2
M3
M4
M5
M6
WebSocket
Analytics
```

must be integrated.

---

# 49. Integration Checklist

Run:

```text id="q4x8n2"
Database migration
↓
Seed data
↓
Backend
↓
Frontend
↓
WebSocket
↓
All API tests
↓
All frontend tests
↓
E2E tests
```

---

# 50. Phase 13 – Hardening

Focus on:

```text id="m8v2p5"
error handling
input limits
performance
responsive UI
accessibility
security
logging
documentation
```

No new major features should be introduced here.

---

# 51. Demo Mode

Create a predictable demonstration environment.

Seed:

```text id="x7q3m1"
known stations
known trains
known alerts
known documents
known graph
known events
```

Use deterministic random seeds.

This makes the final presentation reproducible.

---

# 52. Recommended Demo Sequence

The final demonstration should tell one coherent story.

```text id="d1"
1. Dashboard
```

Show:

```text
stations
trains
alerts
live events
network
```

Then:

```text id="d2"
2. Station Search
```

Demonstrate KMP.

Then:

```text id="d3"
3. Alert Analysis
```

Demonstrate Aho-Corasick.

Then:

```text id="d4"
4. Document Analysis
```

Demonstrate suffix structures.

Then:

```text id="d5"
5. Station Name Correction
```

Demonstrate Levenshtein/Damerau.

Then:

```text id="d6"
6. Platform Assignment
```

Demonstrate bipartite matching.

Then:

```text id="d7"
7. Passenger Flow
```

Demonstrate Dinic and min-cut.

Then:

```text id="d8"
8. Scheduling Constraints
```

Demonstrate SAT and reductions.

Then:

```text id="d9"
9. Live Event Stream
```

Demonstrate Reservoir Sampling.

Finally:

```text id="d10"
10. Analytics
```

Show actual algorithm benchmarks.

---

# 53. Git Strategy

Recommended branches:

```text id="r5n8x2"
main
develop

feature/foundation
feature/database
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

Agents should not directly modify `main`.

---

# 54. Commit Convention

Use:

```text id="k7p3m1"
feat:
fix:
test:
refactor:
docs:
chore:
perf:
```

Examples:

```text id="2x8q4m"
feat(m1): implement KMP search

test(m1): add KMP overlapping match tests

feat(m4): add Dinic max flow

test(m4): verify max flow min cut equality

feat(ui): add KMP visualizer
```

---

# 55. Agent Worktree Strategy

Parallel agents should use isolated worktrees when modifying overlapping areas.

Example:

```text id="p8m2v7"
worktree/m1
worktree/m2
worktree/m3
```

After validation:

```text id="f4x9q1"
merge
↓
run integration tests
↓
continue
```

Never merge several untested algorithm branches simultaneously.

---

# 56. Safe Parallelization

After the foundation is stable, these can be parallelized:

```text id="j3m7x8"
M1 Agent
M2 Agent
M3 Agent
M4 Agent
M5 Agent
M6 Agent
```

But only if:

```text id="v5q2n9"
common contracts
API contracts
algorithm interfaces
testing conventions
```

are already frozen.

---

# 57. What Must Remain Sequential

These dependencies should not be parallelized prematurely:

```text id="x8m4p2"
Foundation
   ↓
Database
   ↓
Vertical Slice
```

and:

```text id="n7q3v5"
M4 Graph Model
   ↓
M4 Algorithms
```

and:

```text id="p2x8m4"
M2 Suffix Array
   ↓
LCP / Kasai integration
```

---

# 58. Antigravity Agent Prompt Pattern

Every implementation agent should receive a bounded task.

Template:

```text id="8m3q7v"
You are implementing [TASK] in RailSync.

Read:
- AGENTS.md
- docs/02-system-architecture.md
- docs/04-api-contract.md
- docs/05-algorithm-contracts.md
- docs/07-testing-strategy.md

Scope:
[EXACT FILES / MODULE]

Requirements:
[EXACT ALGORITHM / FEATURE]

Do not:
- modify unrelated modules
- replace the algorithm with a library
- change API contracts without documentation
- fabricate benchmark data
- add unrelated features

Before finishing:
1. implement
2. test
3. integrate
4. run relevant tests
5. report changed files
6. report test results
7. report remaining issues
```

---

# 59. Algorithm Agent Completion Report

Every agent must report:

```text id="v7p3m1"
Task:
Algorithm:

Files changed:

Implementation:
Tests:
API:
UI:
Benchmark:
Trace:

Tests executed:
Tests passed:
Tests failed:

Known limitations:

Contract changes:
```

This prevents agents from saying merely:

```text id="f8q2m4"
"Done."
```

---

# 60. Integration Agent

After each module, run a dedicated integration agent.

Responsibilities:

```text id="x4m8q2"
inspect changes
run tests
verify API contract
verify frontend contract
verify architecture
detect scope creep
fix integration issues
```

It should not redesign the module.

---

# 61. Final Audit Agent

Before release, create a final audit agent.

It checks:

```text id="q3v7m1"
Project scope
Architecture
Database
API
Algorithms
Tests
UI
Performance
Security
Documentation
```

The final audit should produce:

```text id="m8x2p4"
PASS
FAIL
WARNING
```

for each category.

---

# 62. Final Repository Structure

Expected final structure:

```text id="j7q3m9"
RailSync/
│
├── AGENTS.md
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
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── ...
│
├── frontend/
│   ├── src/
│   ├── package.json
│   └── ...
│
├── database/
│   └── migrations/
│
├── scripts/
│   ├── seed/
│   └── benchmark/
│
└── e2e/
```

---

# 63. Release Gates

RailSync cannot move to final release until:

### Gate 1 – Foundation

```text id="gate1"
Build ✓
Database ✓
API ✓
Frontend ✓
```

### Gate 2 – M1

```text id="gate2"
Algorithms ✓
Tests ✓
UI ✓
Integration ✓
```

### Gate 3 – M2

```text id="gate3"
Algorithms ✓
Tests ✓
Documents ✓
UI ✓
```

### Gate 4 – M3

```text id="gate4"
DP ✓
Tests ✓
UI ✓
```

### Gate 5 – M4

```text id="gate5"
Flow ✓
Matching ✓
Mathematical verification ✓
```

### Gate 6 – M5

```text id="gate6"
SAT ✓
Reductions ✓
Approximation ✓
```

### Gate 7 – M6

```text id="gate7"
Randomized ✓
Parallel ✓
Reproducibility ✓
```

### Gate 8 – Full System

```text id="gate8"
WebSocket ✓
Analytics ✓
E2E ✓
CI ✓
Documentation ✓
```

---

# 64. Scope-Creep Protection

The following should **not** be added unless explicitly approved:

```text id="no_scope"
Blockchain
Cryptocurrency
Generic AI chatbot
Facial recognition
IoT hardware integration
Unrelated recommendation systems
Microservices
Kubernetes
Complex authentication
Payment gateway
Unrelated DSA algorithms
Real-world railway control
Real passenger PII
```

The project is already technically substantial.

More features do not automatically make it better.

---

# 65. Research Rule

If an agent needs external information about railway operations:

```text id="r6m2x8"
research
    ↓
document source
    ↓
update docs
    ↓
implement
```

Do not invent railway operational rules.

If a feature is only an academic simulation, label it clearly as such.

---

# 66. Documentation Rule

Whenever implementation changes an architectural decision:

```text id="p4x7m2"
code change
     ↓
documentation update
     ↓
tests
```

Documentation must not describe features that do not actually exist.

---

# 67. Final Project Completion Test

Run the following from a clean checkout:

```text id="k3m8q1"
1. Install dependencies
2. Configure environment
3. Start PostgreSQL
4. Run migrations
5. Seed database
6. Start Spring Boot
7. Start React
8. Open dashboard
9. Execute M1 algorithm
10. Execute M2 algorithm
11. Execute M3 algorithm
12. Execute M4 algorithm
13. Execute M5 algorithm
14. Execute M6 algorithm
15. Trigger live event
16. Verify WebSocket update
17. Run benchmark
18. Run automated tests
```

If this sequence works from a clean environment, RailSync has reached a meaningful release candidate.

---

# 68. Final Development Principle

The project should be developed as:

```text id="q8m4x1"
ONE SYSTEM
```

not:

```text id="z3p7v9"
36 unrelated algorithm demos
```

The relationship must remain:

```text id="railway"
RAILWAY PROBLEM
       ↓
DATA
       ↓
DSA ALGORITHM
       ↓
RESULT
       ↓
VISUALIZATION
       ↓
MEASUREMENT
       ↓
OPERATIONAL INSIGHT
```

That relationship is the core identity of RailSync.