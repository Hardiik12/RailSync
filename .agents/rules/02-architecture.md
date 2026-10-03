---
trigger: always_on
---

# RailSync — Architecture Rule

## 1. Purpose

This rule defines the mandatory technical architecture of RailSync.

All agents must follow the documented architecture unless an explicit project-level decision changes it.

RailSync uses a modular-monolith architecture.

The architecture must remain:

```text
React + Vite
       │
       │ REST / WebSocket
       ▼
Spring Boot
       │
       ├── Railway Domain
       ├── Algorithm Engine
       ├── Analytics
       └── Event Engine
       │
       ▼
PostgreSQL