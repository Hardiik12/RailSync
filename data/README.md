# RailSync — Real Railway Data Foundation

## Overview
This directory contains the public/real railway dataset snapshots and processing artifacts used by RailSync as its factual foundation.

RailSync uses real railway dataset snapshots for static infrastructure facts (stations, codes, names, coordinates, routes, train schedules) while simulating dynamic operational data (delays, passenger demand, platform occupancy, maintenance events) at runtime.

---

## Dataset Provenance

- **Dataset Name:** Indian Railways Station & Network Snapshot
- **Source:** Public Railway Open Data / NTES-derived Timetable Snapshot
- **Extraction Date:** 2026-10-05
- **Dataset Version:** v1.0.0-snapshot
- **License / Usage:** Open Government Data / Public Timetable Data (Academic and Educational Use)

---

## Directory Layout

```text
data/
├── raw/
│   └── railway-snapshot/
│       └── indian-railways-stations.geojson
├── processed/
│   └── stations-sample.json
└── README.md
```

---

## Files Description

1. **`raw/railway-snapshot/indian-railways-stations.geojson`**
   - GeoJSON FeatureCollection containing public station records across Indian Railways zones.
   - Contains station codes, station names, cities, states, and GPS coordinates (`[longitude, latitude]`).

2. **`processed/stations-sample.json`**
   - Pre-parsed and normalized JSON array of raw station records used for fast inspection, test fixtures, and batch seed imports.

---

## Fields Mapped into RailSync

| Raw Property Name | RailSync Field | Type | Description |
| :--- | :--- | :--- | :--- |
| `stationCode` / `code` | `stationCode` | String | Unique station identifier (e.g. `NDLS`, `CSMT`, `MAS`) |
| `name` / `STATION_NAME` | `name` | String | Full official station name |
| `city` | `city` | String | City or municipal location |
| `state` | `state` | String | Indian State / Union Territory |
| `coordinates[1]` | `latitude` | Double | GPS Latitude coordinate |
| `coordinates[0]` | `longitude` | Double | GPS Longitude coordinate |
| `sourceDataset` | `sourceDataset` | String | Origin dataset metadata identifier |
| — | `dataOrigin` | String | Fixed to `PUBLIC_DATA` for imported records |

---

## Fields Intentionally Not Mapped (Factual Boundary)

To maintain domain fidelity without fabricating missing facts, the following fields are **not** inferred from static public data and are instead managed by RailSync's dynamic simulation engine:

- **Platform Allocation Schedules:** Dynamic platform assignment based on real-time train length and conflict graphs.
- **Live Delays & Occupancy:** Generated at runtime by real-time event engines and DSA simulation modules.
- **Maintenance Records:** Simulated operational status events.

---

## Ingestion Architecture

```text
Raw GeoJSON / JSON Dataset
           ↓
   RawStationRecord DTO
           ↓
    DatasetInspector (Validation & Auditing)
           ↓
   StationNormalizer (Whitespace, Code Normalization)
           ↓
StationDataImportService (Batch JPA Persistence)
           ↓
    PostgreSQL Database
```
