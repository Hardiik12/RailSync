# RailSync Documentation Skill

## Purpose

This skill defines how Antigravity must create, maintain, review, and synchronize RailSync documentation with the actual implementation.

Documentation is part of the engineering system.

It must describe what RailSync actually contains, how its components interact, how the DSA modules map to railway operations, and how the system can be tested, demonstrated, and explained.

Documentation must never become a fictional description of functionality that does not exist.

---

# 1. Documentation Principles

Follow these principles:

1. Documentation must reflect the current implementation.
2. Do not document functionality that has not been implemented unless explicitly marked as planned.
3. Do not silently change project scope through documentation.
4. Do not invent benchmarks, test results, APIs, database records, or algorithm behavior.
5. Keep DSA explanations technically correct.
6. Keep railway use cases aligned with the approved RailSync scope.
7. Distinguish implemented, planned, theoretical, and experimental functionality.
8. Update documentation when architecture or contracts change.
9. Prefer concise technical documentation over repetitive prose.
10. Documentation must help development, testing, debugging, demonstration, and viva preparation.

---

# 2. Documentation Hierarchy

RailSync documentation follows:

```text
Project Specification
        ↓
Architecture
        ↓
Database Design
        ↓
API Contract
        ↓
Algorithm Contracts
        ↓
UI Architecture
        ↓
Testing Strategy
        ↓
Development Roadmap
        ↓
Event System