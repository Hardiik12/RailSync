-- V1__create_station_table.sql
-- Create station table for RailSync Railway Operations Platform

CREATE TABLE station (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    station_code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(150) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    platform_count INTEGER NOT NULL DEFAULT 1 CHECK (platform_count > 0),
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'MAINTENANCE')),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for efficient domain retrieval
CREATE INDEX idx_station_code ON station(station_code);
CREATE INDEX idx_station_name ON station(name);
CREATE INDEX idx_station_city ON station(city);
CREATE INDEX idx_station_status ON station(status);
