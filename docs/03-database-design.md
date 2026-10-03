# RailSync — Database Design

## 1. Database Overview

RailSync uses PostgreSQL as its primary persistent data store.

The database stores:

- railway infrastructure
- train services
- routes and trips
- station schedules
- platforms
- passengers
- tickets
- service alerts
- maintenance records
- railway events
- railway documents

The DSA engine retrieves appropriate data from PostgreSQL and performs the required algorithms in Java.

---

# 2. Database Principles

1. Use PostgreSQL.
2. Use normalized relational tables.
3. Use foreign keys for relationships.
4. Use constraints to prevent invalid railway data.
5. Use indexes for normal application retrieval.
6. Do not use database search as a replacement for M1 algorithms.
7. Do not use database graph extensions as a replacement for M4 algorithms.
8. Do not use PostgreSQL functions to hide M1–M6 implementations.
9. Keep algorithm execution primarily in Java.
10. Use deterministic seed data for reproducible demonstrations.
11. Use synthetic passenger data.
12. Do not store unnecessary sensitive passenger information.

---

# 3. Entity Relationship Overview

```text
                         STATION
                           │
              ┌────────────┼─────────────┐
              │            │             │
              ↓            ↓             ↓
          PLATFORM      STOP_TIME     RAILWAY_EVENT
                           │
                           │
                         TRIP
                           │
                           ↓
                         TRAIN
                           │
              ┌────────────┼────────────┐
              ↓            ↓            ↓
           TICKET      MAINTENANCE    ALERT
              │
              ↓
          PASSENGER


STATION ───── ROUTE_STOP ───── ROUTE ───── TRAIN


RAILWAY_DOCUMENT
        │
        ↓
   M2 SUFFIX ENGINE
```

---

# 4. Naming Convention

Database:

- lowercase
- snake_case

Examples:

```text
station
train
stop_time
service_alert
maintenance_record
railway_event
railway_document
```

Primary keys:

```text
id
```

Foreign keys:

```text
station_id
train_id
trip_id
platform_id
passenger_id
```

---

# 5. ID Strategy

Use PostgreSQL-generated IDs.

Recommended primary key:

```text
BIGINT
```

or UUID if the implementation requires distributed identifiers.

For the initial modular monolith, BIGINT is preferred for simplicity and efficient joins.

Public railway identifiers such as:

```text
station_code
train_number
booking_code
```

are separate business fields and must not be used as primary keys.

---

# 6. `station`

Stores railway station information.

```sql
station
-------
id                  BIGINT PRIMARY KEY
station_code        VARCHAR(20) UNIQUE NOT NULL
name                VARCHAR(150) NOT NULL
city                VARCHAR(100) NOT NULL
state               VARCHAR(100)
latitude            DECIMAL(9,6)
longitude           DECIMAL(9,6)
platform_count      INTEGER NOT NULL
passenger_capacity  INTEGER
status              VARCHAR(30) NOT NULL
created_at          TIMESTAMP NOT NULL
updated_at          TIMESTAMP NOT NULL
```

---

## Constraints

```text
platform_count >= 0
passenger_capacity >= 0
status ∈ {ACTIVE, INACTIVE, MAINTENANCE}
```

---

## Indexes

```text
UNIQUE(station_code)

INDEX(station_code)

INDEX(name)

INDEX(city)
```

The `name` index supports ordinary database retrieval.

M1 KMP remains responsible for algorithm-lab pattern matching.

---

# 7. `platform`

Represents platforms belonging to a station.

```sql
platform
--------
id                  BIGINT PRIMARY KEY
station_id          BIGINT NOT NULL
platform_number     VARCHAR(20) NOT NULL
capacity             INTEGER
platform_type       VARCHAR(30)
status              VARCHAR(30) NOT NULL
created_at          TIMESTAMP NOT NULL
updated_at          TIMESTAMP NOT NULL
```

Foreign key:

```text
platform.station_id
→ station.id
```

---

## Constraints

```text
capacity >= 0

UNIQUE(
    station_id,
    platform_number
)
```

---

# 8. `train`

Stores train/service information.

