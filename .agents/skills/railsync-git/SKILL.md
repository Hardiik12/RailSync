# RailSync Git & Version Control Skill

## Purpose

This skill defines how Antigravity must use Git while developing RailSync.

The goal is to keep the repository:

- recoverable
- reviewable
- modular
- traceable
- stable
- easy to debug

Git operations must protect the existing implementation.

Never make uncontrolled repository-wide changes.

---

# 1. Core Git Principles

Follow these rules:

1. Inspect Git state before making changes.
2. Work on the correct branch.
3. Keep changes focused.
4. Do not mix unrelated features in one commit.
5. Inspect diffs before committing.
6. Run relevant tests before committing.
7. Never rewrite project history without explicit instruction.
8. Never force-push without explicit instruction.
9. Never delete branches without explicit instruction.
10. Never commit secrets.
11. Never hide failing tests.
12. Never claim a commit is clean without inspecting it.

---

# 2. Repository Structure

The repository should remain:

```text
RailSync/
├── AGENTS.md
├── README.md
├── docs/
├── .agents/
├── frontend/
├── backend/
├── database/
└── scripts/
```
