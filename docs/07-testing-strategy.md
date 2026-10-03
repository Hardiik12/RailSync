# RailSync – Testing Strategy

**Document:** `docs/07-testing-strategy.md`  
**Project:** RailSync – Intelligent Railway Operations Platform  
**Backend:** Java + Spring Boot  
**Frontend:** React + Vite  
**Database:** PostgreSQL  
**Testing Philosophy:** Correctness first, then integration, then performance

---

# 1. Purpose

Testing in RailSync must verify more than whether the application starts.

The test system must prove:

```text
DSA Correctness
      ↓
Algorithm Integration
      ↓
Railway-Domain Correctness
      ↓
API Correctness
      ↓
Frontend Correctness
      ↓
Real-Time Event Correctness
      ↓
Performance
      ↓
End-to-End Reliability
```

The project must never use visual success as proof that an algorithm is correct.

---

# 2. Testing Layers

RailSync uses seven testing layers.

```text id="m1h1ju"
Layer 1  → Algorithm Unit Tests
Layer 2  → Algorithm Cross-Validation
Layer 3  → Service Tests
Layer 4  → API Integration Tests
Layer 5  → Database Tests
Layer 6  → Frontend Tests
Layer 7  → End-to-End Tests
```

Performance and stress testing operate across these layers.

---

# 3. Testing Stack

## Backend

Recommended:

```text id="c8ckcq"
JUnit 5
Spring Boot Test
Mockito
MockMvc
Testcontainers
AssertJ
```

## Frontend

Recommended:

```text id="w8as6c"
Vitest
React Testing Library
jsdom
```

Browser verification:

```text id="5cxrga"
Playwright
```

The exact versions should follow the project's dependency setup rather than being independently invented by agents.

---

# 4. Test Structure

Backend:

```text id="h44r0c"
backend/
└── src/
    ├── main/
    └── test/
        └── java/
            └── .../
                ├── algorithm/
                │   ├── m1/
                │   ├── m2/
                │   ├── m3/
                │   ├── m4/
                │   ├── m5/
                │   └── m6/
                │
                ├── service/
                ├── controller/
                ├── repository/
                └── integration/
```

Frontend:

```text id="rldc2t"
frontend/
└── src/
    ├── components/
    │   └── __tests__/
    ├── pages/
    │   └── __tests__/
    ├── hooks/
    │   └── __tests__/
    └── api/
        └── __tests__/
```

End-to-end:

```text id="19wj5c"
e2e/
├── dashboard.spec.js
├── operations.spec.js
├── m1.spec.js
├── m2.spec.js
├── m3.spec.js
├── m4.spec.js
├── m5.spec.js
├── m6.spec.js
└── realtime.spec.js
```

---

# 5. Test Naming Convention

Use:

```text id="q1q7un"
should_<expected_behavior>_when_<condition>
```

Example:

```java id="x9f6ad"
shouldFindOverlappingMatchesWhenPatternRepeats()
```

Avoid:

```text id="1n8v7w"
test1()
testAlgorithm()
works()
```

Tests should communicate what they verify.

---

# 6. M1 – String Algorithm Testing

---

## 6.1 KMP Tests

Required tests:

### Basic search

```text id="4h3cz5"
text = "railway"
pattern = "rail"
```

Expected:

```text
[0]
```

### Pattern at end

```text id="84r3m9"
text = "centralstation"
pattern = "station"
```

### Pattern absent

```text id="2i4i5h"
text = "railway"
pattern = "airport"
```

Expected:

```text
[]
```

### Pattern longer than text

```text id="l6p6b5"
text = "abc"
pattern = "abcdef"
```

### Empty text

### Empty pattern

Behavior must be explicitly defined and tested.

### Repeated pattern

```text id="w8z2ax"
text = "AAAAA"
pattern = "AAA"
```

Expected:

```text
[0,1,2]
```

This verifies overlapping matches.

### Railway data