```sql
train
-----
id                    BIGINT PRIMARY KEY
train_number          VARCHAR(20) UNIQUE NOT NULL
train_name            VARCHAR(150) NOT NULL
train_type            VARCHAR(50)
source_station_id     BIGINT NOT NULL
destination_station_id BIGINT NOT NULL
capacity              INTEGER NOT NULL
status                VARCHAR(30) NOT NULL
current_station_id    BIGINT
delay_minutes         INTEGER NOT NULL DEFAULT 0
created_at            TIMESTAMP NOT NULL
updated_at            TIMESTAMP NOT NULL
```

Foreign keys:

```text
source_station_id
→ station.id

destination_station_id
→ station.id

current_station_id
→ station.id
```

---

## Constraints

```text
capacity > 0

delay_minutes >= 0

source_station_id != destination_station_id

status ∈ {
    ACTIVE,
    DELAYED,
    CANCELLED,
    COMPLETED,
    MAINTENANCE
}
```

---

## Indexes

```text
UNIQUE(train_number)

INDEX(train_name)

INDEX(status)

INDEX(current_station_id)

INDEX(source_station_id)

INDEX(destination_station_id)
```

---

# 9. `route`

Represents the planned station sequence of a train service.

```sql
route
-----
id                  BIGINT PRIMARY KEY
train_id            BIGINT NOT NULL
route_name          VARCHAR(150)
distance_km         DECIMAL(10,2)
status              VARCHAR(30)
created_at          TIMESTAMP NOT NULL
updated_at          TIMESTAMP NOT NULL
```

Foreign key:

```text
train_id → train.id
```

A train may have one or more route definitions depending on the project model.

---

# 10. `route_stop`

Represents the ordered stations in a route.

```sql
route_stop
----------
id                  BIGINT PRIMARY KEY
route_id            BIGINT NOT NULL
station_id          BIGINT NOT NULL
stop_sequence       INTEGER NOT NULL
distance_from_origin DECIMAL(10,2)
scheduled_dwell_minutes INTEGER
```

Foreign keys:

```text
route_id → route.id

station_id → station.id
```

Constraints:

```text
stop_sequence > 0

UNIQUE(
    route_id,
    stop_sequence
)
```

Indexes:

```text
INDEX(route_id)

INDEX(station_id)

INDEX(route_id, stop_sequence)
```

---

# 11. `trip`

A trip represents a scheduled operational instance of a train service.

```sql
trip
----
id                  BIGINT PRIMARY KEY
train_id            BIGINT NOT NULL
service_date        DATE NOT NULL
scheduled_status    VARCHAR(30) NOT NULL
actual_status       VARCHAR(30)
created_at          TIMESTAMP NOT NULL
updated_at          TIMESTAMP NOT NULL
```

Foreign key:

```text
train_id → train.id
```

Indexes:

```text
INDEX(train_id)

INDEX(service_date)

INDEX(train_id, service_date)
```

---

# 12. `stop_time`

Stores scheduled/actual arrival and departure data for a trip.

```sql
stop_time
---------
id                    BIGINT PRIMARY KEY
trip_id               BIGINT NOT NULL
station_id            BIGINT NOT NULL
platform_id           BIGINT
stop_sequence         INTEGER NOT NULL
scheduled_arrival     TIMESTAMP
scheduled_departure   TIMESTAMP
actual_arrival        TIMESTAMP
actual_departure      TIMESTAMP
arrival_delay_minutes INTEGER DEFAULT 0
departure_delay_minutes INTEGER DEFAULT 0
```

Foreign keys:

```text
trip_id → trip.id

station_id → station.id

platform_id → platform.id
```

Constraints:

```text
stop_sequence > 0

arrival_delay_minutes >= 0

departure_delay_minutes >= 0

UNIQUE(
    trip_id,
    stop_sequence
)
```

---

# 13. `passenger`

Stores synthetic passenger information.

```sql
passenger
---------
id              BIGINT PRIMARY KEY
passenger_code  VARCHAR(30) UNIQUE NOT NULL
full_name       VARCHAR(150) NOT NULL
age             INTEGER
gender          VARCHAR(30)
created_at      TIMESTAMP NOT NULL
updated_at      TIMESTAMP NOT NULL
```

For the academic project, contact information is optional and should not be required.

