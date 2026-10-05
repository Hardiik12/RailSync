-- V7__create_trip_table.sql
-- Database schema for Trip domain model representing scheduled operational service instances

CREATE TABLE trip (
    id BIGSERIAL PRIMARY KEY,
    train_id BIGINT NOT NULL REFERENCES train(id) ON DELETE CASCADE,
    service_date DATE NOT NULL,
    scheduled_status VARCHAR(30) NOT NULL DEFAULT 'SCHEDULED',
    actual_status VARCHAR(30),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_trip_train_date UNIQUE (train_id, service_date)
);

CREATE INDEX idx_trip_train_id ON trip(train_id);
CREATE INDEX idx_trip_service_date ON trip(service_date);
CREATE INDEX idx_trip_train_date ON trip(train_id, service_date);
