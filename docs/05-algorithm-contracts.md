# RailSync – Algorithm Contracts

**Document:** `docs/05-algorithm-contracts.md`  
**Project:** RailSync – Intelligent Railway Operations Platform  
**Status:** Implementation Contract  
**Language:** Java  
**Backend:** Spring Boot  

---

# 1. Purpose

This document defines the implementation contract for every required DSA algorithm in RailSync.

The architecture must maintain a strict separation:

```text
                    RAILWAY DOMAIN
                         │
                         ▼
                Railway Service Layer
                         │
                         ▼
                 Algorithm Service
                         │
                         ▼
                  DSA Algorithm
                         │
                         ▼
                 Algorithm Result
                         │
                         ▼
                    REST API
                         │
                         ▼
                  React Visualizer
```

The algorithm layer must remain domain-independent.

For example:

```java
KMPAlgorithm.search(text, pattern);
```

must not know what a railway station or train is.

Instead:

```text
RailwaySearchService
        ↓
extracts station/train/alert text
        ↓
KMPAlgorithm
```

---

# 2. Core Design Principle

Every required DSA algorithm must be:

1. manually implemented
2. independently testable
3. reusable
4. benchmarkable
5. optionally traceable
6. connected to a railway-domain use case
7. exposed through a service
8. exposed through a REST endpoint
9. visualizable in the frontend
10. documented with theoretical complexity

---

# 3. Generic Algorithm Interface

All algorithms should follow a common conceptual interface.

```java
public interface Algorithm<I, O> {

    O execute(I input);

    String getName();

    Complexity getComplexity();
}
```

Complexity:

```java
public record Complexity(
        String time,
        String space
) {}
```

Example:

```java
public class KMPAlgorithm
        implements Algorithm<KMPInput, KMPResult> {

    @Override
    public KMPResult execute(KMPInput input) {
        // implementation
    }

    @Override
    public String getName() {
        return "KMP";
    }

    @Override
    public Complexity getComplexity() {
        return new Complexity(
                "O(n + m)",
                "O(m)"
        );
    }
}
```

---

# 4. Algorithm Execution Context

Tracing and benchmarking must not contaminate the core algorithm implementation.

Use an execution context:

```java
public record ExecutionContext(
        boolean traceEnabled,
        boolean benchmarkEnabled,
        int maxTraceSteps
) {}
```

The algorithm runner manages:

```text
start timer
    ↓
execute algorithm
    ↓
collect trace
    ↓
stop timer
    ↓
calculate metrics
    ↓
return result
```

---

# 5. Standard Algorithm Result

Conceptual structure:

```java
public record AlgorithmResult<T>(
        T result,
        AlgorithmMetrics metrics,
        List<TraceStep> trace
) {}
```

Metrics:

```java
public record AlgorithmMetrics(
        String algorithm,
        long executionTimeNanos,
        long operationCount,
        String timeComplexity,
        String spaceComplexity
) {}
```

---

# 6. Trace Contract

All trace-enabled algorithms should produce structured steps.

```java
public record TraceStep(
        int step,
        String action,
        Map<String, Object> state,
        String description
) {}
```

Example:

```json
{
  "step": 12,
  "action": "COMPARE",
  "state": {
    "textIndex": 17,
    "patternIndex": 5
  },
  "description": "Comparing text and pattern characters."
}
```

---

# 7. Trace Rules

Trace generation must follow these rules:

- tracing is optional
- tracing must not change the algorithm result
- tracing must have a configurable limit
- trace steps must be deterministic when the algorithm itself is deterministic
- random algorithms must accept a seed
- trace objects must contain enough state for frontend visualization
- no raw Java internal objects should be serialized

If:

```text
maxTraceSteps = 500
```

then no more than 500 trace entries should be returned.

The algorithm must continue correctly even when trace collection stops.

---

# 8. M1 – String Algorithms

Package:

```text
algorithm.m1
```

---

# 9. KMP Contract

Package:

```text
algorithm.m1.kmp
```

Class:

```java
KMPAlgorithm
```

Input:

```java
public record KMPInput(
        String text,
        String pattern
) {}
```

Output:

```java
public record KMPResult(
        int[] lps,
        List<Integer> matches,
        int matchCount
) {}
```

Required implementation stages:

```text
Pattern
   ↓
Build LPS
   ↓
Scan text
   ↓
Compare characters
   ↓
Fallback using LPS
   ↓
Record matches
```

