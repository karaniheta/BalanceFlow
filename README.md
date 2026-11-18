# 💳 BalanceFlow

<div align="center">

![Flutter](https://img.shields.io/badge/Flutter-02569B?style=for-the-badge&logo=flutter&logoColor=white)
![Dart](https://img.shields.io/badge/Dart-0175C2?style=for-the-badge&logo=dart&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Java](https://img.shields.io/badge/Java_17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)

**A smart full-stack expense sharing, group debt settlement, and personal balance tracking system.**

[Features](#-features) • [Tech Stack](#-tech-stack) • [Project Structure](#-project-structure) • [Getting Started](#-getting-started) • [Environment Variables](#-environment-variables) • [API Reference](#-api-reference) • [License](#-license)

</div>

---

## 📌 Overview

**BalanceFlow** simplifies how friends, roommates, and teams split expenses, settle debts, and manage transactions. Built as a high-performance system combining a **Flutter** mobile/web application with a **Spring Boot 3** REST backend, PostgreSQL relational database, and Redis cache.

Whether it's splitting a weekend trip bill, managing monthly household groceries, or recording debt settlements, BalanceFlow ensures transparency with accurate balance calculations and real-time updates.

---

## ✨ Features

- **👥 Group Expense Management**: Create groups, invite members, track shared costs, and calculate who owes whom effortlessly.
- **⚖️ Smart Debt Settlement**: Real-time balance calculations with one-click debt settlement logs between members.
- **📊 Interactive Dashboard**: Financial summaries, pending balances, recent activity feeds, and statistics.
- **🔐 Secure Authentication**: JWT (JSON Web Token) token-based authentication with OTP email verification and password reset flows.
- **⚡ Redis-Powered OTP & Caching**: Fast and secure verification code handling via Redis.
- **🏷️ Categories & Transactions**: Categorize spending (Food, Travel, Utilities, etc.) and maintain a clean ledger.
- **🔍 Global Search**: Fast user and group discovery by name or email.
- **📱 Clean Cross-Platform UI**: Built with Flutter and the **BLoC (Business Logic Component)** pattern for responsive and predictable state management.
- **🐳 Docker & Cloud Ready**: Fully containerized with `Dockerfile` and `docker-compose.yml` for PostgreSQL, Redis, and Backend services.

---

## 🛠️ Tech Stack

### Client (Frontend)
- **Framework**: [Flutter](https://flutter.dev/) (Dart)
- **State Management**: [flutter_bloc](https://pub.dev/packages/flutter_bloc)
- **Networking**: `http` REST Client
- **Storage**: `shared_preferences` (Session and JWT token persistence)
- **Platforms**: Android, iOS, Web, Desktop

### Server (Backend)
- **Framework**: [Spring Boot 3](https://spring.io/projects/spring-boot) (Java 17)
- **Security**: Spring Security & `io.jsonwebtoken:jjwt` (0.12.6)
- **Data & Persistence**: Spring Data JPA & Hibernate
- **Database**: [PostgreSQL 17](https://www.postgresql.org/)
- **Cache & OTP Store**: [Redis](https://redis.io/)
- **Email Service**: Spring Boot Mail (SMTP)
- **Utilities**: Project Lombok, `spring-dotenv`

### DevOps & Deployment
- **Containerization**: Docker & Docker Compose
- **Target Deployments**: Render, AWS, Neon Tech (Postgres), Upstash (Redis)

---

## 📂 Project Structure

```text
BalanceFlow/
├── app/
│   └── balanceflow/                  # Flutter Mobile/Web Application
│       ├── lib/
│       │   ├── api.dart              # API endpoints and HTTP interceptor/client
│       │   ├── main.dart             # App entry point
│       │   ├── app/                  # Theme colors, routing, custom UI widgets
│       │   └── modules/              # Feature modules (BLoC pattern)
│       │       ├── auth/             # Login, Register, OTP, Reset Password
│       │       ├── dashboard/        # Dashboard stats and alerts
│       │       ├── groups/           # Group listing, detail, expenses & splits
│       │       ├── home/             # Main navigation & landing screens
│       │       ├── profile/          # User profile and account settings
│       │       └── splash/           # Splash & onboarding screens
│       └── pubspec.yaml              # Flutter dependencies and assets
│
├── balanceflow-backend/              # Spring Boot REST API
│   ├── src/main/java/com/balanceflow/balanceflow_backend/
│   │   ├── activity/                 # User & group activity log
│   │   ├── auth/                     # Authentication & OTP controllers/services
│   │   ├── category/                 # Expense category management
│   │   ├── config/                   # Security, Redis & Bean configurations
│   │   ├── dashboard/                # User dashboard & metrics APIs
│   │   ├── expense/                  # Expense creation, splitting & balances
│   │   ├── group/                    # Group CRUD & member associations
│   │   ├── search/                   # User and group query search
│   │   ├── security/                 # JWT Filters & UserDetailsService
│   │   ├── settlement/               # Payment settlements & history
│   │   ├── transaction/              # Income/expense transactions
│   │   └── user/                     # User management & profile services
│   ├── src/main/resources/           # application.properties & configs
│   ├── docker-compose.yml            # Multi-container orchestration (App, DB, Redis)
│   ├── Dockerfile                    # Multi-stage production container build
│   ├── .env.example                  # Environment variable blueprint
│   └── pom.xml                       # Maven dependencies
│
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

Ensure you have the following installed on your machine:
- **Java Development Kit (JDK 17+)**
- **Apache Maven** (or use the included `./mvnw`)
- **Flutter SDK (3.11+)** & Dart
- **Docker & Docker Compose** (Recommended for local database & Redis)

---

### 1. Backend Setup

#### Step A: Configure Environment Variables
Inside `balanceflow-backend/`, copy `.env.example` to `.env`:
```bash
cd balanceflow-backend
cp .env.example .env
```
Fill in your database, Redis, SMTP, and JWT secret credentials:
```env
DB_URL=jdbc:postgresql://localhost:5432/balanceflow
DB_USERNAME=postgres
DB_PASSWORD=your_password

REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=

MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password

JWT_SECRET=your_super_secret_jwt_key_at_least_256_bits
```

#### Step B: Run with Docker Compose (Recommended)
You can start PostgreSQL, Redis, and the backend application together:
```bash
docker-compose up --build
```

#### Alternative: Run Locally with Maven
If you already have PostgreSQL and Redis running:
```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```
The backend will be available at: `http://localhost:8080`.

---

### 2. Frontend Setup (Flutter)

#### Step A: Install Dependencies
```bash
cd app/balanceflow
flutter pub get
```

#### Step B: Configure Backend API Base URL
Open `app/balanceflow/lib/api.dart` and update `baseUrl`:
```dart
class Api {
  // For local Android Emulator:
  static const String baseUrl = 'http://10.0.2.2:8080';

  // For local iOS Simulator or Web:
  // static const String baseUrl = 'http://localhost:8080';

  // For Production / Deployed Server:
  // static const String baseUrl = 'https://balanceflow-backend.onrender.com';
  ...
}
```

#### Step C: Run the App
```bash
# Launch on connected device / emulator
flutter run

# Or run on Chrome
flutter run -d chrome
```

---

## ⚙️ Environment Variables

| Variable | Description | Example / Default |
| :--- | :--- | :--- |
| `DB_URL` | PostgreSQL JDBC connection URL | `jdbc:postgresql://localhost:5432/balanceflow` |
| `DB_USERNAME` | PostgreSQL database user | `postgres` |
| `DB_PASSWORD` | PostgreSQL database password | `password` |
| `REDIS_HOST` | Redis server hostname | `localhost` / Upstash endpoint |
| `REDIS_PORT` | Redis server port | `6379` |
| `REDIS_PASSWORD`| Redis authentication password | `your_redis_password` |
| `MAIL_HOST` | SMTP server host | `smtp.gmail.com` |
| `MAIL_PORT` | SMTP server port | `587` |
| `MAIL_USERNAME` | Email account for notifications | `you@gmail.com` |
| `MAIL_PASSWORD` | Mail app password | `app-specific-password` |
| `JWT_SECRET` | Secret key for signing JWT tokens | `256-bit secret string` |

---

## 📡 API Reference

Here is a summary of the core API endpoints available on the backend:

### Authentication (`/api/auth`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new user |
| `POST` | `/api/auth/login` | Authenticate user & return JWT token |
| `POST` | `/api/auth/send-otp` | Send email OTP for verification / reset |
| `POST` | `/api/auth/verify-otp` | Verify received OTP code |
| `POST` | `/api/auth/reset-password` | Set new password with verified OTP |

### Groups & Expenses (`/api/groups`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/groups/my-groups` | Retrieve all groups current user belongs to |
| `POST` | `/api/groups` | Create a new group |
| `GET` | `/api/groups/{id}` | Get group details & members |
| `POST` | `/api/groups/{id}/members` | Add a member to group |
| `POST` | `/api/groups/{id}/expenses` | Add a shared group expense |
| `GET` | `/api/groups/{id}/balances` | Get calculated net balances between members |
| `POST` | `/api/groups/{id}/settlements`| Settle balance / record payment between users |

### User & Dashboard (`/api/dashboard`, `/api/users`, `/api/profile`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/dashboard` | Get user financial summary & recent stats |
| `GET` | `/api/profile` | Get current user profile details |
| `PUT` | `/api/users/profile` | Update user profile information |
| `POST` | `/api/users/change-password` | Change account password |
| `GET` | `/api/search/users?q={query}` | Search users by name or email |
| `GET` | `/api/search/groups?q={query}`| Search groups by name |

---

## 🛡️ Security Best Practices

> [!IMPORTANT]
> - Never commit your `.env` file or sensitive credentials to GitHub.
> - Always use environment variables in production.
> - Ensure your `JWT_SECRET` is at least 256 bits long for secure cryptographic signatures.

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!
1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

