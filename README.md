# 📊 Transaction-API

The `transaction-api` is a RESTful API built with Kotlin and Spring Boot that allows you to manage financial transactions and retrieve real-time statistics based on them. It's designed with performance, scalability, and clean code practices in mind.

## 🚀 Features

- ✅ Register new financial transactions
- ❌ Delete all existing transactions
- 📈 Get real-time statistics:
    - Sum of transactions
    - Average transaction value
    - Minimum transaction value
    - Maximum transaction value
    - Count of transactions
- ⏱️ Supports transactions within a configurable time window (default: last 60 seconds)

## 🛠 Tech Stack

- **Kotlin**
- **Spring Boot 3.4+**
- **PostgreSQL 16**
- **JPA + Hibernate**
- **Flyway** for database versioning
- **JUnit / Mockito** for unit testing
- **Docker** (optional, for running PostgreSQL locally)

## 📂 Project Structure

```text
src/
 └── main/
     ├── kotlin/
     │   └── com/transactionstat/transactionstat/
     │       ├── config/
     │       ├── controller/
     │       ├── dto/
     │       ├── entity/
     │       ├── repository/
     │       ├── service/
     │       └── TransactionApiApplication.kt
     └── resources/
         ├── application.properties
         └── db/migration/
 ```

## ⚙️ Getting Started

### Prerequisites

- JDK 17+
- Maven 3.9+
- PostgreSQL 16+
- Docker (optional)

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/transaction-api.git
cd transaction-api
```

### 2. Update 'application.properties'

```
spring.datasource.url=jdbc:postgresql://localhost:5432/transactiondb
spring.datasource.username=your_user
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
```

### 3. Add this dependency to support PostgreSQL (16.3) + Flyway 10+

```
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-database-postgresql</artifactId>
</dependency>
```

### 4. Build and run the app

```
./mvnw spring-boot:run
```

## 👨‍💻 Author

Made with ❤️ by @RobsonOliveiraSouza
Feel free to contribute or open an issue!