Required properties:

```text
Time: O(n + m)
Space: O(m)
```

Must not use:

```java
String.indexOf()
String.contains()
Pattern
Matcher
```

for the actual search.

Railway adapter examples:

```text
Train number search
Station name search
Service alert search
```

---

# 10. Z-Function Contract

Package:

```text
algorithm.m1.zfunction
```

Class:

```java
ZFunctionAlgorithm
```

Input:

```java
public record ZFunctionInput(
        String text
) {}
```

Output:

```java
public record ZFunctionResult(
        int[] zArray,
        List<Integer> matches
) {}
```

Required internal concept:

```text
[L, R] Z-box
```

Complexity:

```text
Time: O(n)
Space: O(n)
```

Railway application:

```text
repeated operational text patterns
service-message pattern detection
```

---

# 11. Rabin-Karp Contract

Package:

```text
algorithm.m1.rabinkarp
```

Class:

```java
RabinKarpAlgorithm
```

Input:

```java
public record RabinKarpInput(
        String text,
        String pattern
) {}
```

Output:

```java
public record RabinKarpResult(
        List<Integer> matches,
        long patternHash,
        int verifiedMatches,
        int hashCollisions
) {}
```

Required concepts:

```text
rolling hash
window hash
hash comparison
character verification
```

A hash match must not automatically be considered a string match.

Complexity:

```text
Expected: O(n + m)
Worst case: O(nm)
Space: O(1)
```

Railway application:

```text
ticket codes
booking references
train identifiers
```

---

# 12. Aho-Corasick Contract

Package:

```text
algorithm.m1.ahoCorasick
```

Class:

```java
AhoCorasickAlgorithm
```

Input:

```java
public record AhoCorasickInput(
        String text,
        List<String> keywords
) {}
```

Output:

```java
public record AhoCorasickResult(
        List<KeywordMatch> matches,
        int keywordCount,
        int nodeCount
) {}
```

```java
public record KeywordMatch(
        String keyword,
        int startIndex,
        int endIndex
) {}
```

Required structures:

```text
Trie
Failure links
Output links / output states
```

Railway application:

```text
delay
cancelled
platform
signal
maintenance
diversion
```

Complexity:

```text
Build: O(total pattern length)
Search: O(text length + matches)
```

---

# 13. M2 – Suffix Structures

Package:

```text
algorithm.m2
```

---

# 14. Suffix Array Contract

Class:

```java
SuffixArrayAlgorithm
```

Input:

```java
public record SuffixArrayInput(
        String text
) {}
```

Output:

```java
public record SuffixArrayResult(
        int[] suffixArray
) {}
```

The implementation must construct suffix ordering explicitly.

The project must not rely on:

```text
database full-text search
library suffix-array implementation
built-in substring indexing
```

Railway application:

```text
railway document indexing
service information indexing
```

---

# 15. SA-IS Contract

Class:

```java
SAISAlgorithm
```

Input:

```java
public record SAISInput(
        String text
) {}
```

Output:

```java
public record SAISResult(
        int[] suffixArray
) {}
```

Required concepts:

```text
S-type / L-type classification
LMS positions
induced sorting
recursive reduction
```

SA-IS must be implemented separately from the ordinary suffix-array implementation.

The implementation must not simply call:

```java
new SuffixArrayAlgorithm()
```

internally.

---

# 16. LCP Contract

Class:

```java
LCPAlgorithm
```

Input:

```java
public record LCPInput(
        String text,
        int[] suffixArray
) {}
```

Output:

```java
public record LCPResult(
        int[] lcpArray
) {}
```

Complexity:

```text
O(n)
```

---

# 17. Kasai Contract

Class:

```java
KasaiAlgorithm
```

Kasai's algorithm should be responsible for constructing the LCP array from the text and suffix array.

Input:

```java
public record KasaiInput(
        String text,
        int[] suffixArray
) {}
```

Output:

```java
public record KasaiResult(
        int[] lcpArray,
        String longestRepeatedSubstring,
        int longestRepeatedLength
) {}
```

The implementation must exploit the previous LCP value rather than recomputing every comparison from scratch.

---

# 18. Suffix Automaton Contract

Class:

```java
SuffixAutomatonAlgorithm
```

Input:

```java
public record SuffixAutomatonInput(
        String text,
        String query
) {}
```

Output:

```java
public record SuffixAutomatonResult(
        boolean contains,
        int stateCount,
        int transitionCount,
        List<Integer> matchPositions
) {}
```

Required concepts:

```text
states
transitions
suffix links
cloning
```

Railway application:

```text
substring search
document/service-information search
```

---

# 19. M3 – Advanced Dynamic Programming

Package:

```text
algorithm.m3
```

---

# 20. Levenshtein Contract

Class:

```java
LevenshteinAlgorithm
```

Input:

```java
public record LevenshteinInput(
        String source,
        String target
) {}
```

Output:

```java
public record LevenshteinResult(
        int distance,
        List<EditOperation> operations
) {}
```

```java
public record EditOperation(
        String type,
        int position,
        Character character
) {}
```

Operations:

```text
INSERT
DELETE
REPLACE
MATCH
```

Complexity:

```text
Time: O(nm)
Space: O(nm)
```

Railway use:

```text
station-name correction
passenger-data correction
```

---

# 21. Damerau-Levenshtein Contract

Class:

```java
DamerauLevenshteinAlgorithm
```

Input:

```java
public record DamerauInput(
        String source,
        String target
) {}
```

Output:

```java
public record DamerauResult(
        int distance,
        List<EditOperation> operations
) {}
```

Required additional operation:

```text
TRANSPOSE
```

The implementation must distinguish this from ordinary Levenshtein distance.

---

# 22. Bitmask DP Contract

Class:

```java
BitmaskDPAlgorithm
```

Input:

```java
public record BitmaskInput(
        List<BitmaskItem> items,
        int maxSelections
) {}
```

```java
public record BitmaskItem(
        String id,
        long cost
) {}
```

Output:

```java
public record BitmaskResult(
        List<String> selectedItems,
        long objectiveValue,
        int statesVisited
) {}
```

Required concept:

```text
mask
state
transition
```

The implementation must explicitly enforce a safe maximum number of items.

Example:

```text
n <= configuredLimit
```

because:

```text
states = 2^n
```

Railway use:

```text
small-scale route/service combination analysis
```

It must never be presented as a national-scale scheduling algorithm.

---

# 23. Matrix-Chain Multiplication Contract

Class:

```java
MatrixChainAlgorithm
```

Input:

```java
public record MatrixChainInput(
        int[] dimensions
) {}
```

Output:

```java
public record MatrixChainResult(
        long minimumMultiplications,
        String optimalParenthesization,
        long[][] costMatrix,
        int[][] splitMatrix
) {}
```

Complexity:

```text
Time: O(n^3)
Space: O(n^2)
```

Railway interpretation:

```text
ordered analytical/data operations
```

The algorithm must remain mathematically generic.

---

# 24. Optimal BST Contract

Class:

```java
OptimalBSTAlgorithm
```

Input:

```java
public record OptimalBSTInput(
        List<String> keys,
        long[] frequencies
) {}
```

Output:

```java
public record OptimalBSTResult(
        long expectedSearchCost,
        int rootIndex,
        BSTNodeResult tree
) {}
```

```java
public record BSTNodeResult(
        String key,
        BSTNodeResult left,
        BSTNodeResult right
) {}
```

Railway interpretation:

```text
frequently accessed station/service records
```

Important:

The algorithm demonstrates optimal search-tree construction.

It does not replace:

```text
PostgreSQL indexes
database query planning
```

---

# 25. M4 – Network Flow

Package:

```text
algorithm.m4
```

---

# 26. Common Graph Model

```java
public record GraphInput(
        List<GraphNode> nodes,
        List<GraphEdge> edges,
        String source,
        String sink
) {}
```

```java
public record GraphNode(
        String id,
        String label
) {}
```

```java
public record GraphEdge(
        String id,
        String source,
        String target,
        long capacity
) {}
```

---

# 27. Internal Residual Graph

Flow algorithms should use Java domain objects such as:

```java
class ResidualGraph
class ResidualEdge
```

These are algorithm-engine objects.

They should not be stored directly in PostgreSQL.

---

# 28. Ford-Fulkerson Contract

Class:

```java
FordFulkersonAlgorithm
```

Input:

```java
GraphInput
```

Output:

```java
public record MaxFlowResult(
        long maxFlow,
        List<AugmentingPath> augmentingPaths,
        List<EdgeFlow> edgeFlows
) {}
```

```java
public record AugmentingPath(
        List<String> path,
        long flow
) {}
```

Required concept:

```text
residual graph
augmenting path
residual capacity
```

---

# 29. Edmonds-Karp Contract

Class:

```java
EdmondsKarpAlgorithm
```

Edmonds-Karp must explicitly use BFS to select augmenting paths.

Additional metrics:

```java
public record EdmondsKarpResult(
        long maxFlow,
        int bfsIterations,
        List<AugmentingPath> augmentingPaths,
        List<EdgeFlow> edgeFlows
) {}
```

---

# 30. Dinic Contract

Class:

```java
DinicAlgorithm
```

Required structures:

```text
level graph
BFS
blocking flow
DFS
residual graph
```

Output:

```java
public record DinicResult(
        long maxFlow,
        int phases,
        int blockingFlowIterations,
        List<EdgeFlow> edgeFlows
) {}
```

Railway use:

```text
large passenger-flow networks
```

---

# 31. Bipartite Matching Contract

Class:

```java
BipartiteMatchingAlgorithm
```

Input:

```java
public record BipartiteGraphInput(
        List<BipartiteNode> leftNodes,
        List<BipartiteNode> rightNodes,
        List<BipartiteEdge> edges
) {}
```

Output:

```java
public record MatchingResult(
        int matchingSize,
        List<MatchPair> matches
) {}
```

Railway mapping:

```text
Train → Eligible Platform
```

---

# 32. König's Theorem Contract

Class:

```java
KonigTheoremAnalyzer
```

Input:

```text
bipartite graph
```

Output:

```java
public record KonigResult(
        int maximumMatchingSize,
        int minimumVertexCoverSize,
        VertexCover vertexCover,
        boolean verified
) {}
```

Required verification:

```text
maximum matching size
=
minimum vertex cover size
```

for the supplied bipartite graph.

---

# 33. Max-Flow Min-Cut Contract

Class:

```java
MaxFlowMinCutAnalyzer
```

Output:

```java
public record MaxFlowMinCutResult(
        long maxFlow,
        long minCutCapacity,
        List<CutEdge> cutEdges,
        List<String> bottleneckNodes,
        boolean verified
) {}
```

Verification:

```text
maxFlow == minCutCapacity
```

The cut should be derived from the residual graph after maximum flow.

Railway interpretation:

```text
critical capacity bottlenecks
```

---

# 34. M5 – NP-Completeness and Approximation

Package:

```text
algorithm.m5
```

---

# 35. SAT Contract

Class:

```java
SATAlgorithm
```

Input:

```java
public record SATInput(
        List<String> variables,
        List<List<Literal>> clauses
) {}
```

```java
public record Literal(
        String variable,
        boolean positive
) {}
```

Output:

```java
public record SATResult(
        boolean satisfiable,
        Map<String, Boolean> assignment
) {}
```

The initial implementation may use a bounded backtracking/DPLL-style approach.

Input limits must be explicit.

---

# 36. 3-SAT Contract

Class:

```java
ThreeSATAlgorithm
```

Each clause must contain exactly three literals.

Validation must reject:

```text
0 literals
1 literal
2 literals
>3 literals
```

unless an explicit normalization step is being demonstrated.

---

# 37. 3-SAT → CLIQUE Contract

Class:

```java
ThreeSATToCliqueReduction
```

Input:

```text
3-SAT instance
```

Output:

```java
public record CliqueReductionResult(
        GraphResult graph,
        int targetCliqueSize,
        Map<String, String> mapping
) {}
```

Required property:

For a 3-SAT instance with `m` clauses:

```text
target clique size = m
```

The generated graph must encode compatibility between literals from different clauses.

---

# 38. CLIQUE → Independent Set

Class:

```java
CliqueToIndependentSetReduction
```

Required transformation:

```text
G
    ↓
Complement(G)
```

Property:

```text
G contains clique of size k
iff
complement(G) contains independent set of size k
```

The API should expose enough graph information for the frontend to visualize the transformation.

---

# 39. Independent Set → Vertex Cover

Class:

```java
IndependentSetToVertexCoverReduction
```

Required relationship:

```text
S is an independent set
iff
V − S is a vertex cover
```

The implementation must expose:

```text
original graph
independent set
complement set
vertex cover
verification
```

---

# 40. Vertex-Cover 2-Approximation

Class:

```java
VertexCoverTwoApproximation
```

Required algorithm:

```text
C = empty
E' = all edges

while E' not empty:
    choose an uncovered edge (u, v)
    add u and v to C
    remove all edges incident to u or v
```

