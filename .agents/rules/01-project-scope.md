---
trigger: always_on
---

# RailSync — Project Scope Rule

## Purpose

This rule defines the fixed scope of RailSync.

All agents must treat the project scope as controlled.

The project is an:

> **Academic Railway Operations Simulation & Analytics Platform demonstrating Data Structures and Algorithms through railway-domain applications.**

The railway domain provides the context.

The DSA modules provide the computational core.

---

# 1. Frozen DSA Scope

RailSync contains exactly six primary DSA modules.

```text
M1 — String Algorithms
M2 — Suffix Structures
M3 — Advanced Dynamic Programming
M4 — Network Flow
M5 — NP-Completeness and Approximation
M6 — Randomized and Parallel Algorithms
```

Do not add unrelated algorithms without explicit approval.

---

# 2. M1 Scope

Required:

```text
KMP
Z-Function
Rabin-Karp
Aho-Corasick
```

Railway use cases:

```text
Train-number search
Station-name search
Service-alert search
Ticket/booking-code search
Operational pattern detection
Multi-keyword alert detection
```

---

# 3. M2 Scope

Required:

```text
Suffix Array
SA-IS
LCP
Kasai
Suffix Automaton
```

Railway use cases:

```text
Railway-document indexing
Service-information search
Repeated service-description detection
Substring search
Document similarity analysis
```

### Explicit restriction

Do not add Suffix Tree unless explicitly requested.

---

# 4. M3 Scope

Required:

```text
Levenshtein Distance
Damerau-Levenshtein Distance
Bitmask DP
Matrix-Chain Multiplication
Optimal Binary Search Tree
```

Railway use cases:

```text
Station-name correction
Passenger-data correction suggestions
Small-scale route/service combinations
Railway-data operation optimization
Frequently accessed station/service analysis
```

Bitmask DP must remain bounded to small instances.

---

# 5. M4 Scope

Required:

```text
Ford-Fulkerson
Edmonds-Karp
Dinic
Bipartite Matching
König's Theorem
Max-Flow Min-Cut
```

Railway use cases:

```text
Passenger flow
Railway network capacity
Platform assignment
Service/platform conflict analysis
Bottleneck identification
```

---

# 6. M5 Scope

Required:

```text
SAT
3-SAT
3-SAT → CLIQUE
CLIQUE → INDEPENDENT SET
INDEPENDENT SET → VERTEX COVER
Vertex Cover 2-Approximation
```

Railway use cases:

```text
Scheduling constraints
Service conflicts
Platform constraints
Conflict-graph analysis
```

M5 is an academic complexity and decision-support module.

Do not describe it as an optimal real-world railway scheduler.

---

# 7. M6 Scope

Required:

```text
Randomized QuickSort
Reservoir Sampling
Miller-Rabin
Blelloch Scan
Parallel Reduce
Brent's Theorem
```

Railway use cases:

```text
Train/service ranking
Continuous railway-event sampling
Numerical/primality demonstration
Cumulative passenger statistics
Railway-data aggregation
Parallel-performance analysis
```

Miller-Rabin is a numerical algorithm demonstration.

Do not invent an artificial claim that railway operations inherently require primality testing.

---

# 8. Core Railway Domain

The application may contain the following domain entities:

```text
Station
Platform
Train
Route
Route Stop
Trip
Stop Time
Passenger
Ticket
Service Alert
Maintenance Record
Railway Event
Railway Document
Network Edge
```

These entities exist to provide realistic inputs and operational context for the DSA modules.

---

# 9. Core Functional Areas

RailSync may provide:

### Railway Operations

- train information
- station information
- platform information
- route information
- trip information
- passenger information
- ticket information
- maintenance information
- service alerts

### DSA Laboratory

- algorithm execution
- visualization
- trace
- complexity
- operation counts
- measured execution time
- benchmark comparison
- railway use-case explanation

### Documents

- railway-document storage
- document indexing
- substring analysis
- repeated-text analysis

### Events

- simulated railway events
- event persistence
- live event delivery
- event-stream analysis

### Analytics

- algorithm metrics
- operational statistics
- event statistics
- benchmark history

---

# 10. Data Scope

Use deterministic synthetic railway data.

The application must not depend on real railway passenger data.

Do not use real:

```text
Aadhaar numbers
Phone numbers
Addresses
Passenger identifiers
Ticket identifiers
Confidential railway records
```

Synthetic data must be reproducible.

---

# 11. External Data Scope

The core application must run without external railway APIs.

Do not make:

```text
Indian Railways API
External railway API
Live railway feed
External timetable API
```

a required dependency.

External imports may be added later only if they do not change the core architecture.

---

# 12. Real-Time Scope

RailSync may simulate real-time railway operations.

Supported examples:

```text
Train arrival
Train departure
Train delay
Platform change
Service alert
Maintenance event
Passenger event
```

These are simulated events.

RailSync must not attempt to control real trains, signaling systems, platforms, or railway infrastructure.

---

# 13. What RailSync Is Not

Do not transform RailSync into:

- an official railway platform
- a railway dispatch system
- a train-control system
- a signaling system
- a safety-critical railway system
- an official Indian Railways application
- a real passenger-management system

The project is an academic simulation and analytics platform.

---

# 14. Explicitly Out of Scope

Unless the user explicitly approves them, do not add:

```text
Blockchain
Cryptocurrency
Generic AI chatbot
Facial recognition
Unrelated machine learning
IoT
Payment gateway
Complex authentication
Microservices
Kubernetes
Unrelated recommendation systems
Unrelated DSA
Social-media features
E-commerce functionality
Real railway control
```

Do not add features simply because they make the project appear more advanced.

---

# 15. AI Scope

AI must not be introduced as a generic project feature.

Do not add:

```text
AI chatbot
AI assistant
LLM railway assistant
AI scheduling
AI recommendations
AI prediction
```

unless explicitly requested and justified against the project scope.

The primary intelligence of RailSync is its algorithmic engine.

---

# 16. DSA-First Principle

Every major feature should answer:

> **Which railway problem does this solve, and which required DSA algorithm demonstrates it?**

Preferred:

```text
Railway Problem
      ↓
Required DSA
      ↓
Algorithm Implementation
      ↓
Railway Service
      ↓
API
      ↓
Visualization
```

Avoid:

```text
Random Feature
      ↓
Find a DSA algorithm to attach afterward
```

---

# 17. Feature Approval Rule

Before implementing a feature that is not clearly covered by the project specification:

1. Determine whether it belongs to the railway domain.
2. Determine whether it supports M1–M6.
3. Check the architecture documents.
4. Check whether it is already planned in the roadmap.
5. If it changes project scope, stop and request approval.

Do not silently expand scope.

---

# 18. Documentation Consistency

When a feature is added or removed, the relevant documentation must remain consistent.

Do not allow:

```text
Project Specification ≠ Architecture
Architecture ≠ API
API ≠ Implementation
Implementation ≠ Tests
```

If a documented feature does not exist yet, do not claim that it is implemented.

---

# 19. Scope Priority

When deciding between multiple implementation options, prioritize:

```text
1. Required DSA functionality
2. Railway-domain relevance
3. Correctness
4. Testability
5. Existing architecture
6. Visualization
7. Performance
8. Optional enhancements
```

Optional enhancements must never interfere with required functionality.

---

# 20. Scope Completion

The project scope is considered fulfilled when all required M1–M6 algorithms are:

```text
Implemented
Tested
Integrated
Exposed through the backend
Connected to railway use cases
Available through the frontend
Documented
```

The goal is not to maximize the number of features.

The goal is to make the required RailSync system **complete, correct, explainable, and demonstrable**.