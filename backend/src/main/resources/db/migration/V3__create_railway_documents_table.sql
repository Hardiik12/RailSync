-- Flyway Migration V3: Railway Document Indexing Table
-- Note: Dataset is synthetic and intended for academic simulation.

CREATE TABLE railway_documents (
    id BIGSERIAL PRIMARY KEY,
    doc_identifier VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    doc_type VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_railway_doc_identifier ON railway_documents(doc_identifier);
CREATE INDEX idx_railway_doc_type ON railway_documents(doc_type);

-- Seed Synthetic Railway Operational Documents
INSERT INTO railway_documents (doc_identifier, title, doc_type, content, status) VALUES
('DOC-SERVICE-001', 'Northern Line Schedule', 'TIMETABLE', 'Train 12951 Express departure from New Delhi station at 16:55. High speed intercity service via Mathura Junction.', 'ACTIVE'),
('DOC-MAINT-002', 'Platform Track Maintenance', 'MAINTENANCE', 'Scheduled maintenance on Platform 3 track at New Delhi station. Track clearance and ballast tamping operational.', 'ACTIVE'),
('DOC-ALERT-003', 'Winter Fog Delay Bulletin', 'ALERT', 'Dense fog hazard causing train speed restrictions. Train 12951 Express delayed by 45 minutes.', 'ACTIVE'),
('DOC-INSTRUCT-004', 'Safety Protocols for Station Operators', 'INSTRUCTION', 'Platform track maintenance safety protocols require flag signal clearance before train arrival.', 'ACTIVE');