If contact information is implemented:

```text
email
phone
```

should be treated as sensitive application data and should not be exposed unnecessarily in algorithm demonstrations.

---

# 14. `ticket`

Represents a passenger booking.

```sql
ticket
------
id                  BIGINT PRIMARY KEY
booking_code        VARCHAR(30) UNIQUE NOT NULL
passenger_id        BIGINT NOT NULL
train_id            BIGINT NOT NULL
source_station_id   BIGINT NOT NULL
destination_station_id BIGINT NOT NULL
coach               VARCHAR(20)
seat_number         VARCHAR(20)
booking_status      VARCHAR(30) NOT NULL
booking_time        TIMESTAMP NOT NULL
created_at          TIMESTAMP NOT NULL
updated_at          TIMESTAMP NOT NULL
```

Foreign keys:

```text
passenger_id → passenger.id

train_id → train.id

source_station_id → station.id

destination_station_id → station.id
```

Indexes:

```text
UNIQUE(booking_code)

INDEX(train_id)

INDEX(passenger_id)

INDEX(source_station_id)

INDEX(destination_station_id)

INDEX(booking_time)
```

---

# 15. M1 Database Usage

M1 uses database data as algorithm input.

## KMP

Potential source fields:

```text
train.train_number
train.train_name
station.name
service_alert.title
service_alert.description
```

Flow:

```text
PostgreSQL
    ↓
Java service
    ↓
Extract text
    ↓
KMP
```

The database query itself must not perform the KMP search.

---

# 16. Rabin-Karp Database Usage

Primary source:

```text
ticket.booking_code
```

Example:

```text
SELECT booking_code
FROM ticket;
```

Then:

```text
Java
 ↓
Rabin-Karp
 ↓
matching booking codes
```

For large benchmark datasets, records may be streamed or processed in batches.

---

# 17. Aho-Corasick Database Usage

Source:

```text
service_alert.title
service_alert.description
```

Keyword dictionary is application configuration or database-backed configuration.

Example:

```text
delay
cancelled
diverted
maintenance
platform
rescheduled
```

Flow:

```text
Alerts
 ↓
Text extraction
 ↓
Aho-Corasick
 ↓
Detected keywords
 ↓
Severity analysis
```

---

# 18. `service_alert`

```sql
service_alert
-------------
id                  BIGINT PRIMARY KEY
title               VARCHAR(200) NOT NULL
description         TEXT NOT NULL
severity            VARCHAR(30) NOT NULL
status              VARCHAR(30) NOT NULL
train_id            BIGINT
station_id          BIGINT
created_at          TIMESTAMP NOT NULL
updated_at          TIMESTAMP NOT NULL
```

Foreign keys:

```text
train_id → train.id

station_id → station.id
```

Indexes:

```text
INDEX(train_id)

INDEX(station_id)

INDEX(severity)

INDEX(created_at)
```

---

# 19. `maintenance_record`

```sql
maintenance_record
------------------
id                  BIGINT PRIMARY KEY
train_id            BIGINT
resource_type       VARCHAR(100) NOT NULL
description         TEXT
priority            VARCHAR(30) NOT NULL
start_time          TIMESTAMP NOT NULL
end_time            TIMESTAMP
status              VARCHAR(30) NOT NULL
created_at          TIMESTAMP NOT NULL
updated_at          TIMESTAMP NOT NULL
```

Foreign key:

```text
train_id → train.id
```

---

# 20. `railway_event`

This is one of the most important tables for M1 and M6.

```sql
railway_event
-------------
id                  BIGINT PRIMARY KEY
event_type          VARCHAR(50) NOT NULL
train_id            BIGINT
station_id          BIGINT
passenger_count     INTEGER
event_time          TIMESTAMP NOT NULL
metadata            JSONB
created_at          TIMESTAMP NOT NULL
```

Foreign keys:

```text
train_id → train.id

station_id → station.id
```

Indexes:

```text
INDEX(event_type)

INDEX(event_time)

INDEX(train_id)

INDEX(station_id)

INDEX(event_type, event_time)
```

---

# 21. Event Type Constraint

Allowed event types:

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

The implementation may use a PostgreSQL enum or a VARCHAR with application-level validation.

