# Spring Security + JWT Implementation

A hands-on Spring Boot project implementing user authentication with Spring
Security, BCrypt password hashing, and JWT token generation. Built to learn
the internals of Spring Security's authentication flow rather than relying on
default form login.

## Tech Stack

- Java 21
- Spring Boot 3.5.11
- Spring Security
- Spring Data JPA (Hibernate)
- MySQL
- JJWT (io.jsonwebtoken) 0.12.6
- Lombok
- Maven

## Features

- User registration with email, name, and password
- Passwords hashed with `BCryptPasswordEncoder` before persisting
- Role assignment on user creation (`ROLE_USER` by default, `ROLE_ADMIN` supported)
- Login via `AuthenticationManager` + `DaoAuthenticationProvider` using a
  custom `UserDetailsService`
- JWT access token generated on successful login (subject = email, custom
  `userId` claim)
- Public endpoints (`/users/signup`, `/users/login`) vs. authenticated
  endpoints configured through `SecurityFilterChain`
- Request validation on signup/login DTOs using Jakarta Bean Validation

## Project Structure

```
src/main/java/com/spring_security/Impl/
├── config/
│   └── SecurityConfig.java          # SecurityFilterChain, PasswordEncoder, AuthenticationManager beans
├── controller/
│   └── UserController.java          # /users/signup, /users/login
├── dtos/
│   ├── SignUpDto.java
│   ├── SignUpResponse.java
│   ├── LoginRequestDto.java
│   └── LoginResponseDto.java
├── entity/
│   ├── User.java                    # JPA entity with roles as ElementCollection
│   └── Role.java                    # ROLE_USER, ROLE_ADMIN
├── repository/
│   └── UserRepository.java
├── secuity/
│   ├── CustomUserDetails.java       # UserDetails wrapper around User entity
│   ├── CustomUserDetailsService.java
│   └── JwtService.java              # Token generation & claims extraction
├── service/
│   └── UserService.java
└── serviceImpl/
    └── UserServiceImpl.java
```

## How Authentication Works

1. **Signup** (`POST /users/signup`) — validates input, checks for duplicate
   email, hashes the password with BCrypt, saves the user, and defaults their
   role to `ROLE_USER` via `@PrePersist`.
2. **Login** (`POST /users/login`) — builds a
   `UsernamePasswordAuthenticationToken` and passes it to `AuthenticationManager`,
   which delegates to `CustomUserDetailsService` to load the user and verify
   the password. On success, `JwtService` issues a signed JWT (HMAC-SHA256)
   containing the user's email as subject and `userId` as a custom claim.

## Getting Started

### Prerequisites
- JDK 21
- Maven
- MySQL running locally

### Setup

1. Clone the repository
   ```bash
   git clone https://github.com/riteshdawale02/spring-security-implementation.git
   cd spring-security-implementation/Impl
   ```

2. Create the database (or let Hibernate create it automatically):
   ```sql
   CREATE DATABASE security_db;
   ```

3. Configure `src/main/resources/application.properties` with your local
   MySQL credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/security_db?createDatabaseIfNotExist=true
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```
   > **Note:** Avoid committing real credentials. Prefer environment
   > variables or a `.env`/`application-local.properties` file excluded via
   > `.gitignore` for anything beyond local experimentation.

4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

The app starts on `http://localhost:8080`.

## API Endpoints

### Register
```
POST /users/signup
Content-Type: application/json

{
  "name": "Ritesh Dawale",
  "email": "ritesh@example.com",
  "password": "secret123"
}
```

### Login
```
POST /users/login
Content-Type: application/json

{
  "email": "ritesh@example.com",
  "password": "secret123"
}
```

**Response:**
```json
{
  "time": "14:32:10",
  "message": "Login Successfully...",
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

## Roadmap / Work in Progress

This project is an active learning exercise — the following pieces are
intentionally incomplete or planned next:

- [ ] **JWT authentication filter** — a token is issued on login, but no
      `OncePerRequestFilter` is yet wired into `SecurityConfig` to validate
      the token on subsequent requests to protected endpoints.
- [ ] Complete `JwtService.extractUserName()` / claim extraction helpers.
- [ ] Add token expiration handling and a refresh token flow.
- [ ] Externalize JWT secret and DB credentials via environment variables.
- [ ] Add role-based method security (`@PreAuthorize`) examples.
- [ ] Add unit/integration tests for auth flows.
- [ ] Global exception handling (`@ControllerAdvice`) for cleaner error
      responses (e.g., duplicate email currently throws a generic
      `RuntimeException`).

## Learning Goals

This project was built to understand, from the ground up:
- How Spring Security's `AuthenticationManager` / `AuthenticationProvider`
  chain works
- How to bridge a JPA entity to Spring Security via a custom `UserDetails`
  implementation
- How JWTs are structured, signed, and issued after authentication
- Why password hashing (BCrypt) matters and how it's wired into Spring Security

## License

This project is for educational purposes.
