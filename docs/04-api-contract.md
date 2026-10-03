# RailSync – API Contract

**Document:** `docs/04-api-contract.md`  
**Project:** RailSync – Intelligent Railway Operations Platform  
**Status:** Implementation Contract  
**Backend:** Java + Spring Boot  
**Frontend:** React + Vite  
**Database:** PostgreSQL  
**API Style:** REST + WebSocket  
**Base Path:** `/api`

---

# 1. Purpose

This document defines the API contract shared between:

- Spring Boot backend
- React frontend
- DSA algorithm engine
- Railway domain services
- Analytics and visualization components
- Automated tests
- Antigravity development agents

The contract exists to prevent frontend and backend agents from independently inventing incompatible request/response formats.

The API must remain aligned with the six project modules:

| Module | Focus |
|---|---|
| M1 | String Algorithms |
| M2 | Suffix Structures |
| M3 | Advanced Dynamic Programming |
| M4 | Network Flow |
| M5 | NP-Completeness & Approximation |
| M6 | Randomized & Parallel Algorithms |

---

# 2. General API Rules

## 2.1 Base URL

Development:

```text
http://localhost:8080/api
```

Frontend development server:

```text
http://localhost:5173
```

WebSocket:

```text
ws://localhost:8080/ws/events
```

---

# 3. Standard Response Envelope

Successful responses should follow:

```json
{
  "success": true,
  "data": {},
  "meta": {
    "requestId": "req-8f31a",
    "timestamp": "2026-10-02T18:20:31Z"
  }
}
```

Algorithm responses additionally include execution information:

```json
{
  "success": true,
  "data": {},
  "meta": {
    "requestId": "req-8f31a",
    "algorithm": "KMP",
    "executionTimeNanos": 152340,
    "operationCount": 84,
    "traceEnabled": true
  }
}
```

`executionTimeNanos` is the measured server-side execution time.

It must **not** be presented as theoretical complexity.

---

# 4. Standard Error Response

All validation and processing failures should use:

```json
{
  "success": false,
  "error": {
    "code": "INVALID_INPUT",
    "message": "Pattern must not be empty.",
    "details": {
      "field": "pattern"
    }
  },
  "meta": {
    "requestId": "req-8f31a",
    "timestamp": "2026-10-02T18:20:31Z"
  }
}
```

## Standard error codes

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

---

# 5. Algorithm Execution Options

All algorithm endpoints should support a common execution configuration where applicable.

```json
{
  "traceEnabled": true,
  "benchmarkEnabled": true,
  "maxTraceSteps": 500
}
```

### Meaning

| Field | Purpose |
|---|---|
| `traceEnabled` | Return algorithm execution trace |
| `benchmarkEnabled` | Measure execution statistics |
| `maxTraceSteps` | Prevent enormous trace responses |

Default:

```text
traceEnabled = false
benchmarkEnabled = true
maxTraceSteps = 500
```

The algorithm itself must still execute normally when tracing is disabled.

---

# 6. Railway Domain APIs

These endpoints provide the railway data consumed by the DSA modules.

---

## 6.1 Stations

### `GET /api/stations`

Query parameters:

```text
page
size
search
```

Example:

```text
GET /api/stations?search=Vijay&page=0&size=20
```

Response:

