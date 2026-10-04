# RailSync Data Directory

This directory is for externally sourced railway datasets used by the optional public-data import workflow.

## Modes

1. Synthetic mode — deterministic Flyway seed data for development, CI, tests and reproducible demonstrations.
2. Public dataset mode — optional imported snapshots of publicly available railway information.

The core application remains runnable without an external dataset.

## Layout

data/
├── README.md
├── raw/
│   └── .gitkeep
└── processed/
    └── .gitkeep

Large datasets must not be committed to Git unless explicitly approved.

## Current station dataset candidate

The first supported format is the GeoJSON station FeatureCollection from the Kaggle Indian Railways Dataset. Its published data card describes station records containing coordinates and properties including state, station code, name, zone and address. Kaggle currently lists the dataset as CC0 / Public Domain.

Source: https://www.kaggle.com/sripaadsrinivasan/indian-railways-dataset

RailSync treats this as a public/historical dataset snapshot, never as live railway operational data.

## Import workflow

1. Obtain the dataset from its published source.
2. Record the actual download date and version/snapshot if available.
3. Place the station GeoJSON under data/raw/.
4. Run the station importer against that file.
5. Review import statistics and rejected records.
6. Verify repeated imports are idempotent.

Do not place passenger PII, real ticket information or confidential railway data here.

See docs/09-data-sources.md for provenance and limitations.
