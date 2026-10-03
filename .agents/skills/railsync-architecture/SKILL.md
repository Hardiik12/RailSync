# RailSync Architecture Skill

## 1. Purpose

This skill is the master architectural enforcement layer for RailSync.

It ensures that all development remains consistent with the approved RailSync architecture:

React + Vite
│
│ REST / WebSocket
↓
Spring Boot Modular Monolith
│
├── Railway Domain Services
├── Algorithm Engine
├── Analytics Services
└── Event Engine
│
↓
PostgreSQL

This skill works together with:

- railsync-dsa
- railsync-testing
- railsync-backend
- railsync-frontend
- railsync-database
- railsync-api
- railsync-algorithms
- railsync-events
- railsync-docs
- railsync-security
- railsync-performance
- railsync-seeding
- railsync-git

This skill has authority over architectural boundaries and dependency direction.

---

# 2. Core Architectural Principle

RailSync is a modular monolith.

Do NOT convert RailSync into microservices unless explicitly requested.

The application must remain understandable as one integrated academic project.

Primary architecture:

```text
┌──────────────────────────────────────────────┐
│                 React Frontend               │
│                                              │
│ Dashboard / Operations / DSA / Analytics     │
│ Documents / Settings / Visualizations        │
└──────────────────────┬───────────────────────┘
                       │
                REST / WebSocket
                       │
                       ▼
┌──────────────────────────────────────────────┐
│              Spring Boot Backend             │
│                                              │
│ ┌────────────┐ ┌─────────────┐              │
│ │ Controllers│ │ API / DTOs  │              │
│ └─────┬──────┘ └──────┬──────┘              │
│       │               │                      │
│       ▼               ▼                      │
│ ┌─────────────────────────────────────────┐  │
│ │           Application Services          │  │
│ └───────────────┬─────────────────────────┘  │
│                 │                            │
│     ┌───────────┼──────────────┐             │
│     ▼           ▼              ▼             │
│ Railway     Algorithm       Event Engine     │
│ Domain      Engine                         │
│ Services                                     │
│     │           │              │             │
└─────┼───────────┼──────────────┼─────────────┘
      │           │              │
      └───────────┼──────────────┘
                  ▼
          ┌───────────────┐
          │  PostgreSQL   │
          └───────────────┘
```
