# todo-spring-boot-backend

# To-Do Application (Spring Boot + JWT Auth)

A secure and modular backend application for managing users and their tasks using Spring Boot, Spring Security, and JWT-based authentication.

---

## Features

- User Signup & Login with JWT Authentication
- Role-based Access Control (USER / ADMIN)
- Create, Read, Update, Delete (CRUD) Task Management
- Secure Password Hashing using BCrypt
- JWT Token Validation with Filter Integration
- Forgot Password with OTP Verification
- CORS Configured for Frontend Integration (React, Angular, etc.)
- Modular Architecture (DTOs, Services, Repositories)

---

## Tech Stack

- Java 17+
- Spring Boot 3
- Spring Security
- JWT (via `io.jsonwebtoken`)
- Spring Data JPA (with H2 or MySQL)
- Lombok
- Maven

---

## Project Structure

com.example.todo
├── config/ // Security and CORS config
├── controller/ // Auth, Task, Admin controllers
├── dto/ // Request/response DTOs
├── model/ // JPA entities: User, Task
├── repository/ // UserRepository, TaskRepository
├── security/ // JWT utils, filter, UserDetails logic
├── service/ // Business logic
└── TodoApplication.java // Main Spring Boot class


---

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- IDE (IntelliJ / VS Code)

### Setup Instructions

```bash
# Clone the repository
git clone https://github.com/your-username/todo-app.git

# Navigate to the project folder
cd todo-app

# Build the application
mvn clean install

# Run the applications
mvn spring-boot:run 
 