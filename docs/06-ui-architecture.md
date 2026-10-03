# RailSync – UI Architecture

**Document:** `docs/06-ui-architecture.md`  
**Project:** RailSync – Intelligent Railway Operations Platform  
**Frontend:** React + Vite + Tailwind CSS + Framer Motion  
**Backend:** Spring Boot  
**Design Direction:** Dark, modern railway operations control center  
**Status:** Implementation Contract

---

# 1. Purpose

This document defines the complete frontend architecture for RailSync.

The frontend has two responsibilities:

1. Present the railway operations system.
2. Make the underlying DSA algorithms visible, understandable, and demonstrable.

The UI must therefore avoid becoming a normal CRUD dashboard.

The core experience should communicate:

```text
Railway Operations
        +
Real-time Analytics
        +
DSA Algorithm Laboratory
```

---

# 2. Product UI Philosophy

RailSync should look like an intelligent railway operations platform rather than a college CRUD project.

The visual language should be:

- dark
- technical
- structured
- data-dense but readable
- futuristic without excessive decoration
- responsive
- animation used for understanding, not decoration

Preferred visual direction:

```text
Dark background
        +
Cool neutral surfaces
        +
Railway-inspired accent colors
        +
Clear status indicators
        +
Algorithm visualizations
```

Avoid:

- excessive neon
- excessive gradients
- huge decorative graphics
- unnecessary 3D effects
- fake AI interfaces
- generic admin-template appearance

---

# 3. Application Layout

The application uses a persistent shell.

```text
┌───────────────────────────────────────────────────────────────┐
│ RailSync                         Search     Alerts   Profile  │
├──────────────┬────────────────────────────────────────────────┤
│              │                                                │
│ Dashboard    │                                                │
│              │                                                │
│ Operations   │             MAIN CONTENT                       │
│ ├ Trains     │                                                │
│ ├ Stations   │                                                │
│ ├ Routes     │                                                │
│ ├ Platforms  │                                                │
│ ├ Alerts     │                                                │
│ └ Events     │                                                │
│              │                                                │
│ DSA Lab      │                                                │
│ ├ M1 Strings │                                                │
│ ├ M2 Suffix  │                                                │
│ ├ M3 DP      │                                                │
│ ├ M4 Flow    │                                                │
│ ├ M5 NP      │                                                │
│ └ M6 Random  │                                                │
│              │                                                │
│ Analytics    │                                                │
│ Documents    │                                                │
└──────────────┴────────────────────────────────────────────────┘
```

---

# 4. Main Routes

React Router should use:

```text
/
├── /dashboard
│
├── /operations
│   ├── /trains
│   ├── /stations
│   ├── /routes
│   ├── /platforms
│   ├── /trips
│   ├── /passengers
│   ├── /tickets
│   ├── /alerts
│   ├── /maintenance
│   └── /events
│
├── /dsa
│   ├── /m1
│   ├── /m1/kmp
│   ├── /m1/z-function
│   ├── /m1/rabin-karp
│   ├── /m1/aho-corasick
│   │
│   ├── /m2
│   ├── /m2/suffix-array
│   ├── /m2/sa-is
│   ├── /m2/lcp
│   ├── /m2/kasai
│   ├── /m2/suffix-automaton
│   │
│   ├── /m3
│   ├── /m3/levenshtein
│   ├── /m3/damerau
│   ├── /m3/bitmask
│   ├── /m3/matrix-chain
│   ├── /m3/optimal-bst
│   │
│   ├── /m4
│   ├── /m4/ford-fulkerson
│   ├── /m4/edmonds-karp
│   ├── /m4/dinic
│   ├── /m4/matching
│   ├── /m4/konig
│   └── /m4/min-cut
│   │
│   ├── /m5
│   ├── /m5/sat
│   ├── /m5/3sat
│   ├── /m5/reductions
│   └── /m5/vertex-cover
│   │
│   └── /m6
│       ├── /m6/randomized-quicksort
│       ├── /m6/reservoir-sampling
│       ├── /m6/miller-rabin
│       ├── /m6/blelloch-scan
│       ├── /m6/parallel-reduce
│       └── /m6/brent
│
├── /analytics
├── /documents
└── /settings
```

