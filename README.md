# Spring Boot 3 Project to manage todo list for users
## Prerequisites

- Java 17 or higher
- Maven 3 or higher

## Getting Started

1. Clone this repository.
   ```
   git clone https://github.com/ImadAmahmid/todo-list_backend
   ```

2. Build the project.
    ```
   mvn clean install
   ```

3. Run the project.
    ```
   mvn spring-boot:run
   ```

4. Access the Swagger UI at [http://localhost:8088/swagger-ui/index.html](http://localhost:8088/swagger-ui/index.html).

## Features

- Spring Security for authentication and authorization.
- Swagger UI for API documentation using OpenAPI 3.
- CRUD operations for todo lists per users

## Usage

1. Run the application.
2. Access the Swagger UI at [http://localhost:8088/swagger-ui/index.html](http://localhost:8088/swagger-ui/index.html).
3. Use the Swagger UI to test the API endpoints.
4. Use the register end point first to store the users in the h2 database and then with the access token you can use the todos end points