For easier migrations, VARCHAR plus application validation is acceptable.

---

# 22. M1 Z-Function Event Data

Z-function uses event sequences.

Example:

```text
TRAIN_DELAYED
PASSENGER_ENTRY
TRAIN_DEPARTED
TRAIN_DELAYED
PASSENGER_ENTRY
TRAIN_DEPARTED
```

The service converts event types into a sequence.

Example representation:

```text
D E P D E P D E P
```

or tokenized event objects.

The Z-function operates on the prepared sequence.

The event table remains responsible only for persistence.

---

# 23. `railway_document`

Stores railway text.

```sql
railway_document
----------------
id                  BIGINT PRIMARY KEY
title               VARCHAR(250) NOT NULL
category            VARCHAR(50) NOT NULL
content             TEXT NOT NULL
source              VARCHAR(250)
document_version    VARCHAR(50)
created_at          TIMESTAMP NOT NULL
updated_at          TIMESTAMP NOT NULL
```

Categories:

```text
TIMETABLE
STATION_INFO
SERVICE_NOTICE
MAINTENANCE
OPERATIONS
SAFETY
SERVICE_ALERT
GENERAL
```

Indexes:

```text
INDEX(category)

INDEX(created_at)
```

Do not use PostgreSQL full-text search as a substitute for M2.

---

# 24. M2 Document Pipeline

```text
railway_document
       ↓
DocumentService
       ↓
content extraction
       ↓
normalization
       ↓
Suffix Array / SA-IS
       ↓
LCP / Kasai
       ↓
substring / repetition analysis
```

---

# 25. Suffix Index Persistence

Initial implementation:

The suffix array is generated in memory.

Do not persist every suffix as a database row.

Reason:

For a document of length N, storing every suffix as an individual relational record would create unnecessary database overhead.

Preferred:

```text
PostgreSQL
   ↓
Document text
   ↓
Java
   ↓
Suffix structure in memory
```

Optional future optimization:

A serialized index may be stored for large documents.

That optimization is not part of the initial implementation.

---

# 26. M3 Database Usage

## Levenshtein / Damerau-Levenshtein

Candidate values:

```text
station.name
passenger.full_name
train.train_name
```

Flow:

```text
User Input
   ↓
Candidate retrieval
   ↓
DP distance calculation
   ↓
Rank suggestions
```

The database provides candidates.

The DP algorithm calculates distances.

---

# 27. Bitmask DP Data

Bitmask DP may use:

```text
train
platform
trip
route_stop
```

as input.

However, the optimization instance must be explicitly bounded.

Example:

```text
selectedTrainIds
selectedPlatformIds
constraints
```

The database is not asked to solve the DP problem.

---

# 28. Matrix-Chain Data

Matrix metadata may be stored separately if required.

Optional table:

```sql
matrix_operation
----------------
id                  BIGINT PRIMARY KEY
name                VARCHAR(150) NOT NULL
rows_count          INTEGER NOT NULL
columns_count       INTEGER NOT NULL
sequence_order      INTEGER NOT NULL
```

This table exists only if the Matrix-Chain demonstration needs persistent configurations.

Otherwise, matrix dimensions may be supplied directly through the API.

Prefer API input for the initial version.

---

# 29. Optimal BST Data

Access frequencies may be generated from historical/simulated queries.

Optional table:

```sql
record_access_stat
------------------
id                  BIGINT PRIMARY KEY
record_type         VARCHAR(50) NOT NULL
record_id           BIGINT NOT NULL
access_count        BIGINT NOT NULL
period_start        TIMESTAMP
period_end          TIMESTAMP
```

This table is optional for Phase 1.

The first implementation may use generated frequencies.

---

# 30. M4 Graph Data

The railway network can be derived from:

```text
route_stop
+
station
```

Connections are derived from consecutive route stops.

Example:

```text
Station A
   ↓
Station B
   ↓
Station C
```

becomes:

```text
A → B
B → C
```

Capacity is derived from railway/service data or synthetic configuration.

---

# 31. Do Not Store Algorithm Graph Objects Directly

Do not store Java objects such as:

```text
Graph
ResidualGraph
FlowNetwork
```