---

# 5. Dashboard

Route:

```text
/dashboard
```

The dashboard should immediately communicate the current state of the simulated railway network.

---

## 5.1 KPI Cards

Display:

```text
Active Trains
Stations
Platforms
Active Alerts
Delayed Trains
Passengers
Events / Hour
Network Flow
```

Example:

```text
┌─────────────────┐
│ ACTIVE TRAINS   │
│      47         │
│ +3 from 1h ago │
└─────────────────┘
```

The values must come from backend APIs.

No fake hardcoded dashboard numbers.

---

# 6. Network Overview

Main dashboard visualization:

```text
        Station A
          │
       400 pax
          │
          ▼
        Station B ───────► Station D
          │                  │
       600 pax            300 pax
          │                  │
          ▼                  ▼
        Station C ◄──────── Station E
```

The network visualization should later connect to M4.

The user should be able to:

- zoom
- pan
- select station
- inspect capacity
- inspect flow
- identify bottleneck edges

---

# 7. Live Event Feed

Dashboard includes:

```text
LIVE EVENTS
──────────────────────────────
18:42  Train 12727 delayed
18:41  Platform 4 assigned
18:40  Train 12806 departed
18:39  Service alert created
18:37  Passenger flow updated
```

Events should arrive through:

```text
WebSocket
/ws/events
```

The frontend must not continuously poll if the WebSocket connection is available.

---

# 8. Alert Panel

Display active service alerts.

Severity:

```text
INFO
WARNING
CRITICAL
```

Example:

```text
⚠ SIGNAL FAILURE
Station: VJA
Affected services: 3
Updated: 18:41
```

Aho-Corasick can later be demonstrated directly from the alert screen.

---

# 9. Operations – Trains

Route:

```text
/operations/trains
```

Features:

- train table
- search
- status filter
- route filter
- delay filter
- train details

Table:

```text
Train No | Name | Route | Status | Delay | Next Station
```

Clicking a train opens:

```text
Train Details
├── Overview
├── Route
├── Schedule
├── Platforms
├── Events
└── Maintenance
```

---

# 10. Stations

Route:

```text
/operations/stations
```

Display:

```text
Station
City
Zone
Platforms
Active Trains
Passenger Flow
Status
```

Station details:

```text
Station Overview
      ↓
Platforms
      ↓
Incoming Trains
      ↓
Outgoing Trains
      ↓
Passenger Flow
      ↓
Alerts
```

The station search should provide an obvious pathway to demonstrating KMP.

---

# 11. Platform Management

Route:

```text
/operations/platforms
```

Display:

```text
Platform
Station
Current Train
Next Train
Status
Capacity
```

Status:

```text
AVAILABLE
OCCUPIED
RESERVED
MAINTENANCE
```

The platform screen should provide a link:

```text
Analyze Assignment
```

which opens the M4 bipartite-matching visualization.

---

# 12. Alerts

Route:

```text
/operations/alerts
```

Features:

- active alerts
- severity filtering
- station filtering
- keyword search
- alert details
- keyword detection

The alert detail page should include:

```text
Alert Text
    ↓
Detected Keywords
    ↓
Aho-Corasick
    ↓
Matched Terms
```

This directly connects the operational interface with M1.

---

# 13. Documents

Route:

```text
/documents
```

Purpose:

Provide the railway-document environment for M2.

Display:

```text
Document Title
Type
Length
Created
Indexed
Index Algorithm
```

Actions:

```text
View
Index
Search
Analyze Similarity
```

---

# 14. Document Analysis

Route:

```text
/documents/:id/analyze
```

Display:

```text
DOCUMENT
──────────────────────────

Title
Type
Characters
Words

INDEX
──────────────────────────

Suffix Array
SA-IS
LCP
Kasai
Suffix Automaton

ANALYSIS
──────────────────────────

Repeated phrases
Longest repeated substring
Substring queries
```