```json
{
  "success": true,
  "data": {
    "items": [
      {
        "id": 1,
        "stationCode": "VJA",
        "name": "Vijayawada Junction",
        "city": "Vijayawada",
        "zone": "SCR",
        "platformCount": 8
      }
    ],
    "page": 0,
    "size": 20,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

---

## 6.2 Trains

### `GET /api/trains`

Filters:

```text
trainNumber
name
status
```

Example:

```text
GET /api/trains?trainNumber=127
```

---

## 6.3 Platforms

### `GET /api/platforms`

Optional filters:

```text
stationId
status
```

---

## 6.4 Routes

### `GET /api/routes`

Returns railway route information.

---

## 6.5 Trips

### `GET /api/trips`

Optional filters:

```text
trainId
routeId
date
status
```

---

## 6.6 Passengers

### `GET /api/passengers`

For the academic simulation only.

No real personally identifiable passenger information should be seeded.

---

## 6.7 Tickets

### `GET /api/tickets`

Optional filters:

```text
ticketCode
trainId
status
```

Ticket codes are primarily consumed by M1 Rabin-Karp.

---

## 6.8 Service Alerts

### `GET /api/alerts`

Optional filters:

```text
severity
status
stationId
```

Alerts provide input for:

- KMP
- Z-Function
- Aho-Corasick

---

## 6.9 Maintenance Records

### `GET /api/maintenance`

Optional filters:

```text
stationId
trainId
status
```

---

## 6.10 Railway Events

### `GET /api/events`

Optional filters:

```text
eventType
trainId
stationId
from
to
```

Events are used heavily by M6.

---

## 6.11 Railway Documents

### `GET /api/documents`

Returns indexed railway documents.

Example:

```json
{
  "success": true,
  "data": {
    "items": [
      {
        "id": 101,
        "title": "Platform Allocation Procedure",
        "documentType": "OPERATIONS",
        "contentLength": 4218
      }
    ]
  }
}
```

---

### `GET /api/documents/{id}`

Returns the document content.

```json
{
  "success": true,
  "data": {
    "id": 101,
    "title": "Platform Allocation Procedure",
    "documentType": "OPERATIONS",
    "content": "..."
  }
}
```

---

# 7. M1 – String Algorithms

Base path:

```text
/api/m1
```

---

# 7.1 KMP

### `POST /api/m1/kmp`

Purpose:

Search:

- train numbers
- station names
- service alerts
- railway operational text

### Request

```json
{
  "text": "Train 12727 departs Vijayawada Junction",
  "pattern": "Vijayawada",
  "traceEnabled": true,
  "benchmarkEnabled": true,
  "maxTraceSteps": 200
}
```

### Response

```json
{
  "success": true,
  "data": {
    "matches": [25],
    "matchCount": 1,
    "lps": [0, 0, 0, 0, 0, 0, 0, 0, 0],
    "trace": [
      {
        "textIndex": 25,
        "patternIndex": 0,
        "action": "MATCH"
      }
    ]
  },
  "meta": {
    "algorithm": "KMP",
    "executionTimeNanos": 152340,
    "operationCount": 84
  }
}
```

---

# 7.2 Z-Function

### `POST /api/m1/z`

Request:

```json
{
  "text": "ABCABCABC",
  "pattern": "ABC",
  "traceEnabled": true
}
```

Response:

```json
{
  "success": true,
  "data": {
    "zArray": [0, 0, 0, 6, 0, 0, 3, 0, 0],
    "matches": [3, 6],
    "matchCount": 2,
    "trace": []
  },
  "meta": {
    "algorithm": "Z_FUNCTION",
    "executionTimeNanos": 123456,
    "operationCount": 61
  }
}
```

For railway usage, the service layer may construct:

```text
pattern + separator + operationalText
```

before calling the algorithm.

---

# 7.3 Rabin-Karp

### `POST /api/m1/rabin-karp`

Request:

```json
{
  "text": "TKT-12727-VJA-20261002",
  "pattern": "12727",
  "traceEnabled": true
}
```

Response:

```json
{
  "success": true,
  "data": {
    "matches": [4],
    "matchCount": 1,
    "patternHash": 482913,
    "verifiedMatches": 1,
    "hashCollisions": 0,
    "trace": []
  },
  "meta": {
    "algorithm": "RABIN_KARP",
    "executionTimeNanos": 103920
  }
}
```

Hash collisions must be reported when encountered.

---

# 7.4 Aho-Corasick

### `POST /api/m1/aho-corasick`

Request:

```json
{
  "text": "Train delayed due to platform congestion and signal failure.",
  "keywords": [
    "delay",
    "platform",
    "signal",
    "cancelled"
  ],
  "traceEnabled": true
}
```

Response:

```json
{
  "success": true,
  "data": {
    "matches": [
      {
        "keyword": "platform",
        "startIndex": 29,
        "endIndex": 36
      },
      {
        "keyword": "signal",
        "startIndex": 51,
        "endIndex": 56
      }
    ],
    "keywordCount": 4,
    "matchCount": 2,
    "automatonNodeCount": 30,
    "trace": []
  },
  "meta": {
    "algorithm": "AHO_CORASICK",
    "executionTimeNanos": 184230
  }
}
```

---

# 8. M2 – Suffix Structures

Base path:

```text
/api/m2
```

---

# 8.1 Suffix Array

### `POST /api/m2/suffix-array`

Request:

```json
{
  "text": "railwaystation",
  "traceEnabled": false
}
```

Response:

```json
{
  "success": true,
  "data": {
    "suffixArray": [11, 7, 4, 6, 2, 9, 0, 5, 10, 1, 8, 3],
    "textLength": 13,
    "trace": []
  },
  "meta": {
    "algorithm": "SUFFIX_ARRAY",
    "executionTimeNanos": 219340
  }
}
```

For large inputs, the API must not automatically return every sorted suffix string.

---

# 8.2 SA-IS

### `POST /api/m2/sa-is`

Request:

```json
{
  "text": "platformallocationprocedure",
  "traceEnabled": false
}
```

Response:

```json
{
  "success": true,
  "data": {
    "suffixArray": [],
    "textLength": 27,
    "stateCount": 27
  },
  "meta": {
    "algorithm": "SA_IS",
    "executionTimeNanos": 392100
  }
}
```

SA-IS must remain a separate implementation from the ordinary suffix-array implementation.

---

# 8.3 LCP

### `POST /api/m2/lcp`

Request:

```json
{
  "text": "railwayrail",
  "suffixArray": [5, 8, 0, 6, 3, 9, 1, 7, 4, 2],
  "traceEnabled": true
}
```

Response:

```json
{
  "success": true,
  "data": {
    "lcpArray": [0, 1, 2, 0, 1, 3, 0, 1, 2, 0],
    "longestCommonPrefixLength": 3
  },
  "meta": {
    "algorithm": "LCP"
  }
}
```

---

# 8.4 Kasai

### `POST /api/m2/kasai`

Request:

```json
{
  "text": "railwayrail",
  "suffixArray": [5, 8, 0, 6, 3, 9, 1, 7, 4, 2]
}
```

Response:

```json
{
  "success": true,
  "data": {
    "lcpArray": [],
    "longestRepeatedSubstring": {
      "text": "rail",
      "length": 4
    }
  },
  "meta": {
    "algorithm": "KASAI"
  }
}
```

---

# 8.5 Suffix Automaton

### `POST /api/m2/suffix-automaton`

Request:

```json
{
  "text": "railwaystation",
  "query": "station",
  "traceEnabled": true
}
```

Response:

```json
{
  "success": true,
  "data": {
    "contains": true,
    "matchPositions": [7],
    "stateCount": 18,
    "transitionCount": 25
  },
  "meta": {
    "algorithm": "SUFFIX_AUTOMATON"
  }
}
```

---

# 8.6 Railway Document Indexing

### `POST /api/m2/documents/index`

Request:

```json
{
  "documentIds": [101, 102, 103],
  "indexAlgorithm": "SA_IS"
}
```

Response:

```json
{
  "success": true,
  "data": {
    "documentsIndexed": 3,
    "totalCharacters": 18342,
    "indexType": "SUFFIX_ARRAY"
  }
}
```

This endpoint connects the M2 algorithms to the railway document domain.

---

# 9. M3 – Advanced Dynamic Programming

Base path:

```text
/api/m3
```

---

# 9.1 Levenshtein Distance

### `POST /api/m3/levenshtein`

Request:

```json
{
  "source": "Vijaywada",
  "target": "Vijayawada",
  "traceEnabled": true
}
```

Response:

```json
{
  "success": true,
  "data": {
    "distance": 1,
    "operations": [
      {
        "type": "INSERT",
        "character": "a",
        "position": 7
      }
    ],
    "dpMatrix": []
  },
  "meta": {
    "algorithm": "LEVENSHTEIN"
  }
}
```

---

# 9.2 Damerau-Levenshtein

### `POST /api/m3/damerau`

Request:

```json
{
  "source": "Vijaywada",
  "target": "Vijayawada",
  "traceEnabled": true
}
```

Response:

```json
{
  "success": true,
  "data": {
    "distance": 1,
    "operations": []
  },
  "meta": {
    "algorithm": "DAMERAU_LEVENSHTEIN"
  }
}
```

---

# 9.3 Bitmask DP

### `POST /api/m3/bitmask`

Purpose:

Solve **small bounded route/service combination problems**.

Request:

```json
{
  "items": [
    {
      "id": "R1",
      "cost": 10
    },
    {
      "id": "R2",
      "cost": 14
    },
    {
      "id": "R3",
      "cost": 8
    }
  ],
  "constraints": {
    "maxSelections": 2
  },
  "traceEnabled": true
}
```

Response:

```json
{
  "success": true,
  "data": {
    "selectedItems": ["R1", "R3"],
    "objectiveValue": 18,
    "dpStatesVisited": 7,
    "stateCount": 8
  },
  "meta": {
    "algorithm": "BITMASK_DP"
  }
}
```

The API must enforce practical input limits because this is exponential-state DP.

---

# 9.4 Matrix-Chain Multiplication

### `POST /api/m3/matrix-chain`

Request:

```json
{
  "dimensions": [10, 20, 30, 40, 30],
  "traceEnabled": true
}
```

Response:

```json
{
  "success": true,
  "data": {
    "minimumMultiplications": 30000,
    "optimalParenthesization": "((A1A2)(A3A4))",
    "costMatrix": [],
    "splitMatrix": []
  },
  "meta": {
    "algorithm": "MATRIX_CHAIN"
  }
}
```

Railway interpretation:

Matrix operations may represent ordered analytical/data-processing operations.

The algorithm is not presented as a railway scheduling algorithm.

---

# 9.5 Optimal BST

### `POST /api/m3/optimal-bst`

Request:

```json
{
  "keys": [
    "VJA",
    "BZA",
    "SC",
    "MAS"
  ],
  "frequencies": [50, 30, 15, 5],
  "traceEnabled": true
}
```

Response:

```json
{
  "success": true,
  "data": {
    "expectedSearchCost": 100,
    "rootIndex": 0,
    "tree": {
      "key": "VJA",
      "left": null,
      "right": "BZA"
    }
  },
  "meta": {
    "algorithm": "OPTIMAL_BST"
  }
}
```

This is an algorithmic demonstration for frequently accessed railway records, not a replacement for PostgreSQL indexing.

---

# 10. M4 – Network Flow

Base path:

```text
/api/m4
```

---

# 10.1 Graph Input Contract

All flow algorithms use:

```json
{
  "nodes": [
    {
      "id": "S1",
      "label": "Station A"
    },
    {
      "id": "S2",
      "label": "Station B"
    }
  ],
  "edges": [
    {
      "id": "E1",
      "source": "S1",
      "target": "S2",
      "capacity": 500
    }
  ],
  "source": "S1",
  "sink": "S2",
  "traceEnabled": true
}
```

---

# 10.2 Ford-Fulkerson

### `POST /api/m4/ford-fulkerson`

Response:

```json
{
  "success": true,
  "data": {
    "maxFlow": 500,
    "augmentingPaths": [
      {
        "path": ["S1", "S2"],
        "flow": 500
      }
    ],
    "edgeFlows": []
  },
  "meta": {
    "algorithm": "FORD_FULKERSON"
  }
}
```

---

# 10.3 Edmonds-Karp

### `POST /api/m4/edmonds-karp`

Same graph input contract.

Additional result:

```json
{
  "maxFlow": 500,
  "augmentations": 1,
  "bfsIterations": 1
}
```

---

# 10.4 Dinic

### `POST /api/m4/dinic`

Response:

```json
{
  "success": true,
  "data": {
    "maxFlow": 500,
    "phases": 1,
    "blockingFlowIterations": 1,
    "edgeFlows": []
  },
  "meta": {
    "algorithm": "DINIC"
  }
}
```

---

# 10.5 Bipartite Matching

### `POST /api/m4/bipartite-matching`

Railway use case:

```text
Trains → Eligible Platforms
```

Request:

```json
{
  "leftNodes": [
    {
      "id": "T1",
      "label": "Train 12727"
    },
    {
      "id": "T2",
      "label": "Train 12728"
    }
  ],
  "rightNodes": [
    {
      "id": "P1",
      "label": "Platform 1"
    },
    {
      "id": "P2",
      "label": "Platform 2"
    }
  ],
  "edges": [
    {
      "left": "T1",
      "right": "P1"
    },
    {
      "left": "T1",
      "right": "P2"
    },
    {
      "left": "T2",
      "right": "P2"
    }
  ],
  "traceEnabled": true
}
```

Response:

```json
{
  "success": true,
  "data": {
    "matchingSize": 2,
    "matches": [
      {
        "left": "T1",
        "right": "P1"
      },
      {
        "left": "T2",
        "right": "P2"
      }
    ]
  },
  "meta": {
    "algorithm": "BIPARTITE_MATCHING"
  }
}
```

---

# 10.6 König's Theorem

### `POST /api/m4/konig`

Input:

```json
{
  "leftNodes": [],
  "rightNodes": [],
  "edges": []
}
```

Response:

```json
{
  "success": true,
  "data": {
    "maximumMatchingSize": 4,
    "minimumVertexCoverSize": 4,
    "vertexCover": {
      "left": [],
      "right": []
    },
    "verified": true
  }
}
```

The `verified` field means the implementation verified equality for the supplied bipartite graph.

---

# 10.7 Max-Flow Min-Cut

### `POST /api/m4/max-flow-min-cut`

Response:

```json
{
  "success": true,
  "data": {
    "maxFlow": 850,
    "minCutCapacity": 850,
    "cutEdges": [
      {
        "source": "S2",
        "target": "S5",
        "capacity": 300
      }
    ],
    "bottleneckNodes": [
      "S2"
    ],
    "verified": true
  },
  "meta": {
    "algorithm": "MAX_FLOW_MIN_CUT"
  }
}
```

This powers the railway bottleneck visualization.

---

# 11. M5 – NP-Completeness & Approximation

Base path:

```text
/api/m5
```

M5 endpoints are explicitly educational/analytical.

They must not claim that an NP-complete solver is an exact practical railway scheduler for large real-world networks.

---

# 11.1 SAT

### `POST /api/m5/sat`

Request:

```json
{
  "variables": [
    "T1_P1",
    "T1_P2",
    "T2_P1"
  ],
  "clauses": [
    ["T1_P1"],
    ["!T1_P1", "T1_P2"],
    ["!T2_P1"]
  ],
  "traceEnabled": true
}
```

Response:

```json
{
  "success": true,
  "data": {
    "satisfiable": true,
    "assignment": {
      "T1_P1": true,
      "T1_P2": false,
      "T2_P1": false
    }
  },
  "meta": {
    "algorithm": "SAT"
  }
}
```

---

# 11.2 3-SAT

### `POST /api/m5/3sat`

Request:

```json
{
  "variables": ["A", "B", "C"],
  "clauses": [
    ["A", "!B", "C"],
    ["!A", "B", "C"]
  ]
}
```

Response:

```json
{
  "success": true,
  "data": {
    "satisfiable": true,
    "assignment": {
      "A": true,
      "B": false,
      "C": true
    }
  }
}
```

---

# 11.3 3-SAT → CLIQUE

### `POST /api/m5/3sat-to-clique`

Request:

```json
{
  "variables": ["A", "B", "C"],
  "clauses": [
    ["A", "!B", "C"],
    ["!A", "B", "C"]
  ]
}
```

Response:

```json
{
  "success": true,
  "data": {
    "sourceProblem": "3-SAT",
    "targetProblem": "CLIQUE",
    "targetK": 2,
    "vertices": [],
    "edges": [],
    "mapping": []
  },
  "meta": {
    "algorithm": "3SAT_TO_CLIQUE"
  }
}
```

The transformation must be mathematically traceable.

---

# 11.4 CLIQUE → INDEPENDENT SET

### `POST /api/m5/clique-to-independent-set`

Request:

```json
{
  "vertices": ["A", "B", "C"],
  "edges": [
    ["A", "B"],
    ["B", "C"]
  ],
  "cliqueSize": 2
}
```

Response:

```json
{
  "success": true,
  "data": {
    "transformedVertices": ["A", "B", "C"],
    "transformedEdges": [],
    "independentSetSize": 2
  }
}
```

---

# 11.5 INDEPENDENT SET → VERTEX COVER

### `POST /api/m5/independent-set-to-vertex-cover`

Response:

```json
{
  "success": true,
  "data": {
    "vertexCount": 6,
    "independentSetSize": 4,
    "vertexCoverSize": 2,
    "vertexCover": ["B", "E"],
    "verified": true
  }
}
```

The implementation should verify:

```text
|Independent Set| + |Vertex Cover| = |V|
```

for the transformed/complement relationship where applicable.

---

# 11.6 Vertex-Cover 2-Approximation

### `POST /api/m5/vertex-cover-2approx`

Request:

```json
{
  "vertices": ["A", "B", "C", "D"],
  "edges": [
    ["A", "B"],
    ["B", "C"],
    ["C", "D"]
  ]
}
```

Response:

```json
{
  "success": true,
  "data": {
    "cover": ["B", "C"],
    "coverSize": 2,
    "edgeCount": 3,
    "bound": {
      "maximumAllowedRatio": 2
    }
  },
  "meta": {
    "algorithm": "VERTEX_COVER_2_APPROX"
  }
}
```

Do not label this result as optimal unless an independent exact method verifies optimality.

---

# 12. M6 – Randomized & Parallel Algorithms

Base path:

```text
/api/m6
```

---

# 12.1 Randomized QuickSort

### `POST /api/m6/randomized-quicksort`

Request:

```json
{
  "values": [45, 12, 78, 3, 19, 42],
  "seed": 42,
  "traceEnabled": true
}
```

Response:

```json
{
  "success": true,
  "data": {
    "sortedValues": [3, 12, 19, 42, 45, 78],
    "pivotSelections": [42, 12, 45],
    "comparisons": 12,
    "swaps": 7
  },
  "meta": {
    "algorithm": "RANDOMIZED_QUICKSORT"
  }
}
```

A seed is accepted to make demonstrations reproducible.

---

# 12.2 Reservoir Sampling

### `POST /api/m6/reservoir-sampling`

Request:

```json
{
  "stream": [
    "E1",
    "E2",
    "E3",
    "E4",
    "E5",
    "E6"
  ],
  "sampleSize": 3,
  "seed": 42,
  "traceEnabled": true
}
```

Response:

```json
{
  "success": true,
  "data": {
    "sample": [
      "E2",
      "E5",
      "E6"
    ],
    "streamSize": 6,
    "sampleSize": 3,
    "replacementCount": 2
  },
  "meta": {
    "algorithm": "RESERVOIR_SAMPLING"
  }
}
```

The production-style event simulator should also support incremental processing rather than requiring the entire stream in memory.

---

# 12.3 Miller-Rabin

### `POST /api/m6/miller-rabin`

Request:

```json
{
  "number": 1000000007,
  "rounds": 10,
  "seed": 42,
  "traceEnabled": true
}
```

Response:

```json
{
  "success": true,
  "data": {
    "number": 1000000007,
    "probablyPrime": true,
    "rounds": 10,
    "witnessesTested": 10
  },
  "meta": {
    "algorithm": "MILLER_RABIN"
  }
}
```

The response must use terminology such as `probablyPrime` rather than falsely claiming an unconditional proof of primality for probabilistic testing.

---

# 12.4 Blelloch Scan

### `POST /api/m6/blelloch-scan`

Request:

```json
{
  "values": [10, 20, 15, 5],
  "traceEnabled": true
}
```

Response:

```json
{
  "success": true,
  "data": {
    "exclusiveScan": [0, 10, 30, 45],
    "total": 50,
    "levels": [
      {
        "level": 0,
        "values": []
      }
    ]
  },
  "meta": {
    "algorithm": "BLELLOCH_SCAN"
  }
}
```

Railway use:

```text
event counts
passenger counts
station cumulative statistics
```

---

# 12.5 Parallel Reduce

### `POST /api/m6/parallel-reduce`

Request:

```json
{
  "values": [10, 20, 15, 5],
  "operation": "SUM",
  "workerCount": 4,
  "traceEnabled": true
}
```

Response:

```json
{
  "success": true,
  "data": {
    "result": 50,
    "operation": "SUM",
    "workerCount": 4,
    "partialResults": [30, 20]
  },
  "meta": {
    "algorithm": "PARALLEL_REDUCE"
  }
}
```

Supported operations should initially be:

```text
SUM
MIN
MAX
```

---

# 12.6 Brent's Theorem

### `POST /api/m6/brent`

Purpose:

Analyze parallel computation work and span.

Request:

```json
{
  "work": 1000,
  "span": 100,
  "processors": [1, 2, 4, 8, 16]
}
```

Response:

```json
{
  "success": true,
  "data": {
    "work": 1000,
    "span": 100,
    "estimates": [
      {
        "processors": 1,
        "estimatedTime": 1000
      },
      {
        "processors": 4,
        "estimatedTime": 250
      },
      {
        "processors": 16,
        "estimatedTime": 100
      }
    ]
  },
  "meta": {
    "algorithm": "BRENT_THEOREM"
  }
}
```

This is an analytical model of parallel execution, not a measurement of actual CPU runtime.

---

# 13. Algorithm Trace Contract

Every trace-capable algorithm should return structured trace objects.

Generic format:

```json
{
  "step": 1,
  "action": "COMPARE",
  "state": {
    "indexA": 4,
    "indexB": 7
  },
  "description": "Comparing current pattern character with text character."
}
```

Frontend visualizations must consume structured trace data.

The frontend must **not recreate algorithm logic** just to animate it.

---

# 14. Benchmark Contract

Every benchmarkable algorithm should expose:

```json
{
  "algorithm": "KMP",
  "inputSize": 10000,
  "executionTimeNanos": 128392,
  "operationCount": 8432
}
```

The system must distinguish:

### Theoretical complexity

Example:

```text
KMP
Time: O(n + m)
Space: O(m)
```

### Measured performance

Example:

```text
Input: 10,000 characters
Measured time: 128392 ns
```

Measured runtime must never be hardcoded.

---

# 15. Event WebSocket

Endpoint:

```text
/ws/events
```

Purpose:

Provide continuous railway-event updates.

Example event:

```json
{
  "eventId": "EV-10291",
  "eventType": "TRAIN_DELAY",
  "timestamp": "2026-10-02T18:25:00Z",
  "trainId": 12727,
  "stationId": 101,
  "payload": {
    "delayMinutes": 12
  }
}
```

Possible event types:

```text
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

