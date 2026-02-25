# Project Management API

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

- Java 25
- Spring Boot
- Spring Web
- Spring Data JPA (Hibernate)
- Spring Security
- JWT Authentication
- PostgreSQL (or H2 for development)
- Lombok
- Maven
- OpenAPI / Swagger

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

## 🔐 Authentication and Authorization

- JWT-based authentication
- Access Token (short-lived)
- Refresh Token (long-lived)
- BCrypt password hashing
- Role-based authorization

Only project members can access project resources.  
Only project owners can manage members.

---

## 📡 REST API Endpoints

### Swagger UI
All API Endpoints are listed, documented and ready to be tested with Swagger API at http://localhost:8080/swagger-ui/index.html#/

### Authentication

- POST /api/v1/auth/register
- POST /api/v1/auth/refresh-token
- POST /api/v1/auth/authenticate

### Projects

- GET /api/v1/projects/{id}
- PUT /api/v1/projects/{id}
- DELETE /api/v1/projects/{id}
- GET /api/v1/projects
- POST /api/v1/projects

### Project Members

- GET /api/projects/{projectId}/members
- POST /api/projects/{projectId}/members
- PUT /api/projects/{projectId}/members/{userId}/updateRole
- DELETE /api/projects/{projectId}/members/{userId}

### Tasks
- GET /api/projects/{projectId}/tasks
- POST /api/projects/{projectId}/tasks
- PUT /api/tasks/{taskId}
- PATCH /api/tasks/{taskId}/status
- PATCH /api/tasks/{taskId}/assign
- DELETE /api/tasks/{taskId}

### Users

 -PATCH /api/v1/users

---

## ⚠️ Exception Handling

Custom exceptions:

- ResourceNotFoundException
- DuplicateResourceException
- UnauthorizedActionException
- AuthenticationFailedException


All exceptions are handled via a global `@RestControllerAdvice`.

Standardized error response format:

```json
{
  "timestamp": "2026-02-17T12:00:00"
  "message": "Project not found",
  "details": "Type of Error",
}
```
Mockito for mocking repositories
---
## ▶️ Running the Application
### 1️⃣ Clone repository
git clone https://github.com/your-username/collaborative-task-management-api.git
### 2️⃣ Configure database
Update application.yml:
<pre>
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/taskdb
    username: your_user
    password: your_password
</pre>
Or use H2 for development.
### 3️⃣ Run
mvn spring-boot:run<br>
📖 API Documentation<br>
Swagger UI available at:<br>
http://localhost:8080/swagger-ui.html

---
## 🎯 Design Decisions
- Explicit join entity instead of simple ManyToMany
- DTO-based API (entities are not exposed)
- Centralized exception handling
- Authorization checks in service layer
- Clear separation of concerns

---
## 📌 Future Improvements
- Pagination & sorting
- Docker support
- CI/CD pipeline
- Caching layer