This page is the primary bridge between railway documents and M2.

---

# 15. DSA Laboratory

Route:

```text
/dsa
```

The DSA Lab is one of the most important parts of RailSync.

It should show all six modules.

```text
┌─────────────────────────────────────────────────────────┐
│ DSA LAB                                                 │
│ Explore the algorithms powering RailSync               │
├─────────────────────────────────────────────────────────┤
│                                                         │
│ M1 STRING ALGORITHMS                                    │
│ KMP • Z • Rabin-Karp • Aho-Corasick                    │
│                                                         │
│ M2 SUFFIX STRUCTURES                                   │
│ Suffix Array • SA-IS • LCP • Kasai • SAM               │
│                                                         │
│ M3 DYNAMIC PROGRAMMING                                 │
│ Edit Distance • Bitmask • Matrix Chain • Optimal BST   │
│                                                         │
│ M4 NETWORK FLOW                                        │
│ Ford-Fulkerson • Edmonds-Karp • Dinic • Matching       │
│                                                         │
│ M5 NP-COMPLETENESS                                     │
│ SAT • Reductions • Vertex Cover                        │
│                                                         │
│ M6 RANDOMIZED/PARALLEL                                 │
│ QuickSort • Sampling • Miller-Rabin • Scan • Reduce    │
└─────────────────────────────────────────────────────────┘
```

---

# 16. Standard Algorithm Page

Every algorithm page should use the same basic layout.

```text
┌─────────────────────────────────────────────────────────────┐
│ Algorithm Name                           [Run Algorithm]   │
├───────────────────────┬─────────────────────────────────────┤
│ INPUT                 │ VISUALIZATION                       │
│                       │                                     │
│ text                  │                                     │
│ pattern               │       algorithm animation           │
│                       │                                     │
├───────────────────────┴─────────────────────────────────────┤
│ RESULT                                                     │
├─────────────────────────────────────────────────────────────┤
│ COMPLEXITY │ BENCHMARK │ TRACE │ RAILWAY USE CASE          │
└─────────────────────────────────────────────────────────────┘
```

---

# 17. Algorithm Page Components

Reusable components:

```text
AlgorithmHeader
AlgorithmInput
RunButton
ResetButton
AlgorithmVisualization
ResultPanel
ComplexityCard
BenchmarkCard
TracePanel
RailwayUseCaseCard
ErrorMessage
ExecutionStatus
```

---

# 18. M1 UI

## M1 Dashboard

Route:

```text
/dsa/m1
```

Show:

```text
KMP
Z-Function
Rabin-Karp
Aho-Corasick
```

Each card should contain:

```text
Algorithm
Purpose
Complexity
Railway use case
Open Lab
```

---

# 19. KMP Visualizer

Route:

```text
/dsa/m1/kmp
```

Input:

```text
Text
Pattern
```

Visualization:

```text
TEXT:
T R A I N V I J A Y A W A D A

PATTERN:
          V I J A Y A
```

During animation:

```text
Current text index
Current pattern index
LPS fallback
Character comparison
Match
```

Controls:

```text
Run
Pause
Next Step
Reset
```

Metrics:

```text
Comparisons
Matches
Execution Time
```

---

# 20. Z-Function Visualizer

Display:

```text
Combined String
Z Array
Z-box
Current Position
```

Animation should highlight:

```text
L
R
Current i
Copied Z value
Explicit comparisons
```

---

# 21. Rabin-Karp Visualizer

Display:

```text
Pattern Hash
Current Window
Window Hash
Verification
Collision
```

Example:

```text
PATTERN HASH
482913

CURRENT WINDOW
TKT-1

WINDOW HASH
482913

✓ HASH MATCH
→ VERIFY CHARACTERS
```

If collision occurs:

```text
⚠ HASH COLLISION
Hash matched but characters differ.
```

---

# 22. Aho-Corasick Visualizer

This should use a trie-like visualization.

```text
                 root
              /    |    \
             d     p     s
             |     |     |
             e     l     i
             |     |     |
            lay   atform gnal
```