```text id="v11x8y"
"Train 12727 arrives at Vijayawada Junction"
```

Search:

```text
"12727"
```

---

# 7. KMP LPS Tests

Test:

```text id="lps"
"ABABCABAB"
```

against the expected LPS array.

Also test:

- all unique characters
- all identical characters
- partial repetitions
- empty pattern

The search result must remain correct regardless of LPS construction.

---

# 8. Z-Function Tests

Required:

```text id="7csg9v"
empty string
single character
all identical characters
no repetition
repeated prefix
```

Example:

```text
"AAAAA"
```

Expected Z values:

```text
[0,4,3,2,1]
```

Also verify pattern-search behavior against KMP.

---

# 9. Rabin-Karp Tests

Required:

- exact match
- no match
- multiple matches
- overlapping matches
- pattern longer than text
- repeated characters
- hash collision handling

Critical test:

```text id="f5f0p2"
hash(pattern) == hash(window)
but
pattern != window
```

The algorithm must continue character verification.

---

# 10. Aho-Corasick Tests

Required:

```text id="6y8d3w"
single keyword
multiple keywords
overlapping keywords
nested keywords
no keywords
duplicate keywords
empty keyword handling
```

Railway example:

```text id="1af9m0"
Text:
"Train delayed due to signal failure at platform 4."
```

Keywords:

```text
delay
signal
platform
failure
```

All applicable matches must be returned.

---

# 11. M1 Cross-Validation

For exact single-pattern searches:

```text id="a4w5q3"
KMP
Z-Function
Rabin-Karp
```

must produce equivalent match positions.

Example test:

```text
sameText
samePattern
```

then:

```text
KMP.matches
==
Z.matches
==
RabinKarp.matches
```

This is one of the most important automated correctness checks.

---

# 12. M2 – Suffix Structure Testing

---

# 13. Suffix Array Tests

Required:

```text id="7rnj8b"
empty string
single character
repeated characters
unique characters
repeated substrings
```

Known test:

```text
"banana"
```

Expected suffix array:

```text
[5, 3, 1, 0, 4, 2]
```

The test should verify the complete array.

---

# 14. Suffix Array Property Tests

For every adjacent pair:

```text id="c31c8c"
suffix[i] <= suffix[i+1]
```

must hold lexicographically.

Also verify:

```text id="0z6q8g"
every text position occurs exactly once
```

This catches many implementation errors.

---

# 15. SA-IS Tests

SA-IS must be tested against the independently implemented suffix-array result.

For the same input:

```text id="j4b0l1"
SA_IS(text)
==
SuffixArray(text)
```

Test:

- short strings
- repetitive strings
- random strings
- railway documents

This does not make SA-IS a wrapper around the other implementation; it validates correctness.

---

# 16. LCP Tests

Given:

```text id="z0at4b"
text
suffixArray
```

verify:

```text id="5tx3aj"
lcp[i]
=
actual LCP between adjacent suffixes
```

Use both:

- known examples
- generated random strings

---

# 17. Kasai Tests

Kasai must produce the same LCP result as the independent LCP validation.

Test:

```text id="o4h2eh"
Kasai(text, suffixArray)
```

against:

```text
naiveLCP(text, suffixArray)
```

The naive implementation is allowed **only inside tests** as a reference implementation.

---

# 18. Suffix Automaton Tests

Required:

```text id="z9qkq0"
contains existing substring
does not contain substring
single-character query
full-text query
repeated substring
```

Example:

```text
text = "railwaystation"
query = "station"
```

Expected:

```text
true
```

---

# 19. M2 Document Tests

Seed documents must include repeated operational phrases.

Example:

```text id="4v3u7x"
"Train service delayed due to signal maintenance."
```

and:

```text
"Passenger service delayed due to signal maintenance."
```

The system should be able to identify shared substrings.

---

# 20. M3 – Dynamic Programming Testing

---

# 21. Levenshtein Tests

Required:

```text id="f0g8t2"
"" → ""
"abc" → ""
"" → "abc"
"abc" → "abc"
"abc" → "abd"
```

Known examples:

```text id="wq6p7k"
kitten → sitting
```

Expected distance:

```text
3
```

Railway test:

```text id="4g44p1"
Vijaywada
→
Vijayawada
```

Expected distance:

```text
1
```

---

# 22. Damerau-Levenshtein Tests

Critical test:

```text id="k7j0wq"
"ab"
→
"ba"
```

Damerau distance should recognize the transposition.

Compare:

```text id="1qhy3m"
Levenshtein("ab", "ba")
Damerau("ab", "ba")
```

to verify the distinction.

---

# 23. DP Matrix Validation

For small strings, verify every DP cell against a trusted reference calculation.

This is stronger than testing only the final distance.

Example:

```text id="6u2h2f"
source = "abc"
target = "adc"
```

Verify:

```text
dp[0][0]
dp[1][1]
dp[2][2]
...
```

---

# 24. Bitmask DP Tests

Required:

```text id="x4r3pu"
0 items
1 item
2 items
small complete set
maximum configured size
```

Verify:

```text id="8f7p9m"
result
state count
selected mask
```

For small cases, compare against brute force.

Example:

```text
n = 4
```

Brute-force all subsets in the test.

Production code must still use the Bitmask DP implementation.

---

# 25. Matrix Chain Tests

Known example:

```text id="6qf2ca"
dimensions = [10,20,30]
```

Expected:

```text
6000
```

Also test:

```text
single matrix
two matrices
increasing dimensions
large dimensions
```

Verify that:

```text
minimumMultiplications
```

matches an independent brute-force/reference implementation for small chains.

---

# 26. Optimal BST Tests

Test:

```text id="n3o7n9"
single key
two keys
equal frequencies
skewed frequencies
```

For small inputs, compare against exhaustive tree generation.

Verify:

```text id="b7qvgu"
returned tree
+
expected search cost
```

both agree.

---

# 27. M4 – Network Flow Testing

---

# 28. Common Graph Validation

Every graph test must verify:

- no invalid node references
- capacities are non-negative
- source exists
- sink exists
- source ≠ sink
- duplicate edge behavior is defined
- empty graph behavior is defined

---

# 29. Ford-Fulkerson Tests

Use small known networks.

Verify:

```text id="a6tqgp"
max flow
edge flows
flow conservation
capacity constraints
```

For every intermediate/result edge:

```text
0 <= flow <= capacity
```

For non-source/non-sink nodes:

```text
incoming flow == outgoing flow
```

---

# 30. Edmonds-Karp Tests

Run the same graph used for Ford-Fulkerson.

Verify:

```text id="j8d5a4"
FordFulkerson.maxFlow
==
EdmondsKarp.maxFlow
```

Also verify that Edmonds-Karp uses BFS-generated augmenting paths.

---

# 31. Dinic Tests

Run the same graph:

```text id="l4z8a0"
Ford-Fulkerson
Edmonds-Karp
Dinic
```

Expected:

```text
all max-flow values equal
```

Test:

- zero-capacity edges
- disconnected graph
- single edge
- multiple paths
- bottleneck network

---

# 32. Flow Conservation Test

For every computed flow:

```text id="w4p2m3"
source:
outflow - inflow = maxFlow

sink:
inflow - outflow = maxFlow

internal node:
inflow = outflow
```

This is mandatory.

---

# 33. Bipartite Matching Tests

Verify:

```text id="i4w0fz"
each left node matched at most once
each right node matched at most once
every selected pair is an allowed edge
```

Test:

- perfect matching
- partial matching
- no matching
- isolated nodes
- multiple valid matchings

---

# 34. Railway Platform Matching Tests

Generate:

```text id="9v52gk"
Trains
Platforms
Eligibility edges
```

Then verify:

```text id="u3m7m0"
no platform assigned to two trains
no train assigned to two platforms
all assignments are eligible
```

This is the primary M4 railway integration test.

---

# 35. König Tests

For every test graph:

```text id="6i2m7s"
maximum matching size
==
minimum vertex cover size
```

Also verify every graph edge touches at least one selected cover vertex.

---

# 36. Max-Flow Min-Cut Tests

Verify:

```text id="c0g1e6"
maxFlow == minCutCapacity
```

Additionally:

```text id="z2v3qk"
every source-side → sink-side crossing edge
belongs to the cut
```

and the cut is actually separating source from sink.

---

# 37. M5 – NP-Completeness Testing

M5 requires especially strong validation because reductions can appear plausible while being mathematically incorrect.

---

# 38. SAT Tests

Required:

```text id="q8u4si"
empty formula
single clause
satisfiable formula
unsatisfiable formula
contradictory clauses
```

Example:

```text
(A)
(!A)
```

Expected:

```text
UNSAT
```

---

# 39. 3-SAT Tests

Verify every accepted clause contains exactly three literals.

Test:

```text id="m3v4yo"
satisfiable instance
unsatisfiable instance
repeated variables
negated literals
```

---

# 40. 3-SAT → CLIQUE Reduction Tests

For small formulas, verify the fundamental property:

```text id="6s6r13"
3-SAT formula is satisfiable
iff
generated graph contains clique of target size
```

Do this by brute force in tests.

For small graphs:

```text
enumerate all assignments
```

and:

```text
enumerate all vertex subsets
```

Then compare.

This is a critical mathematical correctness test.

---

# 41. CLIQUE → Independent Set Tests

Verify:

```text id="70y2f5"
G has clique of size k
iff
complement(G) has independent set of size k
```

Use exhaustive enumeration for small graphs.

---

# 42. Independent Set → Vertex Cover Tests

Verify:

```text id="2n5z9b"
S is independent
iff
V − S is vertex cover
```

For every generated small graph:

```text
IndependentSetValidator(S)
==
VertexCoverValidator(V-S)
```

---

# 43. Vertex Cover 2-Approximation Tests

Verify:

```text id="p7k5xn"
all graph edges are covered
```

For small graphs, compute exact optimum using brute force.

Then verify:

```text id="g1m5w9"
approximationSize <= 2 * optimumSize
```

Do not require:

```text
approximationSize == optimumSize
```

because the algorithm is an approximation algorithm.

---

# 44. M6 – Randomized and Parallel Testing

---

# 45. Randomized QuickSort Tests

Required:

```text id="d1xqk4"
empty array
single element
sorted array
reverse sorted array
duplicates
random array
negative numbers
```

Verify:

```text id="b3p0za"
result is sorted
```

and:

```text
multiset(result) == multiset(input)
```

---

# 46. Randomized QuickSort Determinism

With:

```text id="t4x8c6"
seed = 42
```

run twice.

Expected:

```text
same sorted output
same pivot sequence
```

This ensures reproducible debugging.

---

# 47. Reservoir Sampling Tests

Test:

```text id="y7f5j9"
sample size = 0
sample size = 1
sample size = stream size
sample size > stream size
```

Validate:

```text
sample contains only stream elements
sample has no duplicates where the stream itself has unique elements
sample size is correct
```

---

# 48. Reservoir Sampling Statistical Test

Run many trials with a small controlled stream.

Example:

```text id="8jv1s4"
stream = [1,2,3,4,5]
sampleSize = 1
```

Each item should appear approximately uniformly over many trials.

The test should use statistical tolerance rather than exact counts.

Do not write tests expecting one specific random result without supplying a seed.

---

# 49. Miller-Rabin Tests

Required known cases:

```text id="v4h8n7"
2
3
5
7
11
97
```

Composite:

```text
4
9
15
21
25
```

Test:

```text id="b9x0j5"
large known prime
large known composite
```

---

