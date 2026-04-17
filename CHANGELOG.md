# Changelog — TefilinTracker

All notable changes to this project will be documented in this file.
Format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/)
and commits follow [Conventional Commits](https://www.conventionalcommits.org/).

---

## [Unreleased]

### Added
- **2026-04-17** — `chore(git): ignore google-services.json` — added Firebase config to `.gitignore` to keep API keys out of the public repo; file is untracked so no history rewrite needed.
- **2026-04-17** — `feat(models): add User entity with demographic enums` — created `User.java` POJO with Firestore-compatible constructors, `MAX_ACTIVE_HABITS` invariant, and two complex logic methods: `isHabitRelevantScope(...)` for habit filtering and `addHabit(...)` with duplicate/cap guards. Added supporting enums `Gender` and `MaritalStatus`.
- **2026-04-17** — `docs: scaffold PROJECTBOOK.md and CHANGELOG.md` — initialized the Hebrew Bagrut project book with the five required top-level sections (`תכנון ותיעוד מסכי הפרויקט`, `מימוש הפרויקט`, `בסיס הנתונים`, `שימוש בקבצים`, `דרישות סילבוס בגרות — מיפוי`) and the Bagrut requirements tracking table.