# 16. Event Simulation

### `POST /api/events/simulate`

Request:

```json
{
  "eventType": "TRAIN_DELAY",
  "trainId": 12727,
  "stationId": 101,
  "payload": {
    "delayMinutes": 10
  }
}
```

Response:

```json
{
  "success": true,
  "data": {
    "eventId": "EV-10292",
    "published": true
  }
}
```

The event should be persisted and published through WebSocket.

---

# 17. Algorithm History

### `GET /api/algorithm-executions`

Purpose:

Retrieve benchmark/execution history.

Filters:

```text
algorithm
module
from
to
```

Example:

```text
GET /api/algorithm-executions?module=M4&algorithm=DINIC
```

Response:

```json
{
  "success": true,
  "data": {
    "items": [
      {
        "algorithm": "DINIC",
        "module": "M4",
        "inputSize": 100,
        "executionTimeNanos": 392031,
        "operationCount": 812
      }
    ]
  }
}
```

This data may be persisted in the optional `algorithm_execution` table.

---

# 18. Request Validation

The backend must validate all externally supplied inputs.

Examples:

### Empty string

```text
400 INVALID_INPUT
```

### Negative capacity

```text
400 INVALID_INPUT
```

### Invalid graph edge

```text
400 INVALID_INPUT
```

### Bitmask input above configured safe limit

