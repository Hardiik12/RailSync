---
trigger: always_on
---

# RailSync — API Contract Rule

## 1. Purpose

This rule defines the API contract for RailSync.

The API is the boundary between:

```text
React Frontend
      ↓
REST / WebSocket API
      ↓
Spring Boot Backend
      ↓
Railway Services / Algorithm Services