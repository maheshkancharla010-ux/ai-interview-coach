# Volume 3 - Frontend Definition

**Project:** AI Interview Coach | **Status:** COMPLETE | **Date:** August 19, 2026

---

## Technology Stack

React 19, Vite 8, React Router 7, Axios, Tailwind CSS v4

## Design Tokens (src/index.css @theme)

- Primary: #2563EB, Hover: #1D4ED8, Navy: #0F172A
- Background: #F8FAFC, Surface: #FFFFFF, Border: #E2E8F0
- Text Primary: #111827, Text Secondary: #6B7280
- Font: Inter (Google Fonts)

## Routing

- / -> PublicLayout -> LandingPage (public)
- /login -> PublicLayout -> LoginPage (public)
- /register -> PublicLayout -> RegisterPage (public)
- /dashboard -> MainLayout + ProtectedRoute -> DashboardPage
- /resumes -> MainLayout + ProtectedRoute -> ResumesPage
- /interviews -> MainLayout + ProtectedRoute -> InterviewsPage
- /interviews/create -> MainLayout + ProtectedRoute -> CreateInterviewPage
- /interviews/:id -> MainLayout + ProtectedRoute -> InterviewSessionPage

## Bug Fixes

### Bug 1 - Wrong interview completion trigger (InterviewSessionPage)
Removed all updateInterviewStatus calls. Backend FeedbackServiceImpl sets status=COMPLETED
when generating interview-level feedback. Frontend re-fetches interview via getInterviewById.
Per-question feedback path never modifies interview status.

### Bug 2 - Wrong resume file type (ResumesPage)
Changed accept from '.pdf,.doc,.docx' to '.pdf' only.
Backend only handles PDF specially; label updated to 'Select PDF file'.

### Bug 3 - Field-level validation errors not propagated (AuthContext)
login/register now return {success, message, errors} where errors is the backend
400 response data Map<String,String>. LoginPage and RegisterPage display field errors inline.

## Interview Session UX

One question per screen (currentIndex state). Progress dots: filled=answered, outline=unanswered.
Previous/Next navigation. Submit Answer auto-advances to next question.
Generate Feedback shown only when all answered and no feedback yet.
Summary card shown when feedback exists.

## Files Created
- src/layouts/PublicLayout.jsx
- src/pages/LandingPage.jsx
- docs/VOLUME-3-FRONTEND-DEFINITION.md

## Files Modified
- src/index.css, src/routes/index.jsx, src/layouts/MainLayout.jsx
- src/context/AuthContext.jsx (Bug 3)
- src/components/Button.jsx, Card.jsx, Input.jsx, EmptyState.jsx, Loader.jsx, ErrorMessage.jsx
- src/pages/LoginPage.jsx, RegisterPage.jsx (Bug 3 + Figma split layout)
- src/pages/DashboardPage.jsx, ResumesPage.jsx (Bug 2), InterviewsPage.jsx
- src/pages/CreateInterviewPage.jsx, InterviewSessionPage.jsx (Bug 1 + one-at-a-time UX)

## Build Result

npm run build -> SUCCESS (Vite 8, rolldown bundler)