```text
413 INPUT_TOO_LARGE
```

### Excessive trace request

```text
400 TRACE_LIMIT_EXCEEDED
```

### Missing station

```text
404 RESOURCE_NOT_FOUND
```

---

# 19. API Status Codes

Use standard HTTP semantics:

| Status | Meaning |
|---|---|
| `200` | Successful request |
| `201` | Resource created |
| `400` | Invalid input |
| `404` | Resource not found |
| `409` | Conflict |
| `413` | Input too large |
| `422` | Valid structure but invalid algorithm constraints |
| `500` | Unexpected server error |

---

# 20. Frontend API Organization

React should organize API calls by module.

```text
frontend/src/api/
├── client.js
├── stationsApi.js
├── trainsApi.js
├── tripsApi.js
├── alertsApi.js
├── documentsApi.js
├── m1Api.js
├── m2Api.js
├── m3Api.js
├── m4Api.js
├── m5Api.js
├── m6Api.js
└── eventsApi.js
```

Example:

```javascript
export async function runKMP(payload) {
  return apiClient.post("/m1/kmp", payload);
}
```

The frontend should not directly construct algorithm implementations.

---

# 21. Algorithm Result Contract

Every algorithm result should conceptually follow:

```text
AlgorithmRequest
        ↓
AlgorithmService
        ↓
AlgorithmResult
        ↓
REST Response
        ↓
React Visualization
```