inside PostgreSQL.

Database stores railway facts.

Java constructs:

```text
Graph
```

from those facts.

Flow algorithm:

```text
PostgreSQL
 ↓
Railway graph data
 ↓
GraphBuilder
 ↓
FlowNetwork
 ↓
Dinic
```

---

# 32. Optional `network_edge`

If explicit network capacities need to be configured independently from route data, create:

```sql
network_edge
------------
id                  BIGINT PRIMARY KEY
source_station_id   BIGINT NOT NULL
destination_station_id BIGINT NOT NULL
capacity            INTEGER NOT NULL
current_load        INTEGER DEFAULT 0
active              BOOLEAN NOT NULL
created_at          TIMESTAMP NOT NULL
updated_at          TIMESTAMP NOT NULL
```

Foreign keys:

```text
source_station_id → station.id

destination_station_id → station.id
```

Constraints:

```text
capacity >= 0

current_load >= 0

current_load <= capacity
```

This table is recommended for the M4 simulator because it gives us explicit control over graph capacities.

---

# 33. M4 Flow Data Pipeline

```text
network_edge
     ↓
GraphBuilder
     ↓
FlowNetwork
     ↓
Ford-Fulkerson
     │
     ├── Edmonds-Karp
     │
     └── Dinic
     ↓
FlowResult
     ↓
Frontend graph visualization
```

---

# 34. Platform Matching Data

Platform matching derives its bipartite graph from:

```text
trip / stop_time
+
platform
```

Eligibility conditions may include:

- station compatibility
- platform status
- platform type
- scheduled time conflict
- service constraints

The application creates:

```text
Train/Trip node
      ↕
Eligibility edge
      ↕
Platform node
```

Then runs bipartite matching.

---

# 35. M5 Scheduling Data

Scheduling constraints are generated from:

```text
trip
stop_time
platform
```

Possible conflicts:

```text
same platform
same time window
incompatible platform
ordering constraint
service dependency
```

The database provides facts.

The M5 engine transforms those facts into Boolean variables and clauses.

---

# 36. SAT Representation

Example variable:

```text
X(T1,P1)
```

meaning:

```text
Train T1 is assigned to Platform P1.
```

Example clause:

```text
X(T1,P1) OR X(T1,P2)
```

The database does not store the SAT formula by default.

The application generates it from scheduling constraints.

---

# 37. M5 Conflict Graph

Conflict graph vertices represent:

```text
train/service
```

Edges represent:

```text
conflict
```

Example:

```text
T1 ─── T2
T2 ─── T3
T1 ─── T4
```

The graph is constructed in Java.

Then:

```text
CLIQUE
Independent Set
Vertex Cover
```

operate on the graph.

---

# 38. M6 Event Streaming

M6 requires a continuous stream.

Database events may be read in time order:

```text
ORDER BY event_time, id
```

However, the streaming simulator should not repeatedly query the entire event table.

Use a controlled event generator:

```text
EventGenerator
      ↓
EventProcessor
      ↓
Reservoir
      ↓
Analytics
```

The database may persist generated events asynchronously.

---

# 39. Reservoir Sampling Persistence

The reservoir itself should remain in memory for the initial implementation.

Do not write every reservoir update to PostgreSQL.

Persist only:

- final sample
- experiment metadata
- optional benchmark results

when the user explicitly saves an experiment.

---

# 40. Passenger Analytics

Passenger events can be aggregated by:

```text
station
time interval
event type
train
```

Example:

```text
PASSENGER_ENTRY
```

produces a count stream:

```text
20
15
30
40
25
```

Then:

```text
Blelloch Scan
```

produces cumulative values.

---

# 41. Reduce Data

Parallel Reduce can aggregate:

```text
passenger counts
ticket counts
train delays
station events
```

Example:

```text
Station A = 120
Station B = 340
Station C = 210
Station D = 450
```

Reduce:

```text
TOTAL = 1120
```

---

# 42. M6 Benchmark Data

Benchmarks should not modify operational railway tables.

Use separate runtime structures or:

```text
algorithm_execution
```

if persistence is enabled.

Benchmark data must include:

```text
algorithm
input_size
execution_time
operation_count
timestamp
```

