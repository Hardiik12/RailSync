# RailSync Database Seeding Skill

## Purpose

This skill defines how Antigravity must generate, insert, reset, validate, and maintain synthetic RailSync railway data.

The seed system exists to provide realistic, deterministic data for:

- development
- testing
- demonstrations
- DSA execution
- algorithm benchmarking
- analytics
- event simulation
- frontend development

RailSync uses synthetic railway data.

The seed system must never require real passenger information or live railway data.

---

# 1. Core Principles

Follow these rules:

1. Seed data must be synthetic.
2. Seed data should be deterministic where reproducibility matters.
3. Foreign-key relationships must remain valid.
4. Seed order must respect dependencies.
5. Seed data must support the approved DSA modules.
6. Seed generation must not bypass database constraints.
7. Large datasets must be generated deliberately.
8. Reset/reseed behavior must be predictable.
9. Do not invent seed counts as actual results.
10. Do not use live railway APIs as a seed dependency.

---

# 2. Seed Architecture

Recommended structure:

```text
database/
├── migrations/
└── seed/
    ├── generators/
    ├── repositories/
    ├── configuration/
    └── runner/