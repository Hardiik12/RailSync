# RailSync Performance Skill

## Purpose

This skill defines how Antigravity must measure, analyze, optimize, and document RailSync performance.

Performance work must remain evidence-based.

Never invent:

- execution times
- throughput
- memory usage
- benchmark results
- database latency
- WebSocket delivery rates
- frontend FPS
- scalability claims

Every measured performance claim must come from an actual execution.

---

# 1. Performance Principles

Follow these principles:

1. Correctness comes before optimization.
2. Measure before optimizing.
3. Preserve algorithmic correctness.
4. Preserve the approved architecture.
5. Keep theoretical complexity separate from measured runtime.
6. Use deterministic inputs where reproducibility matters.
7. Benchmark algorithms independently from database/network overhead.
8. Benchmark realistic railway-sized data separately.
9. Avoid premature optimization.
10. Never replace a required manual algorithm with a library implementation.
11. Do not optimize by removing required functionality.
12. Document actual measurements.

---

# 2. Performance Layers

RailSync performance has several independent layers:

```text
Algorithm
    ↓
Service
    ↓
Database
    ↓
API
    ↓
WebSocket/Event Engine
    ↓
Frontend