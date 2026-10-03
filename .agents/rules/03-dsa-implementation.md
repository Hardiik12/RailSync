---
trigger: always_on
---

# RailSync — DSA Implementation Rule

## 1. Purpose

This rule defines the mandatory implementation standards for all RailSync DSA algorithms.

The DSA layer is the computational core of RailSync.

All required algorithms must be:

- manually implemented
- correct
- testable
- explainable
- domain-independent
- measurable
- traceable where appropriate
- integrated through the documented service layer

Do not replace required algorithms with library implementations.

---

# 2. Frozen Algorithm Inventory

RailSync contains exactly the following algorithm scope.

## M1 — String Algorithms

1. KMP
2. Z-Function
3. Rabin-Karp
4. Aho-Corasick

## M2 — Suffix Structures

5. Suffix Array
6. SA-IS
7. LCP
8. Kasai
9. Suffix Automaton

## M3 — Advanced Dynamic Programming

10. Levenshtein Distance
11. Damerau-Levenshtein Distance
12. Bitmask DP
13. Matrix-Chain Multiplication
14. Optimal BST

## M4 — Network Flow

15. Ford-Fulkerson
16. Edmonds-Karp
17. Dinic
18. Bipartite Matching
19. König's Theorem
20. Max-Flow Min-Cut

## M5 — NP-Completeness and Approximation

21. SAT
22. 3-SAT
23. 3-SAT → CLIQUE
24. CLIQUE → Independent Set
25. Independent Set → Vertex Cover
26. Vertex Cover 2-Approximation

## M6 — Randomized and Parallel Algorithms

27. Randomized QuickSort
28. Reservoir Sampling
29. Miller-Rabin
30. Blelloch Scan
31. Parallel Reduce
32. Brent's Theorem

No additional primary DSA algorithm may be introduced without explicit approval.

---

# 3. Manual Implementation Requirement

Required algorithms must be implemented manually.

Do not use:

- Java library implementations of the required algorithm
- Apache Commons implementations
- third-party DSA libraries
- external algorithm APIs
- database functions as algorithm replacements
- JavaScript implementations in the frontend
- AI/ML models as algorithm replacements

Allowed libraries include infrastructure libraries for:

- Spring Boot
- PostgreSQL
- JSON serialization
- testing
- logging
- HTTP
- WebSocket
- UI
- visualization

Infrastructure libraries must not replace the required algorithm implementation.

---

# 4. Algorithm Independence

Algorithm implementations must remain independent from the railway domain.

An algorithm must not directly depend on:

- Station
- Train
- Passenger
- Ticket
- PostgreSQL
- Spring Controller
- Repository
- HTTP
- WebSocket
- React

Example:

Correct:

```text
Station data
     ↓
StationService
     ↓
String input
     ↓
KMP