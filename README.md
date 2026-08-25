# VoltPay - Electricity Bill Payment REST API

A Spring Boot backend for viewing and paying electricity bills, with HTTP
Basic authentication (Spring Security), role-based access control, and a
PostgreSQL-backed data layer. This is a pure JSON REST API — there is no
bundled web UI; consume it with curl, Postman, or your own frontend.

## Tech Stack

- Java 17
- Spring Boot 4.1.1 (Spring MVC, Spring Security 7, Spring Data JPA)
- Jackson 3 (JSON serialization)
- PostgreSQL

## Features

- User registration (`POST /api/auth/register`) with Bean Validation
- HTTP Basic authentication; BCrypt-hashed passwords
- Role-based access: `ROLE_USER` (customers) and `ROLE_ADMIN` (bill generation)
- Dashboard summary endpoint (pending balance, bill list, recent payments)
- List/view bills and their status (Pending / Paid / Overdue)
- Simulated bill payment (Credit Card / Debit Card / Net Banking / UPI) that
  generates a transaction ID and marks the bill paid — no real card or bank
  details are collected, this is a mock gateway for demo purposes
- Payment history
- Admin endpoint to generate new bills for a customer by consumer number
- JSON error responses for validation failures, business-rule violations,
  authentication failures (401), and authorization failures (403)

## Project Structure

```
src/main/java/com/electricity/billpayment/
  config/         DataInitializer (seeds demo accounts/bills on first run)
  controller/     REST controllers (Auth, User, Dashboard, Bill, Payment, Admin)
  dto/            Request DTOs (Bean Validation) and response DTOs
  exception/      @RestControllerAdvice — maps exceptions to JSON error bodies
  model/          JPA entities (User, Bill, Payment) and enums
  repository/     Spring Data JPA repositories
  security/       SecurityConfig (HTTP Basic, JSON 401/403) + CustomUserDetailsService
  service/        Business logic (UserService, BillService, PaymentService)
```

## Running Locally

### 1. Start PostgreSQL

Using Docker Compose (recommended):

```bash
docker compose up -d
```

This starts Postgres on `localhost:5432` with database `electricity_billing`,
user `postgres`, password `postgres` — matching the defaults in
`src/main/resources/application.properties`.

If you'd rather use an existing local PostgreSQL install, just create the database:

```sql
CREATE DATABASE electricity_billing;
```

and update `spring.datasource.username` / `spring.datasource.password` in
`application.properties` to match your instance.

### 2. Run the application

```bash
./mvnw.cmd spring-boot:run
```

The app starts on **http://localhost:8081** (configurable via `server.port`).
Hibernate auto-creates the schema, and a `CommandLineRunner` seeds two demo
accounts the first time it runs against an empty database:

| Role  | Username | Password  | Notes                                   |
|-------|----------|-----------|--------------------------------------------|
| Admin | `admin`  | `admin123`| Can generate new bills                     |
| User  | `john`   | `john123` | Has one pending and one paid demo bill     |

All endpoints except `POST /api/auth/register` require HTTP Basic auth.

### 3. Try it out with curl

```bash
# Register a new account
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"jane","password":"pass1234","confirmPassword":"pass1234","fullName":"Jane Doe","email":"jane@example.com","consumerNumber":"CN-20001"}'

# Dashboard summary
curl -u john:john123 http://localhost:8081/api/dashboard

# List bills
curl -u john:john123 http://localhost:8081/api/bills

# Pay a pending bill (id 1 from the seed data)
curl -X POST http://localhost:8081/api/bills/1/pay \
  -u john:john123 -H "Content-Type: application/json" \
  -d '{"paymentMethod":"UPI"}'

# Payment history
curl -u john:john123 http://localhost:8081/api/payments

# Admin: generate a new bill
curl -X POST http://localhost:8081/api/admin/bills \
  -u admin:admin123 -H "Content-Type: application/json" \
  -d '{"consumerNumber":"CN-10001","billMonth":"2026-09","unitsConsumed":190,"dueDate":"2026-09-30"}'

# Admin: list all bills
curl -u admin:admin123 http://localhost:8081/api/admin/bills
```

## API Reference

| Method | Path                     | Auth        | Description                                  |
|--------|--------------------------|-------------|-----------------------------------------------|
| POST   | `/api/auth/register`     | Public      | Create a new customer account                  |
| GET    | `/api/users/me`          | Any user    | Current user's profile                         |
| GET    | `/api/dashboard`         | Any user    | Balance summary, pending bills, recent payments|
| GET    | `/api/bills`             | Any user    | List the caller's bills                        |
| GET    | `/api/bills/{id}`        | Any user    | Bill detail (must be owned by caller)          |
| POST   | `/api/bills/{id}/pay`    | Any user    | Pay a pending bill                             |
| GET    | `/api/payments`          | Any user    | Payment history                                |
| GET    | `/api/payments/{id}`     | Any user    | Payment detail                                 |
| GET    | `/api/admin/bills`       | `ROLE_ADMIN`| List every bill across all customers           |
| POST   | `/api/admin/bills`       | `ROLE_ADMIN`| Generate a new bill for a consumer number      |

Error responses share a common shape:

```json
{
  "timestamp": "2026-08-22T11:09:14.244Z",
  "status": 400,
  "message": "Validation failed",
  "fieldErrors": { "email": "Please provide a valid email address" }
}
```

## Configuration

All datasource, JPA, and server settings live in
[`src/main/resources/application.properties`](src/main/resources/application.properties).
For anything beyond local development, override the datasource credentials via
environment variables or a `application-prod.properties` profile rather than
committing real secrets.

## Security Notes

- Passwords are hashed with BCrypt (`PasswordEncoder` bean in `SecurityConfig`).
- Authentication is HTTP Basic; unauthenticated/unauthorized requests get a
  JSON `401`/`403` body instead of an HTML page.
- CSRF protection is disabled — appropriate for a stateless JSON API
  consumed by non-browser clients rather than HTML forms.
- `/api/admin/**` is restricted to `ROLE_ADMIN`; every other endpoint except
  `/api/auth/register` requires authentication.
- The payment flow is a **simulation** — no real payment gateway integration,
  and no card/bank account numbers are ever collected or persisted.
