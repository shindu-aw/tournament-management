# Tournament Management System

A web-based tournament management platform built with **Java 21**, **Spring Boot**, and **MariaDB**, designed for
organizing tournaments, managing teams, scheduling matches, and handling announcements with robust role-based security.

## ⚡️ Features

- **User & Role Management**: Registration, profile updates, password changes (with role-based permissions: user,
  moderator, admin).
- **Tournament Life Cycle**: Create, update, finish, and auto-cleanup of tournaments (with custom procedures for
  archival/removal).
- **Team Registration**: Register and manage teams using join/manage secret codes.
- **Match Scheduling & Results**: Schedule matches, track scores (with DB triggers automatically updating team results).
- **Announcements**: Owners can create announcements.
- **Moderator Tools**: Moderator management via secret codes.
- **Database Migrations**: Fully automated schema updates via Flyway, including advanced logic (custom procedures,
  cleanup tasks).
- **Multiple Environments / Profiles**: Seamless switching between `dev`, `prod`, `h2-test`, and `flyway-clean`
  profiles.
- **Integration & E2E Tests**: Support for H2 and MariaDB via Testcontainers (requires Docker).

## 🏗️ Tech Stack

- Java 21
- Spring Boot
- Spring MVC, Data JPA
- MariaDB (Also supports H2 for testing)
- Flyway (for DB migrations, triggers, and procedures)
- Maven (build and dependency management)
- Docker (for integration testing with Testcontainers)

## 🔧 Requirements

- JDK 21 or later
- Maven
- MariaDB database server
- Docker (for running integration tests using Testcontainers)

## 🚀 Getting started

#### 1. Clone the repository

```bash
git clone https://github.com/shindu-aw/tournament-management.git
cd tournament-management
```

#### 2. Configure environment variables

Create a `.env` file in the project root with the following fields, or set these as system environment variables:

```dotenv
# Environemntal variables used in Spring Boot configuration (application.yml)
DB_HOST=localhost
DB_PORT=3306
DB_USERNAME=root
DB_PASSWORD=password
DB_NAME=tournaments
```

_See [application.yml](src/main/resources/application.yml) for all available configuration settings and active
profiles._

#### 3. Build the application

Use Maven to build the application:

```bash
mvn clean install
```

#### 4. Set the profiles

- `dev`: Default for development (debug logging).
- `h2-test`: Runs with H2 DB, skips Flyway and uses Hibernate `create-drop`.
- `flyway-clean`: Enables database cleanup/experimentation (do not **ever** use in production).
- `prod`: Production settings, stricter config (no debug logging).

Switch profiles by setting `SPRING_PROFILES_ACTIVE` environment variable or modifying `application.yml`.

_(If you want to set the profiles through the environment variables, you should first remove all active profiles
from [application.yml](src/main/resources/application.yml))_

#### 5. Run the application

You can start the application in your IDE, or use Maven:

```bash
mvn spring-boot:run
```

The application will start on the default port (usually 8080).

#### 6. Access the web interface

Open your web browser and navigate to: http://localhost:8080/

New users can browse the website, while logged-in users can create tournaments, add teams, create matches, post
announcements, etc.

## ⚙️ Database Schema & Logic

- Automated migrations ensure the schema is valid on startup (via Flyway).
- DB triggers update total scores on match insert/update/delete.
- Admins and tournament owners can run a stored procedure to recount the total scores of teams in a tournament.
- Admins can run a stored function to clean up old tournaments and associated data.
- DB indexes optimize queries on frequently searched fields.

## 🧪 Testing

- **Integration tests**: Run with H2 or MariaDB via Testcontainers (Docker required).
  - Lighter integration tests run with H2, and the ones requiring Flyway support run with Testcontainers.
- **Flyway migrations**: Applied automatically, unless disabled by profile.