# RailSync Integration Skill

## 1. Purpose

This skill defines how RailSync features must be integrated across the complete application stack.

RailSync integration follows:

Database
↓
Repository
↓
Domain/Application Service
↓
Algorithm Engine
↓
REST/WebSocket API
↓
React API Client
↓
UI / Visualization
↓
Tests
↓
Documentation

The purpose of this skill is to prevent partially implemented features, disconnected modules, duplicated logic, API drift, and frontend/backend inconsistencies.

This skill works with:

- railsync-architecture
- railsync-backend
- railsync-database
- railsync-algorithms
- railsync-api
- railsync-frontend
- railsync-testing
- railsync-events
- railsync-docs
- railsync-security
- railsync-performance
- railsync-seeding
- railsync-git

---

# 2. Core Integration Principle

A RailSync feature is not considered complete merely because one layer works.

A feature should normally follow:

```text
Requirement
    ↓
Architecture
    ↓
Database / Input Model
    ↓
Backend Service
    ↓
Algorithm
    ↓
API Contract
    ↓
Frontend Integration
    ↓
Visualization
    ↓
Tests
    ↓
Documentation
```
