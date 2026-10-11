<div align="center">

# KickerDaVinci

*A REST API for managing foosball clubs, competitions, cups, and match results*

![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.4-brightgreen.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-supported-blue.svg)
![Maven](https://img.shields.io/badge/Maven-wrapper-C71A36.svg)

[About](#about) • [Features](#features) • [Tech Stack](#tech-stack) • [API](#api-overview) • [Getting Started](#getting-started) • [Structure](#repository-structure)

</div>

---

## About

**KickerDaVinci** is the backend for a foosball club and competition management application. It exposes a REST API for organizing clubs, players, divisions, competitions and cups, and for recording games and results.

The project is built with **Java 17** and **Spring Boot**. It uses **Spring Data JPA** to persist application data in PostgreSQL and provides account sign-in with JWT tokens.

### Main workflows

- Register and sign in users.
- Create clubs, associate captains and players, and place clubs in divisions.
- Schedule competitions and manage cups.
- Record games and match recaps.
- Maintain ranking categories and retrieve users ordered by ranking points.

---

## Features

| Area | Capabilities |
|------|--------------|
| **Accounts** | User registration, password hashing with BCrypt, sign-in with a JWT, and authenticated profile updates |
| **Clubs & divisions** | Create, list, update, and delete clubs and divisions; look up competitions associated with a division |
| **Competitions** | Create and manage scheduled competitions, with a date, capacity, notes, and participating clubs |
| **Cups** | Manage cups, query them by date range, and retrieve active cups |
| **Games & results** | List and retrieve games, filter games by host or guest club, and manage game recaps |
| **Rankings** | Manage ranking ranges and retrieve users sorted by ranking points |
| **Deployment** | GitHub Actions workflow builds the application and deploys it to Azure App Service when changes are pushed to `main` |

---

## Tech Stack

- **Java 17**
- **Spring Boot 2.7.4**
- **Spring Web** for REST endpoints
- **Spring Data JPA** and **Hibernate** for persistence
- **PostgreSQL** database driver
- **Auth0 java-jwt** for JWT creation and verification
- **jBCrypt** for password hashing
- **Maven** for dependency management and builds
- **JUnit 5 / Spring Boot Test** for automated tests

---

## API Overview

The application runs on port **8080** by default. Unless otherwise noted, endpoints accept and return JSON.

### Accounts

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/user` | Register a user |
| `POST` | `/authentication/connect` | Sign in and receive a JWT |
| `PUT` | `/user/{email}` | Update the authenticated user's profile |
| `GET` | `/users` | List users |
| `GET` | `/usersWithoutClub` | List users who do not belong to a club |
| `GET` | `/users/admin` | List users flagged as administrators |
| `GET` | `/users/ranking` | List users ordered by ranking points |

### Clubs and divisions

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/club` | Create a club |
| `GET` | `/clubs` | List clubs |
| `GET` | `/club/{name}` | Retrieve a club by name |
| `PUT` | `/club/{name}` | Update a club |
| `DELETE` | `/club/{name}` | Delete a club |
| `POST` | `/division` | Create a division |
| `GET` | `/divisions` | List divisions |
| `GET` | `/divisions/{divisionName}` | Retrieve competitions associated with a division |
| `PUT` | `/division/{name}` | Update a division |
| `DELETE` | `/division/{name}` | Delete a division |

### Competitions and cups

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/competition` | Create a competition |
| `GET` | `/competitions` | List competitions |
| `GET` | `/competitions/{date}` | Retrieve a competition by date (`YYYY-MM-DD`) |
| `PUT` | `/competition/{date}` | Update a competition |
| `DELETE` | `/competition/{date}` | Delete a competition |
| `POST` | `/cup` | Create a cup |
| `GET` | `/cups` | List cups |
| `GET` | `/cup/{name}` | Retrieve a cup by name |
| `GET` | `/cup?start={date}&end={date}` | Find cups in a date range |
| `GET` | `/cups/active` | List active cups |
| `PUT` | `/cup/{name}` | Update a cup |
| `DELETE` | `/cup/{name}` | Delete a cup |

### Games, recaps, and rankings

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/game` | Create a game |
| `GET` | `/games` | List games |
| `GET` | `/game/{id}` | Retrieve a game |
| `GET` | `/game/host/{hostClubName}` | Find games by host club |
| `GET` | `/game/guest/{guestClubName}` | Find games by guest club |
| `DELETE` | `/game/{id}` | Delete a game |
| `POST` | `/gameRecap` | Create a game recap |
| `GET` | `/gameRecaps` | List game recaps |
| `GET` | `/gameRecap/{id}` | Retrieve the recap associated with a game |
| `DELETE` | `/gameRecap/{id}` | Delete a game recap |
| `POST` | `/ranking` | Create a ranking category |
| `GET` | `/rankings` | List ranking categories |
| `GET` | `/ranking/{name}` | Retrieve a ranking category |
| `PUT` | `/ranking/{name}` | Update a ranking category |
| `DELETE` | `/ranking/{name}` | Delete a ranking category |

### Sign-in example

```bash
curl -X POST http://localhost:8080/authentication/connect \
  -H "Content-Type: application/json" \
  -d '{"email":"player@example.com","password":"your-password"}'
```

The response contains a JWT token. Send that token in the `Authorization` header when calling `PUT /user/{email}`.

---

## Getting Started

### Prerequisites

- Java 17
- PostgreSQL

### Configure the database

Set the PostgreSQL connection details for the application before starting it. The standard Spring Boot settings are `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password`; these can be supplied through external configuration or environment variables. Do not commit database credentials or signing secrets to a public repository.

For example, environment variables can be set in your shell:

```bash
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/kickerdavinci"
export SPRING_DATASOURCE_USERNAME="your-database-user"
export SPRING_DATASOURCE_PASSWORD="your-database-password"
```

### Build and run

```bash
# Clone the repository, then enter the backend directory
cd kickerdavinci_backend

# Run the automated tests
./mvnw test

# Start the API
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

On Windows, use `mvnw.cmd` instead of `./mvnw`.

### Testing endpoints

The `src/test` directory contains `.http` request collections that can be run from an IDE with an HTTP client plugin. Update their URLs and request data to match your local database and running API.

---

## Repository Structure

```text
kickerdavinci_backend/
├── .github/
│   └── workflows/                 # Azure build and deployment workflow
├── src/
│   ├── main/
│   │   ├── java/com/example/kickerdavinci/
│   │   │   ├── Controllers/       # REST API endpoints
│   │   │   ├── Models/            # JPA entities and request models
│   │   │   ├── Repository/        # Spring Data repositories
│   │   │   ├── Services/          # Application and domain logic
│   │   │   └── KickerDaVinciApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/                  # Automated tests
│       └── *.http                 # Sample API requests
├── .mvn/                          # Maven Wrapper files
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

---

<div align="center">

**KickerDaVinci** — backend services for managing clubs, competitions, and match results.

</div>
