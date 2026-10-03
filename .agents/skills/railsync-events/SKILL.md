# RailSync Events Skill

## Purpose

This skill defines how Antigravity must implement the RailSync continuous railway event engine.

The event system provides simulated operational activity such as:

- train arrivals
- train departures
- station events
- platform assignments
- service alerts
- maintenance events
- passenger-flow events
- operational status changes
- periodic railway activity

The event engine exists to support:

- real-time dashboard updates
- WebSocket communication
- event-history storage
- analytics
- algorithm demonstrations
- operational simulations

RailSync is an academic railway operations simulation.

It is NOT:

- a real railway control system
- a signaling system
- a train-control system
- an official railway dispatch platform
- a safety-critical railway system

Never claim that RailSync controls real trains or railway infrastructure.

---

# 1. Rule Priority

When implementing the event system, follow this priority:

1. Project specification
2. `.agents/rules/01-project-scope.md`
3. `.agents/rules/02-architecture.md`
4. `.agents/rules/04-api-contract.md`
5. `.agents/rules/05-testing.md`
6. This skill
7. Existing implementation patterns
8. Developer convenience

Never violate a higher-priority rule to simplify event implementation.

---

# 2. Architecture

The event system follows:

```text
Event Generator
      ↓
Event Validation
      ↓
Event Processing Service
      ↓
Persistence
      ↓
WebSocket Broadcast
      ↓
Frontend Event Store
      ↓
Live UI