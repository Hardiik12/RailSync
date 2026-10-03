# RailSync Algorithm Engineering Skill

## 1. Purpose

This skill defines the exact implementation, validation, integration, tracing, benchmarking, and documentation workflow for all DSA algorithms in RailSync.

Use this skill whenever working on:

- DSA algorithm implementation
- algorithm debugging
- algorithm optimization
- algorithm testing
- algorithm cross-validation
- algorithm traces
- algorithm visualization data
- algorithm benchmarking
- algorithm API integration
- algorithm complexity analysis
- DSA documentation
- DSA viva/demo preparation

This skill operates under:

AGENTS.md

.agents/rules/01-project-scope.md
.agents/rules/02-architecture.md
.agents/rules/03-dsa-implementation.md
.agents/rules/04-api-contract.md
.agents/rules/05-testing.md
.agents/rules/06-ui.md
.agents/rules/07-git.md

Related skills:

.agents/skills/railsync-dsa/SKILL.md
.agents/skills/railsync-testing/SKILL.md
.agents/skills/railsync-backend/SKILL.md
.agents/skills/railsync-api/SKILL.md
.agents/skills/railsync-frontend/SKILL.md

The algorithm implementation is the computational core of RailSync.

---

# 2. Fundamental Rule

The required DSA algorithms must be implemented manually.

Do not replace them with:

- Java library sorting/searching methods
- Apache Commons implementations
- Guava algorithms
- third-party graph libraries
- database queries
- JavaScript implementations
- external algorithm APIs

Library utilities may be used for unrelated infrastructure tasks, but never as a substitute for a required RailSync DSA implementation.

---

# 3. Algorithm Architecture

The algorithm layer must remain domain-independent.

Use:

```text
Railway Domain
      ↓
Railway Service
      ↓
Algorithm Adapter/Input Builder
      ↓
DSA Algorithm
      ↓
Algorithm Result
      ↓
Metrics / Trace
      ↓
API