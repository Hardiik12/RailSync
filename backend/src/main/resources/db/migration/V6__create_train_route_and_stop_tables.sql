-- V6__create_train_route_and_stop_tables.sql
-- Database schema for Train, Route, and RouteStop domain models

CREATE TABLE train (
    id BIGSERIAL PRIMARY KEY,
    train_number VARCHAR(20) NOT NULL UNIQUE,
    train_name VARCHAR(150) NOT NULL,
    train_type VARCHAR(50),
    source_station_id BIGINT NOT NULL REFERENCES station(id),
    destination_station_id BIGINT NOT NULL REFERENCES station(id),
    capacity INTEGER NOT NULL DEFAULT 500,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    current_station_id BIGINT REFERENCES station(id),
    delay_minutes INTEGER NOT NULL DEFAULT 0,
    running_days VARCHAR(100),
    distance_km DOUBLE PRECISION,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_train_capacity CHECK (capacity > 0),
    CONSTRAINT chk_train_delay CHECK (delay_minutes >= 0),
    CONSTRAINT chk_train_source_dest CHECK (source_station_id != destination_station_id)
);

CREATE INDEX idx_train_number ON train(train_number);
CREATE INDEX idx_train_name ON train(train_name);
CREATE INDEX idx_train_status ON train(status);
CREATE INDEX idx_train_source ON train(source_station_id);
CREATE INDEX idx_train_destination ON train(destination_station_id);

CREATE TABLE route (
    id BIGSERIAL PRIMARY KEY,
    train_id BIGINT NOT NULL REFERENCES train(id) ON DELETE CASCADE,
    route_name VARCHAR(150),
    distance_km DOUBLE PRECISION,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_route_train_id ON route(train_id);

CREATE TABLE route_stop (
    id BIGSERIAL PRIMARY KEY,
    route_id BIGINT NOT NULL REFERENCES route(id) ON DELETE CASCADE,
    station_id BIGINT NOT NULL REFERENCES station(id),
    stop_sequence INTEGER NOT NULL,
    distance_from_origin DOUBLE PRECISION,
    scheduled_dwell_minutes INTEGER DEFAULT 2,
    scheduled_arrival VARCHAR(20),
    scheduled_departure VARCHAR(20),
    CONSTRAINT chk_stop_sequence CHECK (stop_sequence > 0),
    CONSTRAINT uq_route_stop_sequence UNIQUE (route_id, stop_sequence)
);

CREATE INDEX idx_route_stop_route_id ON route_stop(route_id);
CREATE INDEX idx_route_stop_station_id ON route_stop(station_id);
CREATE INDEX idx_route_stop_route_seq ON route_stop(route_id, stop_sequence);
