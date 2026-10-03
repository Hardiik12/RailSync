# RailSync Security Skill

## Purpose

This skill defines the security rules Antigravity must follow when developing RailSync.

RailSync is an academic railway operations simulation and analytics platform.

Security must protect:

- application integrity
- database integrity
- API boundaries
- WebSocket communication
- synthetic passenger data
- environment secrets
- algorithm execution endpoints
- simulation controls

Security must not introduce unrelated enterprise infrastructure.

---

# 1. Security Principles

Follow these principles:

1. Validate all external input.
2. Never trust frontend validation alone.
3. Never expose secrets.
4. Never use real passenger PII.
5. Protect database credentials.
6. Limit expensive algorithm inputs.
7. Limit event simulation volume.
8. Validate WebSocket messages.
9. Do not expose stack traces.
10. Keep security logic separate from DSA implementations.
11. Prefer simple, auditable security mechanisms.
12. Do not add unnecessary authentication complexity unless explicitly required.

---

# 2. Security Scope

RailSync security covers:

```text
React
  ↓
REST API
  ↓
Spring Boot
  ↓
Services
  ↓
PostgreSQL

React
  ↓
WebSocket
  ↓
Event Engine