Show:

```text
Trie nodes
Failure links
Current character
Matched keywords
```

Railway alert example:

```text
"Train delayed because of signal failure"
```

Detected:

```text
delay
signal
```

---

# 23. M2 UI

M2 dashboard:

```text
Suffix Array
SA-IS
LCP
Kasai
Suffix Automaton
```

---

# 24. Suffix Array Visualizer

Display:

```text
Original Text
      ↓
All Suffixes
      ↓
Sorted Suffixes
      ↓
Suffix Array
```

For example:

```text
Index | Suffix
---------------------
  4   | ...
  7   | ...
  1   | ...
```

For large text, visualization must be paginated or sampled.

Never render thousands of suffixes simultaneously.

---

# 25. SA-IS Visualizer

Display the stages:

```text
1. Character classification
2. S/L classification
3. LMS positions
4. Induced sorting
5. Reduced problem
6. Final suffix array
```

This is important for viva/demo because SA-IS is substantially more complex than ordinary suffix-array construction.

---

# 26. LCP / Kasai Visualizer

Display:

```text
Suffix Array
       ↓
Adjacent Suffixes
       ↓
LCP Values
       ↓
Longest Repeated Substring
```

Highlight the repeated region in the document.

---

# 27. Suffix Automaton Visualizer

Display the automaton as a graph.

Node:

```text
State 7
len = 12
link = 3
```

Edges:

```text
a → State 9
i → State 11
```

Query:

```text
station
```

Animate the traversal.

---

# 28. M3 UI

M3 dashboard:

```text
Levenshtein
Damerau-Levenshtein
Bitmask DP
Matrix Chain
Optimal BST
```

---

# 29. Edit Distance Visualizer

Display DP matrix:

```text
        V I J A Y A W A D A
    ┌────────────────────────
 V  │ 0 1 2 3 4 5 ...
 i  │ 1 1 2 3 ...
 j  │ 2 2 1 ...
```

Animate:

```text
MATCH
INSERT
DELETE
REPLACE
TRANSPOSE
```

For railway correction:

```text
Input:
Vijaywada

Correct:
Vijayawada

Distance:
1
```

---

# 30. Bitmask DP Visualizer

Display:

```text
Items
 ↓
Binary Masks
 ↓
DP States
 ↓
Optimal Combination
```

Example:

```text
001
010
011
100
101
110
111
```

The UI must clearly state:

```text
Small-scale bounded optimization
```

so users do not interpret it as a full railway scheduling solver.

---

# 31. Matrix Chain Visualizer

Display:

```text
A1 × A2 × A3 × A4
```

Then animate parenthesization:

```text
((A1 × A2) × A3) × A4
```

Show:

```text
Matrix dimensions
DP cost table
Split table
Minimum multiplication cost
```

---

# 32. Optimal BST Visualizer

Display the generated tree:

```text
             VJA
            /   \
          BZA   MAS
          /
         SC
```

Show:

```text
Key
Frequency
Depth
Expected Search Cost
```

The page should explain that this is an algorithmic access-pattern optimization demonstration.

---

# 33. M4 UI

M4 is the most graphically important module.

Dashboard:

```text
Ford-Fulkerson
Edmonds-Karp
Dinic
Bipartite Matching
König
Max-Flow Min-Cut
```

---

# 34. Flow Network Visualizer

Graph:

```text
             500
        A ─────────► B
        │             │
      300│           400│
        ▼             ▼
        C ─────────► D
             200
```

Edge display:

```text
flow / capacity
```

Example:

```text
320 / 500
```

---

# 35. Ford-Fulkerson Animation

Show:

```text
Residual Graph
      ↓
Find Augmenting Path
      ↓
Highlight Path
      ↓
Push Flow
      ↓
Update Residual Capacities
      ↓
Repeat
```

---

# 36. Edmonds-Karp Animation

Highlight BFS layers.

```text
SOURCE
  │
  ├── Level 1
  │
  ├── Level 2
  │
  └── SINK
```

Show the chosen shortest augmenting path.

---

