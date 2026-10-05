-- V8__create_stop_time_table.sql
-- Database schema for StopTime domain model representing timetable information per trip stop

CREATE TABLE stop_time (
    id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT NOT NULL REFERENCES trip(id) ON DELETE CASCADE,
    station_id BIGINT NOT NULL REFERENCES station(id),
    stop_sequence INTEGER NOT NULL,
    scheduled_arrival VARCHAR(20),
    scheduled_departure VARCHAR(20),
    actual_arrival VARCHAR(20),
    actual_departure VARCHAR(20),
    arrival_delay_minutes INTEGER DEFAULT 0,
    departure_delay_minutes INTEGER DEFAULT 0,
    CONSTRAINT chk_stop_time_sequence CHECK (stop_sequence > 0),
    CONSTRAINT chk_arrival_delay CHECK (arrival_delay_minutes >= 0),
    CONSTRAINT chk_departure_delay CHECK (departure_delay_minutes >= 0),
    CONSTRAINT uq_stop_time_trip_sequence UNIQUE (trip_id, stop_sequence)
);

CREATE INDEX idx_stop_time_trip_id ON stop_time(trip_id);
CREATE INDEX idx_stop_time_station_id ON stop_time(station_id);
CREATE INDEX idx_stop_time_trip_seq ON stop_time(trip_id, stop_sequence);
