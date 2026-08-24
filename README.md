# AI Interview Coach

Full-stack interview preparation platform with AI-powered question generation and feedback.

## Tech Stack

### Backend
- Java 25
- Spring Boot 3.5.16
- Spring Security (JWT, stateless)
- Spring Data JPA (Hibernate)
- PostgreSQL 18
- Swagger/OpenAPI (springdoc)
- Maven
- Lombok

### Frontend
- React 18
- Vite
- React Router
- Axios
- Tailwind CSS
- PostCSS

## Project Structure

```
ai-interview-coach/
├── backend/
│   ├── src/main/java/com/mahesh/ai/backend/
│   │   ├── common/          # ApiResponse wrapper
│   │   ├── config/          # Security, Password, OpenAPI, CORS
│   │   ├── controller/      # REST controllers
│   │   ├── dto/             # Request/Response DTOs
│   │   ├── entity/          # JPA entities
│   │   ├── enums/           # Role, InterviewStatus
│   │   ├── exception/       # Custom exceptions + handler
│   │   ├── mapper/          # Entity ↔ DTO mappers
│   │   ├── repository/      # JPA repositories
│   │   ├── security/        # JWT, filters, UserPrincipal
│   │   ├── service/         # Service interfaces + impl
│   │   └── util/            # SecurityUtil
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   ├── application-dev.yml
│   │   └── application-prod.yml
│   └── src/test/java/       # Integration + unit tests
├── frontend/
│   ├── src/
│   │   ├── api/             # API layer (auth, resume, interview, question, feedback)
│   │   ├── components/      # Reusable components (Button, Input, Card, etc.)
│   │   ├── context/         # React Context (AuthContext)
│   │   ├── layouts/         # Layout components (MainLayout)
│   │   ├── pages/           # Page components (Login, Dashboard, etc.)
│   │   ├── routes/          # React Router configuration
│   │   ├── utils/           # Utilities (axios instance)
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── public/
│   ├── package.json
│   ├── vite.config.js
│   └── tailwind.config.js
└── docs/                    # Completion reports and documentation
```

## Prerequisites

- Java 21
- Maven
- PostgreSQL 18
- Node.js 18+
- npm

## Setup

1. Clone the repository

2. Create a PostgreSQL database:

```sql
CREATE DATABASE ai_interview_coach;
```

3. Configure database credentials in `backend/src/main/resources/application-dev.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ai_interview_coach
    username: postgres
    password: your_password
```

4. (Optional) Set JWT secret via environment variable:

```
JWT_SECRET=your-256-bit-secret-key-here
```

## Run the Project

### Backend
```bash
cd backend
mvn spring-boot:run
```

The backend starts at: http://localhost:8080

Swagger UI: http://localhost:8080/swagger-ui/index.html

### Frontend
```bash
cd frontend
npm install
npm run dev
```

The frontend starts at: http://localhost:5173

### Frontend Environment Configuration
Create a `.env` file in the frontend directory:
```
VITE_API_BASE_URL=http://localhost:8080/api
```

See `.env.example` for reference.

## Run Tests

### Backend
```bash
cd backend
mvn clean test
```

Requires PostgreSQL running with the dev database configured.

### Frontend
```bash
cd frontend
npm run build
```

Builds the frontend for production.

## API Overview

### Public Endpoints
- `POST /api/auth/register` — Register and receive JWT
- `POST /api/auth/login` — Login and receive JWT
- `/swagger-ui/**` — Swagger documentation

### Protected Endpoints (require `Authorization: Bearer <token>`)

| Module | Endpoints |
|--------|-----------|
| Users | CRUD at `/api/users` (ADMIN or owner) |
| Resumes | Upload, list, get, delete at `/api/resumes` |
| Interviews | CRUD at `/api/interviews` |
| Questions | Generate, list, answer at `/api/interviews/{id}/questions`, `/api/questions/{id}` |
| Feedback | Generate, list at `/api/interviews/{id}/feedback`, `/api/questions/{id}/feedback` |

See [Volume 2 Completion Report](docs/VOLUME_2_COMPLETION.md) for full API documentation.

## Authentication

All protected endpoints require a JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

Obtain a token by registering or logging in via `/api/auth/register` or `/api/auth/login`.

## Environment Variables (Production)

### Backend
| Variable | Description |
|----------|-------------|
| `JWT_SECRET` | JWT signing secret (required in prod) |
| `DATABASE_URL` | PostgreSQL JDBC URL |
| `DATABASE_USERNAME` | Database username |
| `DATABASE_PASSWORD` | Database password |

### Frontend
| Variable | Description |
|----------|-------------|
| `VITE_API_BASE_URL` | Backend API base URL (e.g., https://api.example.com/api) |

## Frontend Features

- **User Authentication**: Registration, login, logout with JWT token management
- **Dashboard**: Overview of interviews, resumes, and quick actions
- **Resume Management**: Upload, view, download, and delete resumes
- **Interview Management**: Create, view, and delete interviews
- **Interview Sessions**: Generate questions, submit answers, view feedback
- **Real-time Feedback**: AI-powered feedback generation with scoring
- **Responsive Design**: Mobile-friendly interface using Tailwind CSS
- **Protected Routes**: Authentication-based route protection
- **Error Handling**: Comprehensive error states and user-friendly messages
- **Loading States**: Visual feedback for async operations

## Development Workflow

1. **Backend Development**: 
   - Make changes to Spring Boot code
   - Run `mvn clean test` to verify
   - Restart backend server

2. **Frontend Development**:
   - Make changes to React code
   - Frontend dev server hot-reloads automatically
   - Run `npm run build` to verify production build

3. **Integration Testing**:
   - Ensure both backend and frontend are running
   - Test user flows end-to-end
   - Verify API communication

## Documentation

- [AI Development Rules](AI-DEVELOPMENT-RULES.md) - Development guidelines and best practices
- [Volume 2 Completion Report](docs/VOLUME_2_COMPLETION.md) - Backend implementation details
- [Volume 3 Completion Report](docs/VOLUME_3_COMPLETION.md) - Frontend implementation details

## Current Progress

### Volume 1 - System Design & Architecture
- Complete
- See project documentation for architecture details

### Volume 2 - Backend Development
- Project Setup
- Spring Boot Configuration
- PostgreSQL Integration
- Spring Security + JWT Authentication
- User CRUD with Role-based Authorization
- Resume Upload/Management with Ownership
- Interview CRUD with Ownership
- Question Generation (Mock AI) + Answer Submission
- Feedback Generation (Mock AI) + Scoring
- Swagger/OpenAPI Documentation
- 33 Automated Tests (all passing)
- **Status:** Complete

See [docs/VOLUME_2_COMPLETION.md](docs/VOLUME_2_COMPLETION.md) for the full completion report.

### Volume 3 - Frontend Development
- React + Vite Setup
- Tailwind CSS Integration
- React Router Configuration
- Authentication (Login, Register, Logout)
- JWT Token Management
- Protected Routes
- Dashboard Page
- Resume Management (Upload, List, Download, Delete)
- Interview Management (Create, List, Delete)
- Interview Session (Question Generation, Answer Submission)
- Feedback Display
- Responsive Design
- API Integration with Backend
- CORS Configuration
- **Status:** Complete

See [docs/VOLUME_3_COMPLETION.md](docs/VOLUME_3_COMPLETION.md) for the full completion report.

### Volume 4 - AI Integration
- **Status:** Pending
- Replace MockAiServiceImpl with real AI provider
- Implement actual AI-powered question generation
- Implement actual AI-powered feedback generation

### Volume 5 - Testing, Deployment & Final Integration
- **Status:** Pending