Java-side conceptual structure:

```java
public interface Algorithm<I, O> {

    O execute(I input);

    String getName();

    String getTimeComplexity();

    String getSpaceComplexity();
}
```

Benchmark/trace functionality should be layered around the algorithm rather than duplicated inside every controller.

---

# 22. API-to-DSA Mapping

| Endpoint | DSA | Railway Purpose |
|---|---|---|
| `/m1/kmp` | KMP | Train/station/alert search |
| `/m1/z` | Z-Function | Operational pattern detection |
| `/m1/rabin-karp` | Rabin-Karp | Ticket/booking code search |
| `/m1/aho-corasick` | Aho-Corasick | Multi-keyword alert detection |
| `/m2/suffix-array` | Suffix Array | Document indexing |
| `/m2/sa-is` | SA-IS | Large text indexing |
| `/m2/lcp` | LCP | Repeated descriptions |
| `/m2/kasai` | Kasai | LCP construction |
| `/m2/suffix-automaton` | Suffix Automaton | Substring search |
| `/m3/levenshtein` | Levenshtein | Data correction |
| `/m3/damerau` | Damerau-Levenshtein | Typo correction |
| `/m3/bitmask` | Bitmask DP | Small route combinations |
| `/m3/matrix-chain` | Matrix Chain | Data-operation optimization |
| `/m3/optimal-bst` | Optimal BST | Frequently accessed records |
| `/m4/ford-fulkerson` | Ford-Fulkerson | Capacity analysis |
| `/m4/edmonds-karp` | Edmonds-Karp | Capacity analysis |
| `/m4/dinic` | Dinic | Large flow networks |
| `/m4/bipartite-matching` | Matching | Train-platform assignment |
| `/m4/konig` | König | Conflict analysis |
| `/m4/max-flow-min-cut` | Max-Flow Min-Cut | Bottleneck detection |
| `/m5/sat` | SAT | Scheduling constraints |
| `/m5/3sat` | 3-SAT | Constraint modeling |
| `/m5/3sat-to-clique` | Reduction | Complexity demonstration |
| `/m5/clique-to-independent-set` | Reduction | Conflict transformation |
| `/m5/independent-set-to-vertex-cover` | Reduction | Conflict transformation |
| `/m5/vertex-cover-2approx` | Approximation | Critical connections |
| `/m6/randomized-quicksort` | Randomized QuickSort | Record ranking |
| `/m6/reservoir-sampling` | Reservoir Sampling | Event-stream sampling |
| `/m6/miller-rabin` | Miller-Rabin | Numerical processing |
| `/m6/blelloch-scan` | Blelloch Scan | Cumulative statistics |
| `/m6/parallel-reduce` | Parallel Reduce | Aggregation |
| `/m6/brent` | Brent | Parallel performance analysis |

