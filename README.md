# mobileFinanceTracker

A mobile application for tracking personal savings goals — set a target, log deposits, and watch your progress.

![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-2.3.21-7F52FF?logo=kotlin&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.0-6DB33F?logo=springboot&logoColor=white)

## About

mobileFinanceTracker is a personal project that helps users stay on top of their savings. Define a goal (e.g. "New laptop — €1,000"), log deposits toward it over time, and follow your progress at a glance. The project consists of a REST API backend and a native Android client (in development).

## Features

- User registration and login with JWT authentication
- Savings goals CRUD: create, list, view, partially update and delete
- Create and list deposits for a specific savings goal
- Automatic update of a goal's current amount, progress percentage and status after a deposit
- Data access limited to the authenticated user's own goals and deposits
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
- A JWT secret with at least 32 characters

### Run the backend

1. Create the database:
   ```sql
   CREATE DATABASE financeApp;
   ```
2. Configure the database connection in `backend/src/main/resources/application.yml`.
3. Create `backend/.env` and set the JWT secret:

   ```text
   JWT_SECRET=replace-with-a-long-random-secret
   ```

4. Start the server:

   ```bash
   cd backend
   ./gradlew bootRun
   ```

5. Verify it is running: open `http://localhost:8080/health` — it should return `{"status":"UP"}`.

Flyway applies database migrations automatically on the first run. The current migrations create the `users`, `goals` and `deposits` tables and insert demo data.

Run backend tests with:

```bash
./gradlew test
```

## Project Structure

```
mobileFinanceApp/
├── backend/   # Spring Boot REST API
├── app/       # Android client (planned)
├── docs/      # Documentation
└── postman/   # API collection and environment template
```

## API

All endpoints except `/register`, `/login` and `/health` require this header:

```text
Authorization: Bearer <JWT_TOKEN>
```

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/register` | Registers a user and returns a JWT token |
| POST | `/login` | Logs in with username and password, then returns a JWT token |
| GET | `/health` | Health check |
| POST | `/goals` | Creates a savings goal for the authenticated user |
| GET | `/goals` | Lists the authenticated user's goals |
| GET | `/goals/{goalId}` | Returns one of the authenticated user's goals |
| PATCH | `/goals/{goalId}` | Updates one or more fields of a goal |
| DELETE | `/goals/{goalId}` | Deletes a goal and its deposits |
| POST | `/goals/{goalId}/deposits` | Creates a deposit for a goal |
| GET | `/goals/{goalId}/deposits` | Lists deposits for a goal, oldest first |

## Postman

The repository contains an API collection and an environment template in the `postman/` directory.

1. Import `FinanceApp.postman_collection.json` and `FinanceApp.postman_environment.json` into Postman.
2. In the selected environment, set `baseUrl` to `http://127.0.0.1:8080`.
3. Send the Login request. Its post-response script saves the returned JWT into the `token` environment variable.
4. Set `goalId` to an existing goal ID before testing goal-specific or deposit endpoints.

## Roadmap

- ✅ Backend skeleton (Spring Boot, Flyway, MySQL)
- ✅ Registration, login and JWT authentication
- ✅ Savings goals CRUD
- ✅ Create and list deposits
- ⬜ Update and delete deposits
- ⬜ Charts & reports
- ⬜ Native Android client

## License

Distributed under the MIT License.

## Author

**Luka Stajkovic** — [@stajkovic-luka](https://github.com/stajkovic-luka)
