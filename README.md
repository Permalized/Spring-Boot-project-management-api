# Collaborative Task Management API

Secure REST API for collaborative project and task management built with Spring Boot.  
The system supports multi-user project collaboration, role-based access control and JWT authentication.

---

## 🚀 Overview

This application allows users to:

- Register and authenticate using JWT
- Create and manage projects
- Collaborate with multiple users per project
- Assign roles within a project (OWNER, MEMBER, VIEWER)
- Create and manage tasks
- Assign tasks to project members
- Enforce role-based authorization rules

The project focuses on **clean architecture, security and proper domain modeling** rather than UI.

---

## 🛠 Tech Stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA (Hibernate)
- Spring Security
- JWT Authentication
- PostgreSQL (or H2 for development)
- Lombok
- Maven
- OpenAPI / Swagger
- JUnit & Mockito

---

## 🏗 Architecture

Layered architecture: controller → service → repository → database

### Main Domain Entities

- **User**
- **Project**
- **ProjectMember** (join entity for many-to-many relationship)
- **Task**

### Relationship Modeling

Instead of using a simple `@ManyToMany`, the relationship between User and Project is modeled through a dedicated `ProjectMember` entity.

This allows storing additional metadata such as:

- Project role (OWNER, MEMBER, VIEWER)
- Join date
- Fine-grained authorization control

---

## 🔐 Security

- JWT-based authentication
- BCrypt password hashing
- Role-based authorization
- Project-level access validation
- Global exception handling

Only project members can access project resources.  
Only project owners can manage members.

---

## 📡 REST API Endpoints

### Authentication

POST /api/auth/register
POST /api/auth/login

### Projects
GET /api/projects
POST /api/projects
GET /api/projects/{id}
PUT /api/projects/{id}
DELETE /api/projects/{id}

### Project Members
GET /api/projects/{projectId}/members
POST /api/projects/{projectId}/members
PUT /api/projects/{projectId}/members/{userId}/role
DELETE /api/projects/{projectId}/members/{userId}

### Tasks
GET /api/projects/{projectId}/tasks
POST /api/projects/{projectId}/tasks
GET /api/tasks/{taskId}
PUT /api/tasks/{taskId}
PATCH /api/tasks/{taskId}/status
PATCH /api/tasks/{taskId}/assign
DELETE /api/tasks/{taskId}

---

## ⚠️ Exception Handling

Custom exceptions:

- ResourceNotFoundException
- DuplicateResourceException
- UnauthorizedActionException
- InvalidStateException
- BadRequestException

All exceptions are handled via a global `@RestControllerAdvice`.

Standardized error response format:

```json
{
  "message": "Project not found",
  "status": 404,
  "timestamp": "2026-02-17T12:00:00"
}
```
🧪 Testing

Unit tests for service layer

Exception scenario testing

Basic integration tests

Mockito for mocking repositories
▶️ Running the Application
1️⃣ Clone repository
git clone https://github.com/your-username/collaborative-task-management-api.git
2️⃣ Configure database
Update application.yml:
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/taskdb
    username: your_user
    password: your_password
Or use H2 for development.
3️⃣ Run
mvn spring-boot:run
📖 API Documentation
Swagger UI available at:
http://localhost:8080/swagger-ui.html

🎯 Design Decisions
-Explicit join entity instead of simple ManyToMany
-DTO-based API (entities are not exposed)
-Centralized exception handling
-Authorization checks in service layer
-Clear separation of concerns

📌 Future Improvements
-Pagination & sorting
-Audit logging
-Docker support
-CI/CD pipeline
-Refresh tokens
-Caching layer













