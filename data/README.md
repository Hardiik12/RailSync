# RailSync — Real Railway Data Foundation

## Overview
This directory contains the public/real railway dataset snapshots and processing artifacts used by RailSync as its factual foundation.

RailSync uses real railway dataset snapshots for static infrastructure facts (stations, codes, names, coordinates, routes, train schedules, trips, stop times) while simulating dynamic operational data (delays, passenger demand, platform occupancy, maintenance events) at runtime.

---

## Dataset Provenance

- **Dataset Name:** Indian Railways Station, Train & Timetable Snapshot
- **Source:** Public Railway Open Data / NTES-derived Timetable Snapshot
- **Extraction Date:** 2026-10-05
- **Dataset Version:** v1.1.0-snapshot
- **License / Usage:** Open Government Data / Public Timetable Data (Academic and Educational Use)

---

## Directory Layout

```text
data/
├── raw/
│   └── railway-snapshot/
│       ├── indian-railways-stations.geojson
│       └── indian-railways-trains.json
├── processed/
│   └── stations-sample.json
└── README.md
```

---

## Files Description

1. **`raw/railway-snapshot/indian-railways-stations.geojson`**
   - GeoJSON FeatureCollection containing public station records across Indian Railways zones.
   - Contains station codes, station names, cities, states, and GPS coordinates (`[longitude, latitude]`).

2. **`raw/railway-snapshot/indian-railways-trains.json`**
   - JSON array containing public train records, running days, route stop sequences, scheduled arrival/departure times, and distances.

3. **`processed/stations-sample.json`**
   - Pre-parsed and normalized JSON array of raw station records used for fast inspection, test fixtures, and batch seed imports.

---

## Fields Mapped into RailSync

### Station Domain
| Raw Property Name | RailSync Field | Type | Description |
| :--- | :--- | :--- | :--- |
| `stationCode` / `code` | `stationCode` | String | Unique station identifier (e.g. `NDLS`, `CSMT`, `MAS`) |
| `name` / `STATION_NAME` | `name` | String | Full official station name |
| `city` | `city` | String | City or municipal location |
| `state` | `state` | String | Indian State / Union Territory |
| `coordinates[1]` | `latitude` | Double | GPS Latitude coordinate |
| `coordinates[0]` | `longitude` | Double | GPS Longitude coordinate |

### Train & Route Domain
| Raw Property Name | RailSync Field | Type | Description |
| :--- | :--- | :--- | :--- |
| `trainNumber` | `trainNumber` | String | Unique train service number (e.g. `12951`) |
| `trainName` | `trainName` | String | Train name (e.g. `Mumbai Rajdhani Express`) |
| `trainType` | `trainType` | String | Train classification (`RAJDHANI`, `SHATABDI`, etc.) |
| `sourceStationCode` | `sourceStation` | FK -> Station | Resolved origin station |
| `destinationStationCode` | `destinationStation` | FK -> Station | Resolved terminus station |
| `runningDays` | `runningDays` | String | Service operating frequency (`DAILY`, `MON,TUE...`) |
| `distanceKm` | `distanceKm` | Double | Total journey distance in kilometers |

### RouteStop & Timetable Domain
| Raw Property Name | RailSync Field | Type | Description |
| :--- | :--- | :--- | :--- |
| `stationCode` | `station` | FK -> Station | Resolved station at stop |
| `sequenceOrder` | `stopSequence` | Integer | 1-based index order along journey |
| `arrivalTime` | `scheduledArrival` | String | Scheduled arrival time (`HH:mm`) |
| `departureTime` | `scheduledDeparture` | String | Scheduled departure time (`HH:mm`) |
| `distanceKm` | `distanceFromOrigin` | Double | Distance from route origin in kilometers |
| Calculated | `scheduledDwellMinutes` | Integer | Derived from `departureTime - arrivalTime` (0 at origin/terminus) |

### Trip & StopTime Normalization Domain
| Domain Entity | Primary Purpose | Key Fields |
| :--- | :--- | :--- |
| **`Trip`** | Service occurrence instance for a train | `id`, `train_id`, `serviceDate`, `scheduledStatus`, `actualStatus` |
| **`StopTime`** | Timetable instance per trip stop | `id`, `trip_id`, `station_id`, `stopSequence`, `scheduledArrival`, `scheduledDeparture`, `actualArrival`, `actualDeparture`, `arrivalDelayMinutes`, `departureDelayMinutes` |

---

## Factual vs. Simulated Data Boundary

To maintain domain fidelity without fabricating missing facts, the distinction between factual data and dynamic simulated metrics is enforced as follows:

| Classification | Category | Description |
| :--- | :--- | :--- |
| **Factual Data** | Station identities, codes, coordinates, train numbers, names, route stop ordering, scheduled timetables | Ingested directly from public railway snapshots |
| **Simulated Data** | Live delays, actual arrival/departure timestamps, passenger demand counts, platform occupancy, maintenance events | Generated dynamically by RailSync operational engines & DSA modules |

---

## Ingestion Architecture

```text
Raw GeoJSON / JSON Dataset
           ↓
RawStationRecord / RawTrainRecord / RawStopRecord DTOs
           ↓
DatasetInspector (Validation & Auditing)
           ↓
StationNormalizer & TrainNormalizer
           ↓
TrainDataImportService (Batch Idempotent Persistence)
           ↓
STATION → TRAIN → ROUTE → ROUTE_STOP → TRIP → STOP_TIME
```
