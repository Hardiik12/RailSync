# RailSync API Engineering Skill

## 1. Purpose

This skill defines the standard workflow for designing, implementing, validating, testing, and maintaining the RailSync REST and WebSocket API.

Use this skill whenever working on:

- REST endpoints
- request DTOs
- response DTOs
- API validation
- API error handling
- pagination
- filtering
- sorting
- algorithm endpoints
- algorithm execution metadata
- WebSocket events
- API versioning
- API documentation
- frontend/backend API integration
- API integration tests
- API debugging

The API must follow:

AGENTS.md

.agents/rules/01-project-scope.md
.agents/rules/02-architecture.md
.agents/rules/03-dsa-implementation.md
.agents/rules/04-api-contract.md
.agents/rules/05-testing.md
.agents/rules/06-ui.md
.agents/rules/07-git.md

Related skills:

.agents/skills/railsync-backend/SKILL.md
.agents/skills/railsync-database/SKILL.md
.agents/skills/railsync-dsa/SKILL.md
.agents/skills/railsync-testing/SKILL.md
.agents/skills/railsync-frontend/SKILL.md

The API is the contract between the backend and frontend.

API consistency is mandatory.

---

# 2. API Architecture

The API layer follows:

```text
React Frontend
      │
      │ REST / WebSocket
      ↓
Spring Boot Controller
      ↓
Request DTO
      ↓
Validation
      ↓
Service
      ↓
Domain / Algorithm
      ↓
Repository / PostgreSQL