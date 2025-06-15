# 🏎️ F1Bet API

F1Bet is a RESTful web application where users can place bets on Formula 1 sessions using real-world driver data. It integrates with the [OpenF1 API](https://api.openf1.org) to retrieve session and driver information, and allows users to bet on a driver for a specific session.

---

## 📚 Core Logic

- **Users** have an account with a balance in virtual money.
- For each **F1 session**, a list of **drivers** is retrieved using the public OpenF1 API.
- The system assigns each driver a **random payout multiplier** (2, 3, or 4) for each session, which is stored in the database (`DriverOdds` table).
- A **user can place one bet per session**, choosing a driver and an amount.
- When a session finishes, an admin can mark the **winning driver**, and:
    - All bets for that session are closed.
    - Winning users receive their reward (`amount * odds`) in their balance.
    - Losing bets are marked as closed without reward.
- The DB doesn't store anything that can be retrieved from the OpenF1 API

---

## 🛠️ Tech Stack

- Java 21
- Spring Boot
- PostgreSQL (Dockerized)
- Spring Data JPA
- OpenFeign (for external API calls)
- Lombok
- Maven

---

## 🚀 Getting Started

### 1. Start PostgreSQL using Docker

```bash
  docker compose up -d
```

### 2. Start the App

```bash
  mvn spring-boot:run
```
The app will be running on **localhost:8080**

---

## 📡 API Endpoints
### 🛣️ Sessions

- GET /sessions (**Retrieve F1 sessions from the OpenF1 API.**)
  - Optional query parameters:
    - country – Filter by country name (e.g., Belgium)
    - sessionType – Filter by session type (e.g., Sprint)
    - year – Filter by year (e.g., 2023)
  - Example: localhost:8080/sessions?country=Belgium&sessionType=Sprint&year=2023


- POST /sessions/close (**Close a specific session with a winning driver**) 🏁
  - Request Body Example
```
{
  "session_key": 9140,
  "winning_driver_number": 1
}
```
      

### 🧍 Users

- POST /users (**Creates a new user with 100.0 balance**)
  - Request Body Example
```
{
  "first_name": "Franco",
  "last_name": "di Napoli",
  "email": "franco.di.napoli@gmail.com",
  "username": "fdinapoli"
}
 ```


### 💰 Bet

- POST /bet (**A User place a bet for a driver in a session**)
  - Request Body Example
```
{
  "user_id": 1,
  "session_key": 9140,
  "driver_number": 1,
  "amount": 50.0
}
```

---

## 📌 Future Improvements

- Custom Exception handling using **@ControllerAdvice** class.
- Data Validation for Request Params/Body.
- Unit and Integration Test Cases.
- Flyway or Liquibase for DB management.
- Extra business logic such as:
  - Only allow to bet on not finished sessions.
  - Ability to edit user/balance.
  - JWT Authentication.
  - Admin panel for managing odds.
  - Leaderboard by winnings.
  - User statistics & history.
- In case we what to add another source of information, alongside OpenF1 API, we only have to create a new feign client class that consumes the new API.