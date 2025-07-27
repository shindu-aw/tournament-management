# Tournament Management System

The Tournament Management Application is a web-based platform built with Java Spring Boot. Its purpose is to help users
organize and manage tournaments efficiently. The application covers several aspects including tournament creation, team
registration, match scheduling, announcement management, and moderator control. It also provides authorization features
using secret codes and role-based access to ensure that only authorized users can perform sensitive operations.
The application leverages Spring MVC for controllers, Spring Data JPA for persistence, and Flyway for database
migrations and cleanup tasks

## Features

- **User Management**: Supports registration and secure login. Users can update their profiles and change passwords.
- **Tournament Management**: Create, edit, and delete tournaments. Tournaments can have start and end dates along with
  descriptions.
- **Match Scheduling**: Create and update matches between teams within a tournament. Score tracking and match deletion
  are supported.
- **Team Registration**: Teams can be registered to tournaments using secret codes. The system filters out already
  registered teams.
- **Announcements**: Tournament owners can post announcements for participants.
- **Moderator Functions**: Secret-code-based moderator application and removal ensure that only trusted users manage
  tournaments.
- **Administrative Tools**: Admins can perform tasks such as deleting tournaments that are older than a specified date.
- **Authorization Checks**: Critical operations include authorization logic that validates the current user's rights
  based on roles and secret codes.
- **Database Migrations**: Uses Flyway for managing schema changes and cleaning up the database in test or cleanup
  profiles

## Requirements

- **Java:** JDK 21 or later
- **Build tool:** Maven is used for building and dependency management
- **Database:** H2 and MariaDB `Localcontainers` are used for tests, but for running the application, a MariaDB database
  is needed.
- **Docker:** Docker is needed to run the integration tests, since `Localcontainers` need it.

## Installation

#### 1. Clone the repository

Use Git to clone the repository:

```bash
git clone https://github.com/shindu-aw/tournament-management.git
```

#### 2. Navigate to the project directory

```bash
cd tournament-management
```

#### 3. Build the project

Use Maven to build the application:

```bash
mvn clean install
```

#### 4. Configure environment variables

To run this project, you will need to create an `.env` file at the root of the project. Example of the `.env` file with
all the necessary fields is included below (connection info to a MariaDB database).

```dotenv
# Environemntal variables used in Spring Boot configuration (application.yml)
DB_HOST=localhost
DB_PORT=3306
DB_USERNAME=root
DB_PASSWORD=password
DB_NAME=tournaments
```

Alternatively, those variables can be passed as system environment variables or command-line arguments.

## Usage

#### 1. Run the application

You can start the application in your IDE, or use Maven:

```bash
mvn spring-boot:run
```

The application will start on the default port (usually 8080).

#### 2. Access the web interface

Open your web browser and navigate to: http://localhost:8080

New users can browse the website, while logged-in users can create tournaments, add teams, create matches, post
announcements, etc.