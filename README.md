# Task Management System

## Description

The **Task Management System** is a Spring Boot-based REST API designed for managing tasks. It supports user authentication, task creation, updating, querying, and deletion. The application also includes features such as notifications based on task deadlines and an audit trail of task actions.

### Features:
- **User Management**: User registration, login, and querying.
- **Task Management**: Create, update, delete, and view tasks.
- **Task Notification**: Email notifications for task deadlines.
- **History Trail**: Track task actions (creation, modification, deletion) with user details.
- **Swagger Documentation**: Easily explore and test APIs.

## Requirements

- **Java 17** 
- **Maven** for dependency management
- **Spring Boot** for the application framework
- **JWT** for authentication and authorization

## Running the Application

### 1. Clone the Repository
```bash
git clone https://github.com/Fady32/TaskManagement.git
cd TaskManagement
```
## 2. Build the Application
Make sure you have Maven installed. Run the following command to build the project:
```bash
mvn clean install
```


##  3. Run the Application
   Run the application with the following command:

```bash
mvn spring-boot:run
```

### By default, the application will be available at:

Base URL: http://localhost:8080
**Swagger UI: http://localhost:8080/** **(use Swagger UI to test the API)**

## 4. API Authentication

By default, there is an `admin` user with the following credentials:
- **Username**: `admin`
- **Password**: `admin`
- **Role**: `admin`

To authenticate and access other endpoints, follow these steps:

### 4.1 Login

Use the `POST /api/auth/login` API to log in with the `admin` credentials (or any other valid user credentials). Upon successful login, you will receive a JWT token.

### 4.2 Response Example

{
"token": "JWT_TOKEN"
}


#### Request Example:

```bash
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{
  "username": "admin",
  "password": "admin"
}'

```
## Available APIs

### User Management
- **POST /api/auth/register**: Register a new user.
- **POST /api/auth/login**: Login and get a JWT token.
- **GET /api/v1/users**: Get a list of users.
- **GET /api/v1/users/{username}**: Get user by username.

### Task Management
- **POST /api/v1/tasks**: Create a new task.
- **GET /api/v1/tasks/{id}**: Get a task by ID.
- **PUT /api/v1/tasks/{id}**: Update an existing task.
- **DELETE /api/v1/tasks/{id}**: Delete a task.

### Notification Management
- **GET /api/notifications**: Get all notifications.
- **POST /api/notifications**: Save a new notification.

### Task History
- **POST /api/v1/histories**: Filter task histories based on criteria.
- **GET /api/v1/histories/{id}**: Get task history by ID.

## Example Request for Creating a Task:

You can use the following `curl` command to create a new task:

```bash
curl -X POST http://localhost:8080/api/v1/tasks \
-H "Content-Type: application/json" \
-d '{
  "title": "New Task",
  "description": "This is a new task",
  "status": "TODO",
  "priority": "MEDIUM",
  "dueDate": "2024-12-10", // present or future date
  "assignedUsernames": ["username"]
}'
```

## Diagrams : 


![Endpoints Diagram.png](src/main/resources/static/Endpoints%20Diagram.png)

----

![Entity Diagram.png](src/main/resources/static/Entity%20Diagram.png)