# 37. Dinic Visualization

Display:

```text
PHASE 1
Level Graph
     ↓
Blocking Flow

PHASE 2
Level Graph
     ↓
Blocking Flow
```

Metrics:

```text
Phases
BFS calls
DFS calls
Max Flow
```

---

# 38. Platform Matching UI

Railway-specific visualization:

```text
TRAINS                 PLATFORMS

Train 12727 ───────────── P1
     │
     └──────────────────── P2

Train 12728 ───────────── P2
```

After matching:

```text
Train 12727 → Platform 1
Train 12728 → Platform 2
```

Use clear visual distinction between:

```text
eligible edge
selected matching
unselected edge
```

---

# 39. König Visualization

Show:

```text
Maximum Matching
        ↓
Minimum Vertex Cover
```

Highlight the selected vertices.

Display:

```text
Maximum Matching = 4
Minimum Vertex Cover = 4

✓ König's theorem verified
```

---

# 40. Min-Cut Visualization

After max-flow:

```text
SOURCE SIDE
────────────

S1
S2
S3

──────── CUT ────────

T1
T2
T3

SINK SIDE
```

Cut edges should be highlighted.

Display:

```text
Max Flow
Min Cut Capacity
Critical Edges
```

---

# 41. M5 UI

M5 must visually emphasize **problem transformations**, not just a boolean answer.

Dashboard:

```text
SAT
3-SAT
3-SAT → CLIQUE
CLIQUE → INDEPENDENT SET
INDEPENDENT SET → VERTEX COVER
VERTEX COVER 2-APPROX
```

---

# 42. SAT Interface

Display:

```text
Variables
Clauses
Assignment
Satisfied / Unsatisfied
```

Example:

```text
C1: A ∨ ¬B ∨ C
C2: ¬A ∨ B ∨ C
```

Result:

```text
SATISFIABLE
```

---

# 43. Reduction Visualizer

Primary layout:

```text
SOURCE PROBLEM
      │
      │ transformation
      ▼
TARGET PROBLEM
      │
      ▼
Equivalent Property
```

For 3-SAT → CLIQUE:

```text
3-SAT Formula
      ↓
Clause/Literal Vertices
      ↓
Compatibility Edges
      ↓
Clique of size m
```

Users should be able to inspect generated vertices and edges.

---

# 44. Vertex Cover Approximation UI

Display:

```text
Original Graph
       ↓
Choose uncovered edge
       ↓
Add both endpoints
       ↓
Remove covered edges
       ↓
Repeat
```

Final:

```text
Approximate Cover
Cover Size
Theoretical Bound
```

Never display:

```text
"Optimal Solution"
```

unless an exact verification was performed.

---

# 45. M6 UI

Dashboard:

```text
Randomized QuickSort
Reservoir Sampling
Miller-Rabin
Blelloch Scan
Parallel Reduce
Brent
```

---

# 46. Randomized QuickSort Visualizer

Display array:

```text
45 12 78 3 19 42
```

Highlight selected pivot:

```text
Pivot = 42
```

Animate partitioning.

Metrics:

```text
Comparisons
Swaps
Pivot sequence
Execution time
```

Seed:

```text
Seed: 42
```

Allow:

```text
Randomize Seed
```

---

# 47. Reservoir Sampling Visualizer

Display a live stream:

```text
E1 → E2 → E3 → E4 → E5 → E6 → ...
```

Reservoir:

```text
[ E2 ][ E5 ][ E6 ]
```

When replacement occurs:

```text
E7
 ↓
Random decision
 ↓
Replace E2
```

This gives a strong visual demonstration of streaming algorithms.

---

# 48. Miller-Rabin UI

Display:

```text
Number
Rounds
Witnesses
```

Example:

```text
n = 1000000007

Round 1 ✓
Round 2 ✓
Round 3 ✓
...
Round 10 ✓

Probably Prime
```

For a composite:

```text
Witness found
→ Composite
```

---

# 49. Blelloch Scan Visualizer

Show the tree-like computation.

