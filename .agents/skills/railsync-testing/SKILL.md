# RailSync Testing Engineering Skill

## 1. Purpose

This skill defines the standard testing workflow for RailSync.

Use this skill whenever working on:

- unit tests
- algorithm tests
- cross-validation
- service tests
- controller/API tests
- database integration tests
- frontend tests
- WebSocket tests
- end-to-end tests
- regression testing
- randomized algorithm testing
- trace testing
- benchmark testing
- test fixtures
- test debugging
- test coverage
- CI validation
- release validation

Testing must follow:

AGENTS.md

.agents/rules/01-project-scope.md
.agents/rules/02-architecture.md
.agents/rules/03-dsa-implementation.md
.agents/rules/04-api-contract.md
.agents/rules/05-testing.md
.agents/rules/06-ui.md
.agents/rules/07-git.md

The testing strategy must verify both:

1. DSA correctness
2. Railway application correctness

Never treat a successful compilation as proof that an algorithm or feature works.

---

# 2. Core Testing Principles

Always follow:

```text
Implement
   ↓
Compile
   ↓
Unit Test
   ↓
Cross-Validate
   ↓
Service Test
   ↓
API Test
   ↓
Database Test
   ↓
Frontend Test
   ↓
E2E Test
   ↓
Regression Test