Output:

```java
public record ApproximationResult(
        Set<String> cover,
        int coverSize,
        int selectedEdges,
        double approximationBound
) {}
```

Expected theoretical guarantee:

```text
|C| <= 2 * OPT
```

The implementation must not claim that `C` is optimal unless independently verified.

---

# 41. M6 – Randomized Algorithms

Package:

```text
algorithm.m6
```

---

# 42. Randomized QuickSort Contract

Class:

```java
RandomizedQuickSortAlgorithm
```

Input:

```java
public record RandomizedQuickSortInput(
        int[] values,
        Long seed
) {}
```

Output:

```java
public record RandomizedQuickSortResult(
        int[] sortedValues,
        List<Integer> pivotSelections,
        long comparisons,
        long swaps
) {}
```

If seed is supplied:

```text
same input + same seed
→ reproducible execution
```

---

# 43. Reservoir Sampling Contract

Class:

```java
ReservoirSamplingAlgorithm<T>
```

Input:

```java
public record ReservoirInput<T>(
        List<T> stream,
        int sampleSize,
        Long seed
) {}
```

Output:

```java
public record ReservoirResult<T>(
        List<T> sample,
        long streamSize,
        int sampleSize,
        long replacementCount
) {}
```

Railway application:

```text
continuous railway-event streams
```

The implementation must process the stream sequentially rather than first selecting a random subset using a library function.

---

# 44. Miller-Rabin Contract

Class:

```java
MillerRabinAlgorithm
```

Input:

```java
public record MillerRabinInput(
        long number,
        int rounds,
        Long seed
) {}
```

Output:

```java
public record MillerRabinResult(
        long number,
        boolean probablyPrime,
        int rounds,
        List<Long> witnesses
) {}
```

Required terminology:

```text
probablyPrime
```

not:

```text
guaranteedPrime
```

unless a mathematically appropriate deterministic range is explicitly implemented and documented.

---

# 45. Blelloch Scan Contract

Class:

```java
BlellochScanAlgorithm
```

Input:

```java
public record ScanInput(
        long[] values
) {}
```

Output:

```java
public record ScanResult(
        long[] exclusiveScan,
        long total,
        List<ScanLevel> levels
) {}
```

```java
public record ScanLevel(
        int level,
        long[] values
) {}
```

Required phases:

```text
Upsweep
   ↓
Set root to identity
   ↓
Downsweep
```

Railway use:

```text
cumulative passenger statistics
event counts
station statistics
```

---

# 46. Parallel Reduce Contract

Class:

```java
ParallelReduceAlgorithm
```

Input:

```java
public record ReduceInput(
        long[] values,
        ReduceOperation operation,
        int workerCount
) {}
```

```java
public enum ReduceOperation {
    SUM,
    MIN,
    MAX
}
```

Output:

```java
public record ReduceResult(
        long result,
        ReduceOperation operation,
        int workerCount,
        long[] partialResults
) {}
```

The implementation should expose how the input is divided into partial computations.

---

# 47. Brent's Theorem Contract

Class:

```java
BrentTheoremAnalyzer
```

Input:

```java
public record BrentInput(
        long work,
        long span,
        int[] processors
) {}
```

Output:

```java
public record BrentResult(
        long work,
        long span,
        List<ProcessorEstimate> estimates
) {}
```

```java
public record ProcessorEstimate(
        int processors,
        double estimatedTime
) {}
```

Conceptual bound:

```text
T_p = O(W / P + S)
```

where:

```text
W = total work
S = span
P = processors
```

This endpoint is an analytical model, not an actual benchmark of CPU scheduling.

---

# 48. Algorithm Service Layer

Controllers must not directly instantiate algorithms.

Incorrect:

```java
@RestController
class KMPController {

    @PostMapping
    public Object run(...) {
        return new KMPAlgorithm().execute(...);
    }
}
```

Preferred:

```text
KMPController
      ↓
KMPService
      ↓
KMPAlgorithm
      ↓
KMPResult
```

Example:

```java
@Service
public class KMPService {

    private final KMPAlgorithm algorithm;

    public KMPResult execute(KMPInput input) {
        return algorithm.execute(input);
    }
}
```

---

# 49. Railway Adapter Layer

Railway-specific interpretation must remain outside the algorithm.

Example:

```text
StationSearchService
        ↓
loads station names
        ↓
KMPAlgorithm
        ↓
matches
        ↓
map matches to Station objects
```

This prevents:

```text
KMPAlgorithm
```

from becoming coupled to:

```text
StationRepository
TrainRepository
TicketRepository
```

---

# 50. Algorithm Registry

The backend should maintain a registry of available algorithms.

Conceptual structure:

```java
public record AlgorithmDescriptor(
        String id,
        String name,
        String module,
        String description,
        String timeComplexity,
        String spaceComplexity
) {}
```

Example:

```json
{
  "id": "m1-kmp",
  "name": "Knuth-Morris-Pratt",
  "module": "M1",
  "description": "Exact pattern matching",
  "timeComplexity": "O(n + m)",
  "spaceComplexity": "O(m)"
}
```

Endpoint:

```text
GET /api/algorithms
```

This allows the frontend to build the DSA module catalogue dynamically.

---

# 51. Complexity Metadata

Complexity must be stored as documentation metadata.

Example:

```java
new Complexity(
    "O(n + m)",
    "O(m)"
);
```

The system must distinguish:

```text
theoretical complexity
```

from:

```text
measured execution time
```

The UI should display both separately.

---

# 52. Benchmark Wrapper

Use a common benchmark wrapper:

```java
public class AlgorithmBenchmark {

    public <I, O> BenchmarkResult<O> run(
            Algorithm<I, O> algorithm,
            I input
    ) {
        // measure execution
    }
}
```

Benchmark result:

```java
public record BenchmarkResult<O>(
        O result,
        long executionTimeNanos,
        long operationCount
) {}
```

Do not use benchmark values generated manually.

---

# 53. Operation Counting

Algorithms should increment logical operation counters where meaningful.

Examples:

### KMP

```text
character comparisons
LPS transitions
matches
```

### QuickSort

```text
comparisons
swaps
partition operations
```

### Dinic

```text
BFS iterations
DFS augmentations
edge inspections
```

### DP

```text
state evaluations
transitions
```

### Aho-Corasick

```text
character transitions
failure transitions
matches
```

The operation counter must be clearly defined for each algorithm.

---

# 54. Determinism Rules

Deterministic algorithms:

```text
same input
→ same output
```

Randomized algorithms:

```text
same input + same seed
→ same output
```

Without a seed:

```text
output may vary
```

This is especially important for:

```text
Randomized QuickSort
Reservoir Sampling
Miller-Rabin witness selection
```

---

# 55. Input Safety

The backend must protect algorithms from pathological inputs.

Examples:

```text
Bitmask DP
maximum item count

SAT
maximum variable/clause count

3-SAT → CLIQUE
maximum generated graph size

Trace
maximum trace steps

Suffix structures
maximum request text size
```

Limits must be configurable.

Example:

```yaml
railsync:
  algorithms:
    bitmask:
      max-items: 20
    sat:
      max-variables: 30
    trace:
      max-steps: 500
```

Values are implementation configuration, not hardcoded algorithm assumptions.

---

# 56. Testing Contract

Every algorithm must have:

```text
unit tests
edge-case tests
complexity-oriented tests
integration tests
API tests
```

Example:

```text
KMP
├── empty text
├── empty pattern
├── pattern longer than text
├── pattern at beginning
├── pattern at end
├── repeated pattern
├── overlapping matches
└── random comparison against trusted reference
```

---

# 57. Cross-Algorithm Validation

Where algorithms solve related problems, cross-validation should be used.

Examples:

### KMP vs Rabin-Karp

For the same exact-match input:

```text
KMP matches == Rabin-Karp matches
```

### LCP vs Kasai

```text
LCP result == Kasai result
```

### Ford-Fulkerson vs Edmonds-Karp vs Dinic

```text
maxFlow_FF
=
maxFlow_EK
=
maxFlow_Dinic
```

for the same valid graph.

### Matching vs König

```text
maximumMatchingSize
=
minimumVertexCoverSize
```

### Max-Flow Min-Cut

```text
maxFlow
=
minCutCapacity
```

### Independent Set / Vertex Cover

For the relevant transformation:

```text
|IndependentSet| + |VertexCover| = |V|
```

---

# 58. No Fake Benchmarks

Never write:

```java
executionTimeNanos = 123456;
```

Never fabricate:

```text
KMP is 37% faster
```

without actually measuring it.

Benchmark values must originate from actual execution.

---

# 59. No Hidden Library Implementations

