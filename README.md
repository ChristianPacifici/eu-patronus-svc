# EU Patronus Social Network API

[![Java 21](https://img.shields.io/badge/Java-21-blue.svg)](https://openjdk.org/projects/jdk/21/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.21-purple.svg)](https://kotlinlang.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-orange.svg)](https://gradle.org/)
[![jOOQ](https://img.shields.io/badge/jOOQ-3.x-red.svg)](https://www.jooq.org/)
[![OpenAPI](https://img.shields.io/badge/OpenAPI-3.0.3-brightgreen.svg)](https://www.openapis.org/)

A Spring Boot microservice for the **Patronus Social Network API**, built with **Kotlin**, **Java 21**, **Gradle**, **jOOQ**, and **OpenAPI Generator**.

---

## **Table of Contents**
- [Overview](#overview)
- [Features](#features)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
    - [Clone the Repository](#clone-the-repository)
    - [Configure Database](#configure-database)
    - [Build and Run](#build-and-run)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Testing](#testing)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)

---

## **Overview**
The **Patronus Social Network API** is a microservice that provides RESTful endpoints for managing users, posts, comments, and friendships in a social network platform. The API is auto-generated using **OpenAPI Generator** and uses **jOOQ** for type-safe database access.

---

## **Features**
- **User Management**: Create, read, update, and delete users.
- **Post Management**: Create, read, update, and delete posts.
- **Comment Management**: Create comments on posts.
- **Friendship Management**: Send and update friend requests.
- **OpenAPI/Swagger Documentation**: Auto-generated API docs.
- **Type-Safe Database Access**: Using jOOQ for SQL queries.
- **Kotlin & Java 21**: Modern, concise, and interoperable code.

---

## **Technologies**
| Technology       | Version | Description                          |
|------------------|---------|--------------------------------------|
| **Spring Boot**  | 3.x     | Framework for building microservices |
| **Kotlin**       | 2.1.21  | Primary programming language         |
| **Java**         | 21      | Runtime environment                  |
| **Gradle**       | 8.x     | Build tool                           |
| **jOOQ**         | 3.x     | Type-safe SQL query builder          |
| **OpenAPI**      | 3.0.3   | API specification and code generation|
| **PostgreSQL**   | 14+     | Database (recommended)               |

---

## **Prerequisites**
- **Java 21** (OpenJDK or Oracle JDK)
- **Gradle 8.x**
- **Docker** (optional, for containerized DB)
- **PostgreSQL** (or another supported database)

---

## **Getting Started**

### **Clone the Repository**
```bash
git clone https://github.com/ChristianPacifici/eu-patronus-svc.git
cd eu-patronus-svc
```

### **Configure Database**
1. Create a PostgreSQL database:
   ```sql
   CREATE DATABASE eu_patronus_db;
   ```
   Or spin up a docker instance
   ```shell
   docker run --name eu_patronus_db \
        -e POSTGRES_USER=postgres \
        -e POSTGRES_PASSWORD=password \
        -e POSTGRES_DB=eu_patronus_db \
        -p 5432:5432 \
        --health-cmd="pg_isready -U postgres" \
        --health-interval=10s \
        --health-timeout=5s \
        --health-retries=5 \
        -d postgres:14

   ```
    
2. Update `src/main/resources/application.yml` with your database credentials:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/eu_patronus_db
       username: postgres
       password: password
   ```

### **Build and Run**
1. **Build the project**:
   ```bash
   ./gradlew spotlessApply build
   ```
2. **Run the application**:
   ```bash
   ./gradlew bootRun
   ```
3. Access the API at:
    - **Local**: [http://localhost:8080](http://localhost:8080)
    - **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## **API Documentation**
The API is documented using **OpenAPI/Swagger**. After starting the application, you can explore the interactive API docs at:
- [Swagger UI](http://localhost:8080/swagger-ui.html)
- [OpenAPI JSON](http://localhost:8080/v3/api-docs)

### **Endpoints**
| Endpoint               | Method | Description                          |
|------------------------|--------|--------------------------------------|
| `/api/users`           | GET    | Get all users                        |
| `/api/users`           | POST   | Create a new user                    |
| `/api/users/{id}`      | GET    | Get user by ID                       |
| `/api/users/{id}`      | PUT    | Update user by ID                    |
| `/api/users/{id}`      | DELETE | Delete user by ID                    |
| `/api/posts`           | GET    | Get all posts                        |
| `/api/posts`           | POST   | Create a new post                    |
| `/api/posts/{id}`      | GET    | Get post by ID                       |
| `/api/posts/{id}`      | PUT    | Update post by ID                    |
| `/api/posts/{id}`      | DELETE | Delete post by ID                    |
| `/api/comments`        | POST   | Create a new comment                 |
| `/api/friendships`     | POST   | Send a friend request                |
| `/api/friendships`     | PUT    | Update friendship status             |

---

>**Note**: 
>All the IDs are UUID. Why a UUID as an ID Is Useful?
>Distributed Systems: UUIDs are designed to be globally unique. This means you can generate a new user ID on a different server or even a client-side application without needing to first check the database for conflicts. This is crucial for applications that are scaled across multiple servers or for offline data synchronization.
>Security: Using a UUID makes it much harder for someone to guess or "enumerate" other records in your database. With an auto-incrementing integer ID, an attacker might be able to guess the next user ID by simply incrementing a number (e.g., /users/1, /users/2, /users/3). A UUID, being a long, random string, completely eliminates this vulnerability.
>Data Merging: If you ever need to merge data from two different databases, having a UUID for each record guarantees that there won't be any ID collisions. If you were using simple integer IDs, you would need to reassign IDs to avoid duplicates, which can be a complex and error-prone process.
>Privacy: A UUID doesn't reveal any information about the number of records in your table or the order in which they were created. This can be a minor but useful privacy feature in some contexts.

## **Project Structure**
```
.
├── src/
│   ├── main/
│   │   ├── kotlin/com/patronus/
│   │   │   ├── controller/    # REST controllers
│   │   │   ├── service/       # Business logic
│   │   │   ├── repository/    # jOOQ-based repositories
│   │   │   ├── model/         # Data models (auto-generated)
│   │   │   └── config/        # Configuration classes
│   │   └── resources/
│   │       ├── application.yml # Spring Boot config
│   │       └── db/            # Database scripts
│   └── test/                  # Unit and integration tests
├── build.gradle.kts           # Gradle build script
└── README.md
```

---

## **Configuration**
### **Gradle Plugins**
- **OpenAPI Generator**: Auto-generates API controllers, models, and DTOs.
- **jOOQ**: Generates type-safe SQL queries from your database schema.
- **Spring Boot**: Simplifies microservice development.

### **Database**
- The project uses **jOOQ** for type-safe SQL. Ensure your database schema matches the API requirements.
- Flyway/Liquibase can be added for database migrations.

---

## **Testing**
Run tests with:
```bash
./gradlew test
```
- **Unit Tests**: Mock-based tests for services and controllers.
- **Integration Tests**: Test API endpoints with a real database.

---

## **Deployment**
### **Docker**
1. Build the Docker image:
   ```bash
   docker build -t patronus-social-network .
   ```
2. Run the container:
   ```bash
   docker run -p 8080:8080 patronus-social-network
   ```
   alternatevely, you can the docker compose in the repo

    ```shell
    docker-compose up
    ```

---

## **License**
This project is licensed under the **MIT License**.