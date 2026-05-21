# Task Management System

Complete REST API backend for Task Management System built with Spring Boot 4.x, PostgreSQL, JWT authentication, and microservices-ready architecture.

## Features

- **User Management** - Authentication, registration, user profiles
- **Project Management** - Create, update, delete projects with members
- **Task Management** - Complete task lifecycle with filtering and searching
- **Comments & Attachments** - Comments on tasks with file uploads
- **Notifications** - Real-time notifications for task assignments and updates
- **JWT Security** - Token-based authentication with role-based access control
- **Async Processing** - Background task execution with @Async
- **API Documentation** - Swagger/OpenAPI 3.0 documentation
- **Comprehensive Logging** - Aspect-oriented logging with logback
- **Docker Support** - Multi-stage Docker build with docker-compose

## Technology Stack

- **Framework**: Spring Boot 4.x
- **Language**: Java 21
- **Database**: PostgreSQL 15
- **Security**: JWT (jjwt 0.13.0)
- **API Documentation**: SpringDoc OpenAPI 2.x
- **Build Tool**: Maven
- **Logging**: Logback with SLF4J
- **Containerization**: Docker & Docker Compose

## Project Structure

```
TaskManagementSystem/
├── src/main/java/kz/ablaysharimov/taskmanagementsystem/
│   ├── config/              # Configuration classes
│   ├── controller/          # REST controllers
│   ├── dto/                 # Data Transfer Objects
│   │   ├── request/
│   │   └── response/
│   ├── entity/              # JPA entities
│   ├── exception/           # Custom exceptions
│   ├── mapper/              # DTO mappers
│   ├── repository/          # JPA repositories
│   ├── security/            # Security components
│   ├── service/             # Business logic
│   │   └── impl/
│   ├── aspect/              # AOP aspects
│   └── Application.java
├── src/main/resources/
│   ├── application.yml
│   ├── application-docker.yml
│   └── logback-spring.xml
├── pom.xml
├── Dockerfile
├── docker-compose.yml
└── README.md
```

## Quick Start

### Prerequisites

- Java 21
- Maven 3.9+
- PostgreSQL 15
- Docker & Docker Compose (optional)

### Local Development

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd TaskManagementSystem
   ```

2. **Configure Database**
   Update `src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/taskmanagement_db
       username: postgres
       password: postgres
   ```

3. **Build the project**
   ```bash
   mvn clean package
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the API**
   - API Base URL: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - API Docs: http://localhost:8080/v3/api-docs

### Docker Deployment

1. **Build and run with Docker Compose**
   ```bash
   docker-compose up -d
   ```

2. **Access services**
   - Application: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - PostgreSQL: localhost:5432
   - PgAdmin: http://localhost:5050

3. **Stop services**
   ```bash
   docker-compose down
   ```

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user

### Users
- `GET /api/users` - Get all users (ADMIN only)
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/me` - Get current user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Deactivate user (ADMIN only)
- `POST /api/users/me/profile-picture` - Upload profile picture

### Projects
- `POST /api/projects` - Create project
- `GET /api/projects` - Get all projects
- `GET /api/projects/{id}` - Get project details
- `PUT /api/projects/{id}` - Update project
- `DELETE /api/projects/{id}` - Delete project
- `POST /api/projects/{id}/members/{userId}` - Add member
- `DELETE /api/projects/{id}/members/{userId}` - Remove member
- `GET /api/projects/my` - Get my projects

### Tasks
- `POST /api/tasks` - Create task
- `GET /api/tasks` - Get tasks (with filtering, searching, pagination)
- `GET /api/tasks/{id}` - Get task details
- `PUT /api/tasks/{id}` - Update task
- `DELETE /api/tasks/{id}` - Delete task
- `PATCH /api/tasks/{id}/status` - Change task status
- `PATCH /api/tasks/{id}/assign` - Assign task
- `GET /api/tasks/my` - Get my tasks
- `GET /api/tasks/overdue` - Get overdue tasks (async)
- `GET /api/tasks/stats/project/{projectId}` - Get project stats (async)

### Comments
- `POST /api/comments` - Add comment
- `GET /api/comments/task/{taskId}` - Get task comments
- `PUT /api/comments/{id}` - Update comment
- `DELETE /api/comments/{id}` - Delete comment

### Files
- `POST /api/files/upload/task/{taskId}` - Upload file
- `GET /api/files/download/{attachmentId}` - Download file
- `DELETE /api/files/{attachmentId}` - Delete file
- `GET /api/files/task/{taskId}` - Get task attachments

### Notifications
- `GET /api/notifications` - Get my notifications
- `PATCH /api/notifications/{id}/read` - Mark as read
- `PATCH /api/notifications/read-all` - Mark all as read
- `GET /api/notifications/unread-count` - Get unread count

## Authentication

The API uses JWT (JSON Web Tokens) for authentication. Include the token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

## Database Schema

The application automatically creates database tables on startup using Hibernate with `ddl-auto: update`.

### Main Entities
- **AblaySharimovUser** - System users
- **AblaySharimovProject** - Projects
- **AblaySharimovTask** - Tasks
- **AblaySharimovComment** - Task comments
- **AblaySharimovAttachment** - File attachments
- **AblaySharimovNotification** - User notifications

## Configuration

### application.yml

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/taskmanagement_db
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: <your-256-bit-base64-secret>
  expiration: 86400000

file:
  upload-dir: ./uploads
```

## Security Features

- JWT-based authentication
- Role-based access control (ADMIN, MANAGER, USER)
- Password encryption with BCrypt
- CORS configuration
- CSRF protection
- Stateless session management
- Method-level security annotations

## Logging

Logs are configured in `logback-spring.xml`:
- **Console**: Immediate feedback during development
- **File**: `logs/taskmanagement.log` (daily rolling)
- **Error File**: `logs/error.log` (error-level only)

## Performance Features

- Async task execution with ThreadPoolTaskExecutor
- Pagination support for all list endpoints
- Optimized queries with projections
- Eager/Lazy loading strategies
- Connection pooling

## Error Handling

The API returns standardized error responses:

```json
{
  "timestamp": "2026-05-21T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/users",
  "field_errors": {
    "email": "Email should be valid"
  }
}
```

## Development

### Running Tests
```bash
mvn test
```

### Building JAR
```bash
mvn clean package
```

### Running with Different Profiles
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=docker"
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License

## Author

**AblaySharimov**

## Support

For support, email ablaysharimov@example.com or check the documentation at http://localhost:8080/swagger-ui.html

