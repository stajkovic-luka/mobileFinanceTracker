# mobileFinanceTracker

A mobile savings-goal tracker — set goals, track deposits, and watch your progress.

> **Work in progress.** This project is under active development as part of a diploma thesis. Expect incomplete features and frequent changes.

## Overview

mobileFinanceTracker is a personal savings application that helps users define financial goals (e.g. "New laptop — €1,000"), log deposits toward each goal, and visualize progress through charts and filtered reports. It is built as a REST backend paired with a native Android client.

## Planned features

- User registration and login (JWT)
- CRUD for savings goals (target amount, deadline, progress %)
- CRUD for deposits per goal (automatically updates progress)
- Reports and charts with filters (by date, goal, category)
- Native Android client (Jetpack Compose)

## Tech stack

**Backend** (`backend/`)
- Spring Boot 4.1, Kotlin 2.3, JDK 25
- Gradle (Kotlin DSL) 9.5
- Spring Data JPA, Flyway (database migrations)
- MySQL 9.x
- JWT authentication (planned)

**Android** (`app/`, planned)
- Kotlin, Jetpack Compose
- Retrofit, charts, MVVM

## Project structure

```
mobileFinanceApp/
├── backend/   # Spring Boot REST API
├── app/       # Android client (planned)
├── docs/      # Documentation
└── sql/       # DB seeds / reference SQL
```

## Roadmap

| Phase | Description | Status |
| --- | --- | --- |
| A0 | Backend skeleton (Spring Boot, Flyway, DB schema, `/health`) | Done |
| A1 | Authentication (JWT, register/login) | In progress |
| A2 | Goals CRUD | Planned |
| A3 | Deposits CRUD | Planned |
| A4 | Reports and charts | Planned |
| B0–B4 | Android client | Planned |

## Running the backend

Prerequisites: JDK 25, MySQL.

1. Create the database:
```sql
   CREATE DATABASE financeApp;
```
2. Configure the connection in `backend/src/main/resources/application.yml`.
3. Start the server:
```bash
   cd backend
   ./gradlew bootRun
```
4. Verify it is running: open `http://localhost:8080/health` — it should return `{"status":"UP"}`.

Database tables (`users`, `goals`, `deposits`) are created automatically by Flyway on the first run.

## About

Diploma thesis project, built with Kotlin across the stack (Spring Boot backend + Android client).