# 50. Miller-Rabin Determinism

When a seed is supplied:

```text id="k8f3v2"
same input
+
same seed
+
same rounds
```

must produce the same witness sequence.

---

# 51. Blelloch Scan Tests

Input:

```text id="h1n5q8"
[1,2,3,4]
```

Expected exclusive scan:

```text
[0,1,3,6]
```

Test:

```text id="g0v7m3"
empty
one element
power of two
non-power of two
zeros
negative values
```

If the implementation requires padding to a power of two, that behavior must be tested.

---

# 52. Parallel Reduce Tests

Test:

```text id="7d6n1w"
SUM
MIN
MAX
```

Compare:

```text id="q5p9ea"
parallel result
==
sequential reference result
```

Run with:

```text
workerCount = 1
2
4
8
```

The result must remain identical.

---

# 53. Brent's Theorem Tests

Verify the calculated estimates against the defined mathematical model.

For:

```text id="l8q3y7"
W = 100
S = 20
P = 4
```

the implementation must consistently apply the documented formula.

Test:

```text
P = 1
P > W
P large
W = S
```

---

# 54. Service Layer Testing

Algorithm services should be tested separately from controllers.

Example:

```text id="k6x4w2"
KMPController
    ↓
KMPService
    ↓
KMPAlgorithm
```

Tests should verify that:

- input reaches algorithm correctly
- result is mapped correctly
- benchmark metadata is attached
- trace configuration is respected
- exceptions become appropriate service errors

---

# 55. REST API Tests

Every algorithm endpoint requires at least:

```text id="9b5g3d"
valid request
invalid request
empty input
boundary input
oversized input where applicable
trace enabled
trace disabled
```

Example:

```text id="2f6k0e"
POST /api/m1/kmp
```

Test:

```text
200
```

for valid input.

Test:

```text
400
```

for invalid input.

---

# 56. API Contract Tests

Verify the exact response structure.

For example KMP:

```text id="5t4z5n"
success
data
data.matches
data.matchCount
data.lps
meta
```

Tests should fail if an agent accidentally renames:

```text
matches
```

to:

```text
results
```

without updating the contract.

---

# 57. Error Contract Tests

Verify:

```json id="3n2f8w"
{
  "success": false,
  "error": {
    "code": "...",
    "message": "...",
    "details": {}
  },
  "meta": {
    "requestId": "..."
  }
}
```

Every API failure should maintain the same envelope.

---

# 58. Database Testing

Use a disposable PostgreSQL environment for integration tests.

Recommended:

```text id="g7l5h0"
Testcontainers PostgreSQL
```

Test:

```text
migrations
constraints
foreign keys
indexes
seed data
repository queries
transactions
```

Do not run destructive automated tests against the developer's permanent database.

---

# 59. Database Integrity Tests

Verify:

```text id="9k3s7a"
train references valid route
route_stop references valid route/station
platform references valid station
trip references valid train
stop_time references valid trip/station
ticket references valid passenger/trip
alert references valid station/train where applicable
event references valid entities
```

---

# 60. Synthetic Dataset Tests

The seed dataset must be:

```text id="n6q1rz"
deterministic
internally consistent
repeatable
non-sensitive
```

Running:

```text id="5xw2jr"
seed database
```

twice from a clean database should produce the same logical dataset.

---

# 61. Railway Domain Integration Tests

---

## Scenario 1 – Station Search

```text id="0o1y4d"
Database
   ↓
Station API
   ↓
Station Search Service
   ↓
KMP
   ↓
Matching stations
```

Verify the correct stations are returned.

---

# 62. Scenario 2 – Alert Keyword Detection

```text id="98d3pj"
Service Alert
   ↓
Aho-Corasick
   ↓
Detected keywords
   ↓
Alert UI
```

Verify:

- alert stored
- keywords detected
- matches returned
- UI renders them

---

# 63. Scenario 3 – Document Analysis