```text
              50
           /      \
         30        20
        /  \      /  \
      10   20    15   5
```

Then:

```text
UPSweep
   ↓
Root = identity
   ↓
DOWNSWEEP
   ↓
Exclusive Scan
```

---

# 50. Parallel Reduce Visualizer

Show worker partitions:

```text
Worker 1 → [10, 20] → 30
Worker 2 → [15, 5]  → 20

              ↓

            50
```

Display:

```text
Workers
Partial Results
Final Result
```

---

# 51. Brent Visualization

Display processor scaling:

```text
Processors     Estimated Work
1              ███████████████
2              ████████
4              ████
8              ██
16             █
```

Also show:

```text
Work W
Span S
Processors P
T(P)
```

The chart should be based on the actual API response.

---

# 52. Shared Visualization Components

Create:

```text
frontend/src/components/algorithms/
```

Structure:

```text id="5f8b6c"
algorithms/
├── AlgorithmHeader.jsx
├── AlgorithmInput.jsx
├── AlgorithmControls.jsx
├── AlgorithmResult.jsx
├── ComplexityCard.jsx
├── BenchmarkCard.jsx
├── TraceViewer.jsx
├── ExecutionTimeline.jsx
├── ArrayVisualizer.jsx
├── StringVisualizer.jsx
├── DPMatrix.jsx
├── GraphVisualizer.jsx
├── TreeVisualizer.jsx
├── TrieVisualizer.jsx
├── AutomatonVisualizer.jsx
├── FlowNetwork.jsx
├── MatchingVisualizer.jsx
├── ReductionVisualizer.jsx
└── StreamVisualizer.jsx
```

---

# 53. Shared UI Components

General components:

```text id="1wqckg"
components/
├── layout/
│   ├── AppShell
│   ├── Sidebar
│   ├── Topbar
│   └── PageContainer
│
├── ui/
│   ├── Button
│   ├── Card
│   ├── Badge
│   ├── Input
│   ├── Select
│   ├── Modal
│   ├── Tabs
│   ├── Table
│   ├── Tooltip
│   ├── Skeleton
│   └── EmptyState
│
├── railway/
│   ├── TrainStatusBadge
│   ├── StationCard
│   ├── AlertCard
│   ├── EventFeed
│   ├── PlatformCard
│   └── NetworkMap
│
└── analytics/
    ├── MetricCard
    ├── MetricChart
    ├── BenchmarkTable
    └── PerformanceChart
```

---

# 54. State Management

Use React state for local UI state.

Use a shared application state mechanism for:

```text id="oh8kvf"
current railway events
selected station
selected train
algorithm execution state
theme
```

Recommended separation:

```text
server state
    ↓
API/data layer

UI state
    ↓
React state/context

algorithm execution state
    ↓
algorithm-specific hooks
```

Do not create a massive global store for every piece of application state.

---

# 55. Algorithm Hooks

Create reusable hooks:

```text id="pmqzgc"
useAlgorithm
useAlgorithmTrace
useBenchmark
useWebSocketEvents
useStations
useTrains
useAlerts
useDocuments
```

Example:

```javascript id="3kgp9k"
const {
  run,
  result,
  loading,
  error,
  trace
} = useAlgorithm("kmp");
```

---

# 56. Animation Rules

Framer Motion may be used for:

- page transitions
- card transitions
- algorithm-step transitions
- graph node movement
- result reveal
- alert appearance

Do not animate everything.

Algorithm animation should prioritize:

```text
clarity > decoration
```

---

# 57. Responsive Design

Desktop is the primary environment because RailSync is an operations/analytics application.

Still support:

```text
desktop
tablet
mobile
```

On mobile:

```text
sidebar → drawer
multi-column dashboard → single column
large graphs → horizontally scrollable container
tables → responsive cards where necessary
```

Algorithm visualizations should never overflow the entire viewport.

---

# 58. Loading States

Every API-driven screen requires loading states.

Example:

```text
Loading stations...
```

Use skeletons rather than blank pages.

Algorithm execution:

```text
Preparing input...
Running KMP...
Rendering trace...
```

