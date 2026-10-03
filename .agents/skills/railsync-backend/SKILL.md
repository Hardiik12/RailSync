# RailSync Backend Engineering Skill

## 1. Purpose

This skill defines the standard workflow for building, modifying, testing, debugging, and integrating the RailSync backend.

Use this skill whenever working on:

- Spring Boot backend
- REST APIs
- WebSocket/event APIs
- PostgreSQL
- Flyway migrations
- JPA entities
- repositories
- services
- controllers
- DTOs
- validation
- exception handling
- algorithm integration
- domain logic
- database seed data
- backend testing
- backend performance
- API documentation

The backend must follow:

AGENTS.md

.agents/rules/01-project-scope.md
.agents/rules/02-architecture.md
.agents/rules/03-dsa-implementation.md
.agents/rules/04-api-contract.md
.agents/rules/05-testing.md
.agents/rules/06-ui.md
.agents/rules/07-git.md

The backend is a **modular monolith**.

Do not introduce microservices unless the project scope is explicitly changed.

---

# 2. Backend Architecture

Use:

```text
React Frontend
      │
      │ REST / WebSocket
      ↓
Spring Boot
      │
      ├── Domain Layer
      ├── Repository Layer
      ├── Service Layer
      ├── Algorithm Layer
      ├── Event Layer
      └── API Layer
      │
      ↓
PostgreSQL