---

# 43. Database Index Strategy

Indexes should support ordinary application access.

Required initial indexes:

```text
station.station_code
station.name
station.city

train.train_number
train.train_name
train.status
train.current_station_id

route.train_id

route_stop.route_id
route_stop.station_id
route_stop.route_id + stop_sequence

trip.train_id
trip.service_date

stop_time.trip_id
stop_time.station_id
stop_time.trip_id + stop_sequence

ticket.booking_code
ticket.train_id
ticket.passenger_id

service_alert.train_id
service_alert.station_id
service_alert.severity
service_alert.created_at

railway_event.event_type
railway_event.event_time
railway_event.train_id
railway_event.station_id

railway_document.category
railway_document.created_at

network_edge.source_station_id
network_edge.destination_station_id
```

---

# 44. Important Indexing Rule

Do not create an index for every column.

Indexes have:

- storage cost
- insert/update cost
- maintenance cost

Create them for realistic retrieval patterns.

---

# 45. Foreign Key Rules

Use foreign keys for all actual relationships.

Examples:

```text
platform.station_id
→ station.id

train.source_station_id
→ station.id

ticket.passenger_id
→ passenger.id

ticket.train_id
→ train.id

stop_time.trip_id
→ trip.id
```

Avoid circular dependencies.

---

# 46. Delete Rules

Default policy:

Do not cascade-delete historical railway records casually.

Prefer:

```text
status = INACTIVE
```

or:

```text
status = CANCELLED
```

for operational entities.

Historical:

- tickets
- events
- maintenance records
- alerts

should generally remain available for analysis.

---

# 47. Timestamp Rules

Store timestamps consistently.

Recommended:

```text
TIMESTAMP WITH TIME ZONE
```

for operational events and time-sensitive records.

Application should convert to the user's display timezone.

Do not mix timezone-aware and timezone-unaware timestamps without a clear reason.

---

# 48. Seed Dataset

The initial deterministic seed should include approximately:

```text
Stations:             30–50
Platforms:            100+
Trains:               50+
Routes:               50+
Route Stops:          300+
Trips:                100+
Stop Times:           500+
Passengers:           5,000+
Tickets:              10,000+
Alerts:               500+
Maintenance Records:  100+
Railway Events:       20,000+
Documents:            100+
Network Edges:        100+
```

These are development targets, not fixed requirements.

Actual generated counts must be reported.

---

# 49. Seed Data Requirements

Seed data must be:

- deterministic
- internally consistent
- relationally valid
- realistic enough for demonstrations
- large enough for algorithm benchmarks

Example:

If a train references station `STA001`, that station must exist.

If a ticket references train `TR001`, that train must exist.

If a stop time references platform `P01`, that platform must belong to the relevant station.

---

# 50. Synthetic Data Rules

Do not use real passenger personal information.

Passenger names should be synthetic.

Ticket/booking codes should be generated.

Station/train data may use fictional or clearly synthetic records.

If real public railway names are used, the operational data should still be identified as simulated unless sourced from a verified public dataset.

---

# 51. Database Migration Structure

Recommended:

```text
backend/src/main/resources/db/migration/
│
├── V1__create_station.sql
├── V2__create_platform.sql
├── V3__create_train.sql
├── V4__create_route.sql
├── V5__create_route_stop.sql
├── V6__create_trip.sql
├── V7__create_stop_time.sql
├── V8__create_passenger.sql
├── V9__create_ticket.sql
├── V10__create_service_alert.sql
├── V11__create_maintenance_record.sql
├── V12__create_railway_event.sql
├── V13__create_railway_document.sql
└── V14__create_network_edge.sql
```

The exact numbering can change during implementation.

Never edit an already-applied production migration casually.

Create a new migration for schema changes.

---

# 52. Seed Migration Strategy

Seed data should be separated from structural migrations.

Recommended:

```text
db/
├── migration/
└── seed/
```

The seed process should be executable independently.

Example:

```text
./mvnw ... seed
```

or an application seeder profile.

The final mechanism will be selected during implementation.

---

# 53. Database Transaction Rules

Use transactions for multi-table operations.

Example:

Creating a trip and its stop times:

```text
BEGIN
 ↓
create trip
 ↓
create stop times
 ↓
COMMIT
```

If any stop time fails validation:

```text
ROLLBACK
```

---

# 54. Concurrency

For the initial simulation:

- PostgreSQL handles persistence concurrency.
- WebSocket events are handled by Spring Boot.
- Algorithm computations are isolated from database transactions.

Do not hold database transactions open while running expensive algorithms.

Bad:

```text
BEGIN
 ↓
load graph
 ↓
run Dinic for several seconds
 ↓
update database
 ↓
COMMIT
```

Prefer:

```text
load data
 ↓
COMMIT/read complete
 ↓
build graph
 ↓
run algorithm
 ↓
persist result if required
```

---

# 55. Large Dataset Handling

For large M1/M2/M6 experiments:

Do not load unnecessary database columns.

Use projections/DTO queries where appropriate.

Example:

M1 train-number search only needs:

```text
train.id
train.train_number
```

It does not need:

```text
train.updated_at
train.capacity
train.current_station
...
```

---

# 56. Database → Algorithm Adapter

Each module should have a data adapter.

Example:

```text
M1:
RailwayTextProvider

M2:
RailwayDocumentProvider

M3:
DPInputProvider

M4:
RailwayGraphBuilder

M5:
ConstraintGraphBuilder

M6:
RailwayEventStreamProvider
```

These adapters isolate database structure from algorithm implementation.

---

# 57. M4 Graph Builder

Input:

```text
network_edge
```

Output:

```text
Graph
```

Conceptual Java model:

```text
Graph
 ├── Vertex
 └── Edge
      ├── capacity
      ├── flow
      └── reverseEdge
```

The actual algorithm implementation owns its residual-edge representation.

Do not force database entities into the flow algorithm.

---

# 58. M5 Constraint Builder

Input:

```text
trips
stop_times
platforms
```

Output:

```text
SchedulingConstraintModel
```

Then:

```text
SchedulingConstraintModel
        ↓
CNF
        ↓
3-SAT
```

This keeps railway semantics separate from complexity-theory implementations.

---

# 59. Data Access for M2

M2 requires potentially large text.

Use:

```text
RailwayDocumentRepository
```

to retrieve document content.

The suffix structures operate on:

```text
String
```

or a suitable character/token representation.

The initial implementation should process one selected document or controlled document collection at a time.

---

# 60. Data Access for M6

M6 should support two modes.

## Historical mode

Read persisted events:

```text
railway_event
```

## Live simulation mode

Generate events:

```text
EventGenerator
```

The two modes must share a common event-processing interface.

---

# 61. Future Partitioning

If the event table becomes very large, partitioning by event date may be considered.

This is not part of the initial implementation.

Do not prematurely introduce database partitioning.

---

# 62. Database Health Requirements

Before M1 begins:

[ ] PostgreSQL starts

[ ] All migrations run

[ ] All foreign keys work

[ ] Seed data loads

[ ] Referential integrity passes

[ ] Required indexes exist

[ ] Basic CRUD APIs work

[ ] M1 data extraction works

[ ] Database tests pass

---

# 63. Database Definition of Done

The database layer is complete when:

```text
PostgreSQL
     ↓
Valid railway schema
     ↓
Deterministic seed data
     ↓
Repositories
     ↓
Domain services
     ↓
Algorithm adapters
```

works without the algorithms depending directly on JPA entities.

The algorithm engine must receive clean algorithm-specific inputs.

---

# 64. Final Data Flow

The complete system should follow:

```text
                  POSTGRESQL
                       │
       ┌───────────────┼────────────────┐
       │               │                │
       ↓               ↓                ↓
 Railway Domain    Documents         Events
       │               │                │
       ↓               ↓                ↓
 Domain Services  DocumentService  EventService
       │               │                │
       ↓               ↓                ↓
      M1              M2               M6
       │
       ├──────────────→ M3
       │
       └──────────────→ M4 / M5
                         │
                         ↓
                   Algorithm Results
                         │
                         ↓
                       REST
                         │
                         ↓
                     React UI
```

The database is the source of railway-domain facts.

The Java algorithm engine is the source of algorithmic computation.

The React application is the source of visualization and user interaction.