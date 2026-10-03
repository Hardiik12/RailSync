---
trigger: always_on
---

# RailSync — Testing Rule

## 1. Purpose

This rule defines the mandatory testing strategy for RailSync.

Testing must verify:

- algorithm correctness
- algorithm edge cases
- algorithm complexity assumptions where practical
- cross-algorithm equivalence
- railway service behavior
- API contracts
- database integration
- real-time event behavior
- frontend behavior
- end-to-end workflows

A successful compilation is NOT evidence that a feature works.

A test may only be reported as passed if it was actually executed and passed.

---

# 2. Testing Principles

Prioritize:

```text
Correctness
    ↓
Coverage of important behavior
    ↓
Regression prevention
    ↓
Integration verification
    ↓
Performance verification