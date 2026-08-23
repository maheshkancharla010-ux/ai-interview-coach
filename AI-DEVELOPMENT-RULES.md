# AI Development Rules

## Project Overview
AI Interview Coach - An MVP platform for interview preparation with AI-powered question generation and feedback.

## Technology Stack

### Backend
- Java 21
- Spring Boot 3.5.16
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL 18
- Maven

### Frontend
- React 18
- Vite
- React Router
- Axios
- Tailwind CSS (@tailwindcss/postcss)

## Development Guidelines

### Backend Development
1. Follow Spring Boot best practices
2. Use JWT for stateless authentication
3. Implement proper error handling with GlobalExceptionHandler
4. Use ApiResponse wrapper for consistent API responses
5. Maintain ownership-based authorization at service layer
6. Write integration tests for new features
7. Run `mvn clean test` before committing

### Frontend Development
1. Use React functional components with hooks
2. Implement proper loading states for async operations
3. Handle errors gracefully with user-friendly messages
4. Use centralized API layer (api/ folder)
5. Implement protected routes for authenticated pages
6. Use Tailwind CSS for styling
7. Run `npm run build` before committing

### API Integration
1. Backend runs on http://localhost:8080
2. Frontend runs on http://localhost:5173
3. All protected endpoints require JWT in Authorization header
4. CORS is configured for localhost:5173 and localhost:3000
5. API base URL is configured via VITE_API_BASE_URL environment variable

### Git Workflow
1. Create feature branches for new work
2. Write meaningful commit messages
3. Never commit secrets or sensitive data
4. Test thoroughly before pushing
5. Run backend tests with `mvn clean test`
6. Run frontend build with `npm run build`

### Code Quality
1. Follow existing code patterns and conventions
2. Keep components focused and reusable
3. Add comments only when necessary
4. Use meaningful variable and function names
5. Avoid unnecessary complexity

## Testing

### Backend Testing
- Integration tests for all controllers
- Unit tests for business logic
- Mock AI service tests
- Run with: `mvn clean test`

### Frontend Testing
- Manual testing in browser
- Verify all user flows work end-to-end
- Test error states and loading states
- Build verification: `npm run build`

## Deployment Notes

### Environment Variables
- `JWT_SECRET` - JWT signing key (required in production)
- `DATABASE_URL` - PostgreSQL connection string
- `DATABASE_USERNAME` - Database username
- `DATABASE_PASSWORD` - Database password

### Frontend Configuration
- `VITE_API_BASE_URL` - Backend API base URL

## Known Limitations
- PDF resume parsing uses simulated extraction (MVP approach)
- Mock AI service provides deterministic responses
- No refresh token support (JWT expires in 24 hours)
- No email verification on registration