---

# 23. API Security Rules

Initial academic implementation:

- no real passenger PII
- no passwords stored in railway domain tables
- no secrets committed to Git
- environment variables for database credentials
- CORS restricted to known frontend origins
- request validation enabled
- database credentials never returned through APIs
- stack traces never returned to clients

Authentication/authorization should not be introduced unless required by the actual project scope.

---

# 24. Performance Rules

Algorithm endpoints must not silently substitute:

```text
PostgreSQL search
Java library implementation
Apache graph library
third-party algorithm
```

for the required DSA implementation.

For example:

```text
GET /api/stations?search=Vijay
```

may use database indexing.

But:

```text
POST /api/m1/kmp
```

must execute the manually implemented KMP algorithm.

This distinction is critical for DSA evaluation.

---

# 25. Frontend Visualization Requirements

Every M1–M6 algorithm page should expose:

```text
Input
↓
Run Algorithm
↓
Result
↓
Visualization
↓
Complexity
↓
Benchmark
↓
Trace
```

For example, M4 Dinic:

```text
Graph Input
     ↓
Run Dinic
     ↓
Maximum Flow
     ↓
Animated Residual Graph
     ↓
Bottleneck Highlight
     ↓
Execution Metrics
```

For M5:

```text
SAT Instance
     ↓
Transformation
     ↓
Generated Graph
     ↓
Result
     ↓
Reduction Visualization
```