The following are prohibited for the core DSA implementation:

```text
Apache Commons string search
Java regex for required pattern matching
database full-text search
graph algorithm libraries
third-party suffix-array libraries
optimization libraries replacing DP
SAT solver replacing the educational implementation
```

Libraries may be used for:

```text
JSON
HTTP
database access
testing
logging
Spring infrastructure
```

but not to replace the required DSA algorithms.

---

# 60. Package Structure

Final algorithm package:

```text
backend/src/main/java/.../algorithm/

├── common/
│   ├── Algorithm.java
│   ├── Complexity.java
│   ├── AlgorithmMetrics.java
│   ├── AlgorithmResult.java
│   ├── ExecutionContext.java
│   ├── TraceStep.java
│   └── AlgorithmBenchmark.java
│
├── m1/
│   ├── kmp/
│   ├── zfunction/
│   ├── rabinkarp/
│   └── ahoCorasick/
│
├── m2/
│   ├── suffixarray/
│   ├── sais/
│   ├── lcp/
│   ├── kasai/
│   └── suffixautomaton/
│
├── m3/
│   ├── levenshtein/
│   ├── damerau/
│   ├── bitmask/
│   ├── matrixchain/
│   └── optimalbst/
│
├── m4/
│   ├── common/
│   ├── fordfulkerson/
│   ├── edmondskarp/
│   ├── dinic/
│   ├── matching/
│   ├── konig/
│   └── mincut/
│
├── m5/
│   ├── sat/
│   ├── threesat/
│   ├── reductions/
│   └── vertexcover/
│
└── m6/
    ├── randomizedquicksort/
    ├── reservoirsampling/
    ├── millerrabin/
    ├── blellochscan/
    ├── parallelreduce/
    └── brent/
```

---

# 61. Algorithm Definition of Done

An individual algorithm is **not complete** merely because its Java method returns the correct answer.

It is complete only when:

- [ ] core algorithm implemented manually
- [ ] input/output model created
- [ ] complexity documented
- [ ] edge cases handled
- [ ] unit tests written
- [ ] benchmark support added
- [ ] operation counting defined
- [ ] optional trace implemented
- [ ] trace limit implemented
- [ ] service adapter implemented
- [ ] REST endpoint implemented
- [ ] API integration test written
- [ ] railway use case connected
- [ ] frontend visualization connected
- [ ] frontend result display connected
- [ ] error handling implemented
- [ ] documentation updated
- [ ] cross-validation added where applicable

---

# 62. Module Definition of Done

A module is complete only when every algorithm inside it satisfies the individual definition of done.

For example:

```text
M1
│
├── KMP ✓
├── Z-Function ✓
├── Rabin-Karp ✓
└── Aho-Corasick ✓
        │
        ▼
   M1 Integration Test
        │
        ▼
   M1 Frontend Dashboard
```

The same rule applies to:

```text
M2
M3
M4
M5
M6
```

---

# 63. Critical Antigravity Rule

An agent working on one algorithm must not silently modify another algorithm's implementation.

For example:

```text
M1 agent
```

must not modify:

```text
M4 graph engine
```

unless an explicit shared-interface change is required.

If a shared contract must change:

```text
1. document the change
2. update algorithm contract
3. update API contract
4. update affected tests
5. notify dependent agents
```

---

# 64. Final Algorithm Architecture

The final architecture must look like:

```text
                    ┌─────────────────────┐
                    │    React Frontend   │
                    └──────────┬──────────┘
                               │
                              REST
                               │
                    ┌──────────▼──────────┐
                    │    Spring Boot API  │
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │   Railway Services  │
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │ Algorithm Services  │
                    └──────────┬──────────┘
                               │
             ┌─────────────────┴──────────────────┐
             │                                    │
       ┌─────▼─────┐                        ┌─────▼─────┐
       │ Algorithm │                        │ Benchmark │
       │   Engine  │                        │  / Trace  │
       └─────┬─────┘                        └───────────┘
             │
      ┌──────┼──────┬──────┬──────┬──────┐
      │      │      │      │      │      │
     M1     M2     M3     M4     M5     M6
```

The key architectural rule is:

```text
Railway domain gives algorithms meaningful data.
Algorithms perform the actual DSA computation.
Frontend visualizes the computation.
```

RailSync should therefore demonstrate **DSA through a railway operations system**, rather than merely attaching railway labels to generic algorithm demos.