# Task Manager API

A secure REST API built using Spring Boot with JWT authentication and role-based access control.

## 🚀 Features

- JWT Authentication
- Role-Based Authorization (ADMIN / USER)
- User-Specific Task Access
- Soft Delete for Tasks
- Pagination Support
- Validation using @Valid
- Global Exception Handling
- Swagger API Documentation

## 🛠 Tech Stack

- Java 21
- Spring Boot 3.3.5
- Spring Security
- JWT (jjwt)
- MySQL
- Maven
- Swagger (OpenAPI)

## 🔐 Authentication

Login endpoint:

POST /auth/login

Use the returned JWT token in:

Authorization: Bearer <your_token>

## 📄 Swagger Documentation

After running the application:

http://localhost:8080/swagger-ui/index.html

## 🗂 Project Structure

- controller
- service
- repository
- model
- security
- dto
- exception

## 👩‍💻 Author

Helinia