```text id="1r2c8e"
Railway Document
   ↓
Suffix Array / SA-IS
   ↓
LCP
   ↓
Repeated Phrase
```

Verify the returned repeated substring is actually present.

---

# 64. Scenario 4 – Station Name Correction

```text id="f3z6nb"
Misspelled station
       ↓
Levenshtein
       ↓
Candidate correction
       ↓
Station database
```

Verify the calculated distance.

---

# 65. Scenario 5 – Platform Assignment

```text id="1n9z4d"
Train eligibility
       ↓
Bipartite Graph
       ↓
Matching
       ↓
Platform assignment
```

Verify:

```text
no duplicate platform
no duplicate train
only eligible assignments
```

---

# 66. Scenario 6 – Passenger Flow

```text id="3p2h8k"
Railway network
       ↓
Dinic
       ↓
Maximum flow
       ↓
Bottleneck
```

Verify:

```text
flow conservation
capacity constraints
max-flow/min-cut equality
```

---

# 67. Scenario 7 – Event Stream

```text id="7f8y2j"
Railway Events
       ↓
Reservoir Sampling
       ↓
Sample
       ↓
Analytics
```

Verify the sample contains only events from the stream.

---

# 68. WebSocket Tests

Test:

```text id="8r4z6q"
client connects
server accepts connection
event published
client receives event
multiple events received in order where ordering is guaranteed
disconnect handled
reconnect handled
```

Example:

```text id="5n2d8k"
TRAIN_DELAY
```

should reach subscribed frontend clients.

---

# 69. Frontend Component Testing

Test reusable components:

```text id="p7w5x9"
Button
Card
Table
Badge
Modal
Tabs
MetricCard
AlgorithmHeader
TraceViewer
```

Focus on behavior rather than internal implementation details.

---

# 70. Frontend Algorithm Tests

Each algorithm page should verify:

```text id="y3q7m4"
input accepted
Run button works
loading displayed
API called
result displayed
trace displayed
benchmark displayed
error displayed
reset works
```

Example:

```text id="5v9r2m"
KMPPage
```

should not directly execute KMP.

It should call the API client.

---

# 71. Visualization Tests

Test that a trace causes the expected visual state.

Example:

```text id="z8n4c2"
KMP trace step:
textIndex = 5
patternIndex = 2
```

should highlight those positions.

The visualization test does not need to prove KMP correctness again; backend algorithm tests handle that.

---

# 72. Browser End-to-End Tests

Use a real browser for major user journeys.

---

# 73. E2E Journey 1 – Dashboard

```text id="3a7k8w"
Open RailSync
      ↓
Dashboard loads
      ↓
KPIs appear
      ↓
Network graph appears
      ↓
Events appear
```

---

# 74. E2E Journey 2 – KMP

```text id="7s2m9p"
Open DSA Lab
      ↓
Open M1
      ↓
Open KMP
      ↓
Enter text
      ↓
Enter pattern
      ↓
Run
      ↓
Result appears
      ↓
Trace appears
      ↓
Benchmark appears
```

---

# 75. E2E Journey 3 – Platform Matching

```text id="4n6x8q"
Operations
      ↓
Platforms
      ↓
Analyze Assignment
      ↓
M4 Matching
      ↓
Run
      ↓
Assignments appear
```

---

# 76. E2E Journey 4 – Document Analysis

```text id="h7q3m5"
Documents
      ↓
Select document
      ↓
Analyze
      ↓
Build suffix structure
      ↓
LCP/Kasai
      ↓
Repeated phrase displayed
```

---

# 77. E2E Journey 5 – Live Event

```text id="p8v1z4"
Dashboard
      ↓
Simulate Event
      ↓
TRAIN_DELAY
      ↓
WebSocket
      ↓
Live Event Feed updates
```

---

# 78. Performance Testing

Performance testing must answer:

```text id="2b5k8x"
How does the implementation behave as input size grows?
```

Not:

```text id="6c7d1m"
Which algorithm is "best"?
```

Measure:

```text id="8s4q2z"
input size
execution time
operation count
memory where measurable
```

---

# 79. M1 Performance

Test increasing text sizes:

```text id="4v7q1a"
100
500
1,000
5,000
10,000
50,000
```

Compare:

```text
KMP
Z
Rabin-Karp
```

Aho-Corasick should vary:

```text
text length
keyword count
total keyword length
```

---

# 80. M2 Performance

Measure:

```text id="6d9w3n"
text length
suffix-array construction time
SA-IS construction time
LCP construction time
```

Do not run huge inputs merely to produce impressive numbers.

Use controlled benchmark sizes.

---

# 81. M3 Performance

Measure:

```text id="w3n7k9"
edit-distance string lengths
bitmask item count
matrix-chain length
Optimal BST key count
```

For exponential algorithms, input growth must be deliberately bounded.

---

# 82. M4 Performance

Measure:

```text id="2c8h4s"
node count
edge count
capacity
flow
```

Compare the implementations on identical generated graphs.

Do not infer general superiority from a single benchmark.

---

# 83. M5 Performance

M5 inputs must remain small enough for meaningful demonstration.

Track:

```text id="z9h1m5"
variables
clauses
generated graph vertices
generated graph edges
execution time
```

The UI should clearly communicate that growth can become exponential.

---

# 84. M6 Performance

Measure:

### QuickSort

```text id="q6w2p4"
array size
comparisons
swaps
execution time
```

### Reservoir Sampling

```text id="a1m8x3"
stream size
sample size
execution time
```

### Parallel Reduce

```text id="v4n7c2"
input size
worker count
execution time
```

### Scan

```text id="b8r3y6"
input size
levels
execution time
```

---

# 85. Benchmark Reproducibility

Every benchmark record should store:

```text id="5y8k1n"
algorithm
input size
seed where applicable
execution time
operation count
timestamp
```

For randomized algorithms:

```text id="x7p3m9"
seed
```

must be recorded.

---

# 86. Benchmark Environment

Benchmarks must display:

```text id="e6w4p1"
environment
input size
algorithm
seed
```

because measured runtime depends on the execution environment.

Do not claim:

```text id="m5v2r8"
"Algorithm X is universally faster."
```

from local measurements.

---

# 87. Regression Testing

Whenever an algorithm bug is fixed:

```text id="q8x4k1"
1. create a failing test
2. fix implementation
3. verify test passes
4. retain test permanently
```

Never delete a regression test simply because the bug was fixed.

---

# 88. CI Pipeline

Recommended pipeline:

```text id="v7p2q6"
Push / Pull Request
        ↓
Compile
        ↓
Unit Tests
        ↓
Integration Tests
        ↓
Frontend Tests
        ↓
Build
        ↓
E2E Tests
        ↓
Deploy
```

A failed algorithm unit test must block deployment.

---

# 89. Required CI Checks

Backend:

```text id="1a4v8s"
compile
unit tests
integration tests
static analysis
```

Frontend:

```text id="j9m2x5"
lint
unit tests
build
```

Full system:

```text id="q5n8c3"
E2E smoke tests
```

---

# 90. Test Data Strategy

Use three categories.

## A. Handcrafted datasets

Small examples for:

```text id="v4c7s1"
edge cases
known answers
viva demonstrations
```

## B. Generated deterministic datasets

For:

```text id="k8m2q6"
performance
stress
integration
```

Use fixed seeds.

## C. Production-like synthetic datasets

For:

```text id="p6x1z9"
dashboard
railway operations
event streams
document analysis
```

No real passenger PII.

---

# 91. Test Dataset Sizes

Initial integration dataset:

```text id="x4n7p2"
Stations             30–50
Platforms            100+
Trains               50+
Routes               50+
Route Stops          300+
Trips                100+
Stop Times           500+
Passengers           5,000+
Tickets              10,000+
Alerts               500+
Maintenance          100+
Railway Events       20,000+
Documents            100+
Network Edges        100+
```

