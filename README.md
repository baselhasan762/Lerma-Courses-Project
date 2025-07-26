Skill Hub



&nbsp;Spring Boot-based REST API for an online course platform, featuring authentication, course management, enrollments, and reviews.

Built with Spring Boot 3, JWT Authentication, and Swagger UI for API documentation.



✅ Features

User Authentication \& Authorization



Login \& Registration



Role-based access (Admin, User, Student) 



Course Management



Course Purchase Virtual System



Create, update, delete, list courses



Lesson Management



Add lessons to courses



Enrollments



Users can enroll in courses



Reviews



Add course reviews and ratings



API Documentation



Integrated with Swagger UI



Global Exception Handling



✅ Tech Stack

Backend: Java 17, Spring Boot 3



Security: Spring Security, JWT



Database: MySQL (configurable)



Build Tool: Maven



Documentation: Swagger (OpenAPI)



Other: JPA, Hibernate



✅ How to Run

Prerequisites

Java 17+



Maven 3.9+



MySQL Database



IDE: IntelliJ / Eclipse / VS Code



Steps

Clone the project



git clone <repository-url>

cd tenmacourses

Configure Database

src/main/resources/application.properties:



spring.datasource.url=jdbc:mysql://localhost:3306/tenmacourses

spring.datasource.username=root

spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update

jwt.secret=YourJWTSecretKey



Build the Project:



mvn clean install

Run the Application:



mvn spring-boot:run

✅ Swagger UI

Once the application is running, open:



http://localhost:8080/swagger-ui.html

✅ Packages Explanation

Config → JWT filter, security configuration, Swagger setup.



Controller → Handles API endpoints for courses, lessons, users, reviews.



DTO → Request and Response models for API communication.



Entity → Database entities mapped with JPA.



Repository → Data access layer (CRUD via Spring Data JPA).



Service → Business logic for authentication, course management.



Handler → Global exception handler for cleaner responses.