---

# 59. Error States

Example:

```text
┌──────────────────────────────────┐
│ Unable to execute algorithm      │
│                                  │
│ Input exceeds configured limit.  │
│                                  │
│ [Modify Input] [Try Again]       │
└──────────────────────────────────┘
```

Do not expose backend stack traces.

---

# 60. Empty States

Example:

```text
No active service alerts.

The railway event stream currently
contains no active alerts.
```

Avoid generic:

```text
"No data found."
```

when a more useful explanation is possible.

---

# 61. Accessibility

The UI must include:

- keyboard navigation
- visible focus states
- semantic buttons
- labels for inputs
- accessible chart descriptions where practical
- sufficient contrast
- status information not conveyed by color alone

For example:

```text
● CRITICAL
```

should also include the word `CRITICAL`, not rely only on red.

---

# 62. Performance Rules

The frontend must not render massive algorithm datasets directly.

Examples:

```text
100,000 suffixes
1,000,000 graph edges
20,000 event records
```

must be:

```text
paginated
virtualized
aggregated
sampled
```

where appropriate.

Trace data must also respect backend limits.

---

# 63. DSA Module Navigation

Each module page should show:

```text
← Back to DSA Lab

M4 · Network Flow

[Overview]
[Algorithms]
[Visualization]
[Benchmarks]
```

Within the module:

```text
Ford-Fulkerson
Edmonds-Karp
Dinic
Matching
König
Min-Cut
```

The active algorithm should be clearly indicated.

---

# 64. Algorithm Comparison

Each module should eventually have a comparison screen.

Example M4:

```text
Algorithm       Max Flow    Time       Operations
-------------------------------------------------
Ford-Fulkerson  850         ...        ...
Edmonds-Karp    850         ...        ...
Dinic           850         ...        ...
```

Important:

The frontend must display measured results returned by the backend.

It must not invent rankings.

Theoretical complexity should appear in a separate column.

---

# 65. Railway ↔ DSA Cross-Linking

The UI should constantly reinforce why each algorithm exists.

Examples:

### Station Search

```text
Search stations
       ↓
Exact search
       ↓
"KMP Search"
```

### Alerts

```text
Alert
 ↓
Aho-Corasick
 ↓
Detected keywords
```

### Documents

```text
Document
 ↓
Suffix Index
 ↓
Repeated descriptions
```

### Platform Assignment

```text
Eligible trains
 ↓
Bipartite Matching
 ↓
Platform assignment
```

### Network

```text
Passenger flow
 ↓
Dinic
 ↓
Bottleneck analysis
```

### Event Stream

```text
Continuous events
 ↓
Reservoir Sampling
 ↓
Representative sample
```

---

# 66. Analytics Page

Route:

```text
/analytics
```

Sections:

```text
Algorithm Performance
Railway Network Statistics
Passenger Flow
Event Statistics
Alert Statistics
```

Algorithm performance:

```text
Algorithm
Input Size
Execution Time
Operations
Complexity
```

Charts:

```text
Input Size vs Execution Time
Input Size vs Operations
Flow Capacity
Event Rate
```

Charts must use backend-produced data.

---

# 67. Benchmark Lab

Dedicated route:

```text
/analytics/benchmarks
```

Allow:

```text
Select module
Select algorithm
Select dataset
Run benchmark
Compare results
```

Example:

```text
M1

[KMP]
[Rabin-Karp]
[Run]

Input Size:
100
500
1000
5000
10000
```

The system should collect actual measurements.

---

# 68. Global Search

Topbar search should eventually support:

```text
Trains
Stations
Routes
Alerts
Documents
```

It may use the appropriate domain endpoint.

The DSA Lab should separately expose the algorithm implementation used.

This maintains the distinction between:

```text
production application search
```

and:

```text
DSA demonstration
```

---

# 69. Frontend Folder Structure

