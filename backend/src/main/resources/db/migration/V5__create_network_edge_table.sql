-- V5__create_network_edge_table.sql
-- Create network_edge table for RailSync Railway Operations Platform

CREATE TABLE network_edge (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    from_station_id BIGINT NOT NULL REFERENCES station(id) ON DELETE CASCADE,
    to_station_id BIGINT NOT NULL REFERENCES station(id) ON DELETE CASCADE,
    capacity DOUBLE PRECISION NOT NULL CHECK (capacity >= 0),
    distance_km DOUBLE PRECISION NOT NULL CHECK (distance_km >= 0),
    travel_time_minutes INTEGER NOT NULL CHECK (travel_time_minutes >= 0),
    data_origin VARCHAR(50) NOT NULL DEFAULT 'SYNTHETIC' CHECK (data_origin IN ('SYNTHETIC', 'PUBLIC_DATA')),
    source_dataset VARCHAR(150),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_different_stations CHECK (from_station_id != to_station_id),
    CONSTRAINT uq_from_to_station UNIQUE (from_station_id, to_station_id)
);

CREATE INDEX idx_network_edge_from ON network_edge(from_station_id);
CREATE INDEX idx_network_edge_to ON network_edge(to_station_id);
CREATE INDEX idx_network_edge_origin ON network_edge(data_origin);

-- Seed initial synthetic network edges between seeded stations
INSERT INTO network_edge (from_station_id, to_station_id, capacity, distance_km, travel_time_minutes, data_origin, source_dataset)
VALUES
    ((SELECT id FROM station WHERE station_code = 'CSMT'), (SELECT id FROM station WHERE station_code = 'PUNE'), 50.0, 150.0, 180, 'SYNTHETIC', 'SYNTHETIC_TOPOLOGY_2026'),
    ((SELECT id FROM station WHERE station_code = 'PUNE'), (SELECT id FROM station WHERE station_code = 'BZA'), 40.0, 750.0, 720, 'SYNTHETIC', 'SYNTHETIC_TOPOLOGY_2026'),
    ((SELECT id FROM station WHERE station_code = 'BZA'), (SELECT id FROM station WHERE station_code = 'MAS'), 45.0, 430.0, 360, 'SYNTHETIC', 'SYNTHETIC_TOPOLOGY_2026'),
    ((SELECT id FROM station WHERE station_code = 'CSMT'), (SELECT id FROM station WHERE station_code = 'ADI'), 35.0, 500.0, 420, 'SYNTHETIC', 'SYNTHETIC_TOPOLOGY_2026'),
    ((SELECT id FROM station WHERE station_code = 'ADI'), (SELECT id FROM station WHERE station_code = 'NDLS'), 30.0, 930.0, 780, 'SYNTHETIC', 'SYNTHETIC_TOPOLOGY_2026'),
    ((SELECT id FROM station WHERE station_code = 'NDLS'), (SELECT id FROM station WHERE station_code = 'HWH'), 40.0, 1450.0, 1200, 'SYNTHETIC', 'SYNTHETIC_TOPOLOGY_2026'),
    ((SELECT id FROM station WHERE station_code = 'HWH'), (SELECT id FROM station WHERE station_code = 'BZA'), 35.0, 1200.0, 1000, 'SYNTHETIC', 'SYNTHETIC_TOPOLOGY_2026'),
    ((SELECT id FROM station WHERE station_code = 'CSMT'), (SELECT id FROM station WHERE station_code = 'NDLS'), 25.0, 1380.0, 1080, 'SYNTHETIC', 'SYNTHETIC_TOPOLOGY_2026');