For M6:

```text
Stream / Array
     ↓
Algorithm
     ↓
Parallel / Randomized Visualization
     ↓
Measured Statistics
```

---

# 26. API Contract Rules for Antigravity Agents

Agents must follow these rules.

### Rule 1

Do not change an endpoint path without updating this document.

### Rule 2

Do not independently invent response structures.

### Rule 3

Do not return raw Java objects whose structure was not intentionally defined.

### Rule 4

Do not move DSA computation into React.

### Rule 5

Do not replace required algorithms with library implementations.

### Rule 6

Do not hardcode benchmark values.

### Rule 7

Do not claim an approximation result is optimal.

### Rule 8

Do not claim probabilistic primality as absolute proof.

### Rule 9

Do not expose trace data without respecting `maxTraceSteps`.

### Rule 10

Any contract-breaking change requires updating:

```text
docs/04-api-contract.md
```

and the corresponding frontend/backend tests.

---

# 27. Definition of Done

The API contract is considered implemented when:

- [ ] domain endpoints work
- [ ] M1 endpoints work
- [ ] M2 endpoints work
- [ ] M3 endpoints work
- [ ] M4 endpoints work
- [ ] M5 endpoints work
- [ ] M6 endpoints work
- [ ] standard error handling works
- [ ] validation works
- [ ] benchmark metadata works
- [ ] trace limits work
- [ ] WebSocket events work
- [ ] frontend API clients consume the contracts
- [ ] integration tests cover every module
- [ ] API documentation matches implementation
- [ ] no endpoint secretly bypasses the required DSA implementation
- [ ] Postman/API test collection passes

---

# 28. Implementation Order

Antigravity should implement APIs in this order:

```text
1. Health Check
      ↓
2. Station Domain API
      ↓
3. Train/Route/Trip APIs
      ↓
4. M1 API
      ↓
5. M2 API
      ↓
6. M3 API
      ↓
7. M4 API
      ↓
8. M5 API
      ↓
9. M6 API
      ↓
10. Event WebSocket
      ↓
11. Benchmark History
      ↓
12. Full Integration Testing
```

The first vertical slice should therefore be:

```text
PostgreSQL
    ↓
Station Repository
    ↓
Station Service
    ↓
GET /api/stations
    ↓
React Station Page
```

Then:

```text
KMP Algorithm
    ↓
KMP Service
    ↓
POST /api/m1/kmp
    ↓
React KMP Visualizer
```

This gives Antigravity a working backend/frontend foundation before the remaining DSA modules are added.