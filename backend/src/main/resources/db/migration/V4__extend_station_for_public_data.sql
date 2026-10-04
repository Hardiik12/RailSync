-- V4__extend_station_for_public_data.sql
-- V1/V2 remain unchanged so deterministic synthetic seed data is preserved.

ALTER TABLE station
    ALTER COLUMN city DROP NOT NULL,
    ALTER COLUMN platform_count DROP NOT NULL,
    ALTER COLUMN status DROP NOT NULL;

ALTER TABLE station
    ADD COLUMN latitude DECIMAL(9,6),
    ADD COLUMN longitude DECIMAL(9,6),
    ADD COLUMN zone VARCHAR(20),
    ADD COLUMN address VARCHAR(300),
    ADD COLUMN data_origin VARCHAR(30) NOT NULL DEFAULT 'SYNTHETIC',
    ADD COLUMN source_dataset VARCHAR(200);

ALTER TABLE station
    ADD CONSTRAINT chk_station_data_origin
    CHECK (data_origin IN ('SYNTHETIC', 'PUBLIC_DATA'));

ALTER TABLE station
    ADD CONSTRAINT chk_station_latitude
    CHECK (latitude IS NULL OR (latitude >= -90 AND latitude <= 90));

ALTER TABLE station
    ADD CONSTRAINT chk_station_longitude
    CHECK (longitude IS NULL OR (longitude >= -180 AND longitude <= 180));

CREATE INDEX idx_station_zone ON station(zone);
CREATE INDEX idx_station_data_origin ON station(data_origin);