These are development targets rather than claims about real railway scale.

---

# 92. Test Isolation

Tests must not depend on execution order.

Bad:

```text id="c5v8r2"
testA inserts data
testB assumes testA ran
```

Good:

```text id="h7m3x9"
each test establishes its own required state
```

Database tests should reset or isolate state appropriately.

---

# 93. Randomized Test Strategy

Randomized tests must be reproducible.

Use:

```text id="y8q4m2"
seed = known value
```

when debugging.

Property-based/randomized testing may use many generated inputs, but a failing seed must be recorded so the case can be reproduced.

---

# 94. Algorithm Reference Implementations

For testing only, simple reference implementations may be used.

Examples:

```text id="s6v2j8"
naive string search
naive LCP
brute-force subset search
brute-force vertex cover
brute-force independent set
brute-force small SAT assignment
sequential reduce
```

These are **test oracles**, not production implementations.

This distinction must be maintained.

---

# 95. Security Testing

At minimum:

```text id="m8p3z1"
malformed JSON
oversized inputs
invalid IDs
negative capacities
invalid graph references
invalid DP dimensions
trace abuse
```

Verify that the backend rejects invalid input safely.

No stack traces should be returned to the client.

---

# 96. Failure Recovery Testing

Test:

```text id="w2c7x4"
database unavailable
algorithm exception
WebSocket disconnect
invalid event
backend unavailable
frontend API timeout
```

The UI should show useful error states.

---

# 97. Observability Testing

Important backend operations should log:

```text id="n6q4b8"
request ID
endpoint
algorithm
execution time
success/failure
```

Do not log:

```text id="f3y8q1"
real passenger sensitive information
database credentials
secrets
```

---

# 98. Final Test Matrix

| Module | Unit | Integration | API | UI | E2E | Cross-Validation |
|---|---:|---:|---:|---:|---:|---:|
| M1 | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| M2 | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| M3 | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| M4 | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| M5 | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| M6 | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |

---

# 99. Minimum Acceptance Criteria

RailSync cannot be called complete if:

```text id="j2f8m4"
an algorithm returns incorrect results
```

or:

```text id="c7x3p1"
the frontend displays fabricated algorithm output
```

or:

```text id="m4q9z2"
the API contract differs between frontend and backend
```

or:

```text id="v6k1r8"
the railway use case is not connected to the algorithm
```

or:

```text id="s9n3w5"
randomized results cannot be reproduced when seeded
```

or:

```text id="x1p7c4"
approximation output is incorrectly described as optimal
```

---

# 100. Final Definition of Done

The RailSync test system is complete when:

- [ ] all 30+ required algorithms have unit tests
- [ ] all algorithm edge cases are covered
- [ ] cross-validation exists where mathematically applicable
- [ ] railway-domain integration tests pass
- [ ] all API endpoints have contract tests
- [ ] PostgreSQL integration tests pass
- [ ] WebSocket tests pass
- [ ] frontend component tests pass
- [ ] major E2E workflows pass
- [ ] deterministic seed data works
- [ ] randomized algorithms support reproducible seeds
- [ ] performance benchmarks run on controlled inputs
- [ ] no fake benchmark results exist
- [ ] M5 approximation/reduction properties are verified
- [ ] CI blocks broken builds
- [ ] regression tests exist for discovered bugs
- [ ] browser verification passes
- [ ] final project demo can be executed from a clean environment

---

# 101. Final Testing Philosophy

RailSync should be able to answer four questions for every algorithm:

```text id="6q8m2v"
1. Does it produce the correct answer?

2. Does it implement the required DSA algorithm?

3. Does it solve the railway-domain problem assigned to it?

4. Can we demonstrate and measure it reliably?
```

Only when all four answers are yes should that algorithm be considered production-ready within the academic RailSync system.