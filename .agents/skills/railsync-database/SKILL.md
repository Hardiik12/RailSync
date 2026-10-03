# RailSync Database Engineering Skill

## 1. Purpose

This skill defines the standard workflow for designing, modifying, testing, seeding, optimizing, and integrating the RailSync PostgreSQL database.

Use this skill whenever working on:

- PostgreSQL schema
- Flyway migrations
- JPA persistence
- database relationships
- indexes
- constraints
- synthetic seed data
- large railway datasets
- database queries
- pagination
- database performance
- Testcontainers PostgreSQL
- repository integration
- database debugging
- data integrity
- algorithm input preparation

The database must follow:

AGENTS.md

.agents/rules/01-project-scope.md
.agents/rules/02-architecture.md
.agents/rules/03-dsa-implementation.md
.agents/rules/04-api-contract.md
.agents/rules/05-testing.md
.agents/rules/06-ui.md
.agents/rules/07-git.md

The database is the persistence layer of RailSync.

It must support the railway simulation, operational data, event system, documents, analytics, and DSA input preparation without replacing the DSA implementations.

---

# 2. Database Technology

RailSync uses:

```text
PostgreSQL
Flyway
Spring Data JPA
Testcontainers PostgreSQL