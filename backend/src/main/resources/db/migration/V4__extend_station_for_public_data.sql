-- V4__extend_station_for_public_data.sql
-- Extend station table for public railway data import

ALTER TABLE station
    ADD COLUMN data_origin VARCHAR(50) NOT NULL DEFAULT 'SYNTHETIC',
    ADD COLUMN source_dataset VARCHAR(150),
    ADD COLUMN latitude DOUBLE PRECISION,
    ADD COLUMN longitude DOUBLE PRECISION;

CREATE INDEX idx_station_data_origin ON station(data_origin);
