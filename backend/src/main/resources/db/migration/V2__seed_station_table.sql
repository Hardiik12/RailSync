-- V2__seed_station_table.sql
-- RailSync Synthetic Railway Data Seed Script
-- ==============================================================================
-- ACADEMIC SIMULATION NOTICE:
-- The railway dataset seeded below is synthetic and deterministic.
-- Station names and codes reflect realistic domain examples for educational 
-- and DSA algorithm demonstration purposes (e.g., KMP search, flow networks).
-- This dataset DOES NOT represent live Indian Railways operational infrastructure.
-- ==============================================================================

INSERT INTO station (station_code, name, city, state, platform_count, status) VALUES
('BZA', 'Vijayawada Junction', 'Vijayawada', 'Andhra Pradesh', 10, 'ACTIVE'),
('SC',  'Secunderabad Junction', 'Hyderabad', 'Telangana', 10, 'ACTIVE'),
('MAS', 'Chennai Central', 'Chennai', 'Tamil Nadu', 12, 'ACTIVE'),
('HWH', 'Howrah Junction', 'Kolkata', 'West Bengal', 23, 'ACTIVE'),
('CSMT', 'Chhatrapati Shivaji Maharaj Terminus', 'Mumbai', 'Maharashtra', 18, 'ACTIVE'),
('NDLS', 'New Delhi Railway Station', 'New Delhi', 'Delhi', 16, 'ACTIVE'),
('SBC', 'KSR Bengaluru City Junction', 'Bengaluru', 'Karnataka', 10, 'ACTIVE'),
('PUNE', 'Pune Junction', 'Pune', 'Maharashtra', 6, 'ACTIVE'),
('ADI', 'Ahmedabad Junction', 'Ahmedabad', 'Gujarat', 12, 'ACTIVE'),
('JP',  'Jaipur Junction', 'Jaipur', 'Rajasthan', 8, 'ACTIVE'),
('PNBE', 'Patna Junction', 'Patna', 'Bihar', 10, 'ACTIVE'),
('LKO', 'Lucknow Charbagh', 'Lucknow', 'Uttar Pradesh', 9, 'ACTIVE'),
('VSKP', 'Visakhapatnam Junction', 'Visakhapatnam', 'Andhra Pradesh', 8, 'ACTIVE'),
('WL',  'Warangal Railway Station', 'Warangal', 'Telangana', 4, 'ACTIVE'),
('KCG', 'Kacheguda Railway Station', 'Hyderabad', 'Telangana', 5, 'ACTIVE'),
('PER', 'Perambur Railway Station', 'Chennai', 'Tamil Nadu', 4, 'MAINTENANCE');
