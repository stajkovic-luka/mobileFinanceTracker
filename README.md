# mobileFinanceTracker

A mobile application for tracking personal savings goals — set a target, log deposits, and watch your progress.

![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-2.3.21-7F52FF?logo=kotlin&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.0-6DB33F?logo=springboot&logoColor=white)

## About

SQRL is a personal project that helps users stay on top of their savings. Define a goal (e.g. "New laptop — €1,000"), log deposits toward it over time, and follow your progress at a glance. The project consists of a REST API backend and a native Android client (in development).

## Features

- User registration and login with JWT authentication
- Savings goals CRUD: create, list, view, partially update and delete
- Deposits CRUD for a specific savings goal
- Automatic update of a goal's current amount, progress percentage and status after a deposit is created, updated or deleted
- Data access limited to the authenticated user's own goals and deposits
- Deposit reports with date filters
- File logging with automatic rotation
- Charts — *planned*
- Native Android client — Compose navigation, welcome screen and login with JWT storage

## Tech Stack

**Backend** *(current)*

![Kotlin](https://img.shields.io/badge/Kotlin-2.3.21-7F52FF?logo=kotlin&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.0-6DB33F?logo=springboot&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-9.5.1-02303A?logo=gradle&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-9.x-4479A1?logo=mysql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-Migrations-CC0202?logo=flyway&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-Hibernate-6DB33F?logo=hibernate&logoColor=white)

**Mobile** *(in development)*

![Android](https://img.shields.io/badge/Android-Native-3DDC84?logo=android&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4?logo=jetpackcompose&logoColor=white)
![Retrofit](https://img.shields.io/badge/Retrofit-HTTP_client-3DDC84)

**Charts & reporting** *(planned)* — Vico is the library of choice

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

Application logs are written to `backend/logs/backend.log` and automatically archived when they reach the configured size.

Run backend tests with:

```bash
./gradlew test
```

### Run the Android app

1. Open the `app/` directory in Android Studio.
2. Connect a physical Android device through Android Studio.
3. Start the backend before running the Android app.
4. Ensure the device and the computer running the backend are on the same local network.
5. Set `BASE_URL` in `app/app/src/main/java/com/stajkovicluka/financeapp/data/api/ApiClient.kt` to the computer's local IP address and backend port, for example `http://192.168.1.10:8080/`.
6. Run the `app` configuration.

The current Android client supports the Welcome → Login flow. A successful login stores the returned JWT locally; authenticated goal and deposit requests will be added in the following increments.

## Project Structure

```
mobileFinanceApp/
├── backend/   # Spring Boot REST API
├── app/       # Android client (Jetpack Compose)
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
| PATCH | `/goals/{goalId}/deposits/{depositId}` | Updates a deposit's amount and/or note |
| DELETE | `/goals/{goalId}/deposits/{depositId}` | Deletes a deposit and recalculates the goal |
| GET | `/reports/deposits?from={date}&to={date}` | Returns the authenticated user's deposits and total for a date range |

## Postman

The repository contains an API collection and an environment template in the `postman/` directory.

1. Import `FinanceApp.postman_collection.json` and `FinanceApp.postman_environment.json` into Postman.
2. In the selected environment, set `baseUrl` to `http://127.0.0.1:8080`.
3. Send the Login request. Its post-response script saves the returned JWT into the `token` environment variable.
4. Set `goalId` to an existing goal ID before testing goal-specific or deposit endpoints.

## License

Distributed under the MIT License.

## Author

**Luka Stajkovic** — [@stajkovic-luka](https://github.com/stajkovic-luka)
