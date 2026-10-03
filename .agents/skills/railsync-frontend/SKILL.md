# RailSync Frontend Engineering Skill

## 1. Purpose

This skill defines the standard workflow for building, modifying, testing, debugging, and integrating the RailSync frontend.

Use this skill whenever working on:

- React components
- Vite configuration
- Tailwind CSS
- React Router
- frontend state management
- REST API integration
- WebSocket integration
- DSA visualizations
- railway dashboards
- analytics interfaces
- algorithm pages
- responsive layouts
- accessibility
- frontend testing
- Playwright E2E workflows
- frontend performance

The frontend must follow:

AGENTS.md

.agents/rules/01-project-scope.md
.agents/rules/02-architecture.md
.agents/rules/03-dsa-implementation.md
.agents/rules/04-api-contract.md
.agents/rules/05-testing.md
.agents/rules/06-ui.md
.agents/rules/07-git.md

The frontend is a presentation and interaction layer.

It must not become a second implementation of the DSA algorithms.

---

# 2. Frontend Architecture

Use:

```text
React + Vite
      │
      ├── Pages
      ├── Components
      ├── API Client
      ├── State
      ├── Visualizations
      └── WebSocket Client
              │
              ↓
        Spring Boot Backend
              │
              ↓
          PostgreSQL