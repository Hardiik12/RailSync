# RailSync Data Sources

## Public dataset — station foundation

**Dataset:** Indian Railways Dataset  
**Source:** Kaggle — Sripaad Srinivasan  
**URL:** https://www.kaggle.com/sripaadsrinivasan/indian-railways-dataset  
**Type:** Public railway dataset / historical snapshot  
**Published license:** CC0 / Public Domain as listed by the dataset data card.

The published station structure describes GeoJSON features with station name, station code, state, zone, address and point coordinates.

**Limitations:** public/historical snapshot; not live railway operations.

## Synthetic data

Passengers, passenger codes, tickets, booking codes, passenger counts, platform occupancy, maintenance activity, simulated delays, railway event streams, simulated service alerts and simulated network capacities remain synthetic.

## Data boundary

Use “public railway dataset” or “public railway snapshot”. Do not call it live or real-time Indian Railways data.

## Reproducibility

Synthetic Flyway seed data remains available and is not replaced by external imports. Public-data import is optional and idempotent.