```text id="h3wq3c"
frontend/
├── src/
│
├── app/
│   ├── App.jsx
│   ├── router.jsx
│   └── providers.jsx
│
├── api/
│   ├── client.js
│   ├── stationsApi.js
│   ├── trainsApi.js
│   ├── alertsApi.js
│   ├── documentsApi.js
│   ├── eventsApi.js
│   ├── m1Api.js
│   ├── m2Api.js
│   ├── m3Api.js
│   ├── m4Api.js
│   ├── m5Api.js
│   └── m6Api.js
│
├── components/
│   ├── layout/
│   ├── ui/
│   ├── railway/
│   ├── algorithms/
│   └── analytics/
│
├── pages/
│   ├── dashboard/
│   ├── operations/
│   ├── dsa/
│   │   ├── m1/
│   │   ├── m2/
│   │   ├── m3/
│   │   ├── m4/
│   │   ├── m5/
│   │   └── m6/
│   ├── analytics/
│   └── documents/
│
├── hooks/
├── context/
├── utils/
├── constants/
├── styles/
└── main.jsx
```

---

# 70. UI Implementation Order

Do not build every page simultaneously.

Recommended sequence:

```text
1. App Shell
      ↓
2. Sidebar + Topbar
      ↓
3. Dashboard
      ↓
4. Station/Train/Alert pages
      ↓
5. DSA Lab shell
      ↓
6. M1 visualizers
      ↓
7. M2 visualizers
      ↓
8. M3 visualizers
      ↓
9. M4 graph visualizers
      ↓
10. M5 reduction visualizers
      ↓
11. M6 visualizers
      ↓
12. Analytics
      ↓
13. WebSocket event integration
      ↓
14. Responsive/accessibility pass
```

---

# 71. Antigravity Agent Boundaries

Frontend agents should be divided by responsibility.

### UI Foundation Agent

Owns:

```text
AppShell
Sidebar
Topbar
Theme
Reusable UI components
Routing
```

### Railway UI Agent

Owns:

```text
Dashboard
Stations
Trains
Platforms
Routes
Trips
Alerts
Events
```

### M1/M2 Agent

Owns:

```text
M1 visualizers
M2 visualizers
```

### M3 Agent

Owns:

```text
DP matrix
tree
bitmask
M3 pages
```

### M4 Agent

Owns:

```text
Graph visualization
Flow animation
Matching
Min-cut
```

### M5 Agent

Owns:

```text
SAT UI
Reduction visualizations
Approximation UI
```

### M6 Agent

Owns:

```text
Array visualization
Stream visualization
parallel computation visualization
```

### Analytics Agent

Owns:

```text
Benchmark dashboard
Performance charts
Comparison screens
```

---

# 72. Definition of Done – Frontend

The frontend is complete only when:

- [ ] application shell works
- [ ] routing works
- [ ] dashboard consumes backend data
- [ ] railway pages consume APIs
- [ ] live events work
- [ ] DSA Lab works
- [ ] all M1 algorithms have visualizers
- [ ] all M2 algorithms have visualizers
- [ ] all M3 algorithms have visualizers
- [ ] all M4 algorithms have graph visualizers
- [ ] all M5 algorithms have transformation visualizers
- [ ] all M6 algorithms have visualizers
- [ ] benchmark data is displayed
- [ ] algorithm traces are displayed
- [ ] errors are handled
- [ ] loading states exist
- [ ] large datasets are handled safely
- [ ] responsive behavior works
- [ ] accessibility basics work
- [ ] frontend tests exist
- [ ] browser verification passes

---

# 73. Final UX Goal

A user opening RailSync should understand the project within approximately one minute:

```text
RAILWAY NETWORK
       ↓
Operations Data
       ↓
DSA Algorithms
       ↓
Analysis
       ↓
Visualization
       ↓
Measured Performance
```

The application should make the relationship between the railway problem and the algorithm obvious.

The strongest demonstration is therefore not:

```text
"Here is a KMP implementation."
```

It is:

```text
"Here is a railway alert.
These keywords need to be detected.
Aho-Corasick builds the automaton.
The alert is scanned.
The matched operational keywords are highlighted.
Here are the algorithm steps and measured metrics."
```

That same principle applies across all six modules.