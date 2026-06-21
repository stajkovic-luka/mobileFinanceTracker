# mobileFinanceTracker

A mobile application for tracking personal savings goals — set a target, log deposits, and watch your progress.

![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-2.3.21-7F52FF?logo=kotlin&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.0-6DB33F?logo=springboot&logoColor=white)

## About

mobileFinanceTracker is a personal project that helps users stay on top of their savings. Define a goal (e.g. "New laptop — €1,000"), log deposits toward it over time, and follow your progress at a glance. The project consists of a REST API backend and a native Android client (in development).

## Features

- User registration and login (username + password)
- Create and manage savings goals (target amount, deadline, progress %)
- Log deposits toward a goal (progress updates automatically)
- Per-goal progress tracking
- Charts and reports with filters — *planned*
- Native Android client — *planned*

## Tech Stack

**Backend** *(current)*

![Kotlin](https://img.shields.io/badge/Kotlin-2.3.21-7F52FF?logo=kotlin&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.0-6DB33F?logo=springboot&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-9.5.1-02303A?logo=gradle&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-9.x-4479A1?logo=mysql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-Migrations-CC0202?logo=flyway&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-Hibernate-6DB33F?logo=hibernate&logoColor=white)

**Mobile** *(planned)*

![Android](https://img.shields.io/badge/Android-Planned-3DDC84?logo=android&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-Planned-4285F4?logo=jetpackcompose&logoColor=white)

**Charts & reporting** *(planned)* — library not yet decided (TBD).

## Getting Started

### Prerequisites

- JDK 25
- MySQL

### Run the backend

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

Database tables are created automatically by Flyway on first run.

## Project Structure

```
mobileFinanceApp/
├── backend/   # Spring Boot REST API
├── app/       # Android client (planned)
└── docs/      # Documentation
```

## API

| Method | Endpoint     | Description            | Status      |
|--------|--------------|------------------------|-------------|
| POST   | `/register`  | Register a new user    | Available   |
| POST   | `/login`     | Login, returns a token | In progress |
| GET    | `/health`    | Health check           | Available   |

## Roadmap

- ✅ Backend skeleton (Spring Boot, Flyway, MySQL)
- ✅ User registration (password hashing)
- 🚧 Authentication (login + JWT)
- ⬜ Savings goals CRUD
- ⬜ Deposits CRUD
- ⬜ Charts & reports
- ⬜ Native Android client

## License

Distributed under the MIT License.

## Author

**Luka Stajkovic** — [@stajkovic-luka](https://github.com/stajkovic-luka)
