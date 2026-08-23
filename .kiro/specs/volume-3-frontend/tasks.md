# Implementation Plan: AI Interview Coach — Volume 3 Frontend Definition & Complete Implementation

## Overview

This plan implements the full Volume 3 frontend work in discrete, incremental steps. Work is ordered so that the token foundation is laid first, then the layout and routing shell, then shared components, then individual pages (with their bug fixes), and finally the documentation artifact. Each step builds on the previous and integrates immediately — no orphaned code.

Stack: React 19 + Vite 8 + Tailwind CSS v4 + Axios + React Router 7. No new npm packages.

---

## Tasks

- [ ] 1. Establish design tokens in `index.css`
  - [ ] 1.1 Rewrite `src/index.css` with Google Fonts import, `@import "tailwindcss"`, and `@theme {}` block
    - Replace `@tailwind base/components/utilities` with `@import "tailwindcss"`
    - Add `@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap')` before the Tailwind import
    - Declare `@theme {}` with: `--color-primary: #2563EB`, `--color-primary-hover: #1D4ED8`, `--color-navy: #0F172A`, `--color-bg: #F8FAFC`, `--color-surface: #FFFFFF`, `--color-border: #E2E8F0`, `--color-text-primary: #111827`, `--color-text-secondary: #6B7280`, `--font-sans: 'Inter', system-ui, sans-serif`
    - Add `body { margin: 0; font-family: var(--font-sans); background-color: var(--color-bg); }`
    - Do NOT modify `tailwind.config.js`
    - _Requirements: 1.1, 1.2, 1.3, 1.4_

- [ ] 2. Create `PublicLayout` and update `MainLayout`
  - [ ] 2.1 Create `src/layouts/PublicLayout.jsx`
    - Render a `<header>` with `bg-surface border-b border-border`
    - Show "AI Interview Coach" as a `<Link to="/">` on the left
    - When `useAuth().isAuthenticated` is false, render "Sign In" (`/login`) and "Get Started" (`/register`) links on the right
    - When `useAuth().isAuthenticated` is true, render a "Go to Dashboard" link (`/dashboard`) instead
    - Render `<Outlet />` beneath the header inside a `bg-bg` wrapper
    - _Requirements: 2.1, 2.2, 2.3, 2.4, 2.5_

  - [ ] 2.2 Redesign `src/layouts/MainLayout.jsx`
    - Apply `bg-navy text-white` to the `<nav>` element
    - Show "AI Interview Coach" logo/brand link on the left
    - When authenticated: show "Dashboard" (`/dashboard`), "Resumes" (`/resumes`), "Interviews" (`/interviews`) nav links, user `fullName`, and a "Sign Out" button that calls `logout()` then navigates to `/login`
    - When unauthenticated: render a "Sign In" link only, no nav links
    - Content area: `bg-bg` with `max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8`
    - Render `<Outlet />` inside the content area
    - _Requirements: 5.1, 5.2, 5.3, 5.4, 5.5, 5.6_

- [ ] 3. Fix `AuthContext` field-error propagation
  - [ ] 3.1 Update `src/context/AuthContext.jsx` `login` function
    - On success: return `{ success: true }` (no errors key)
    - On Axios error: extract `error.response?.data?.errors` and return `{ success: false, message: ..., errors: errorsMap || null }`
    - On non-Axios catch: return `{ success: false, message: '...' }` without errors key
    - _Requirements: 8.1, 8.3, 8.5_

  - [ ] 3.2 Update `src/context/AuthContext.jsx` `register` function
    - Mirror the same logic as the `login` fix: propagate `errors` map from both success-path failure bodies and Axios error response bodies
    - On success: return `{ success: true }` (no errors key)
    - _Requirements: 8.2, 8.4, 8.5_

  - [ ]* 3.3 Write property tests for AuthContext errors passthrough
    - **Property 6: AuthContext login errors passthrough** — mock Axios to return an error response with an `errors` object; assert `login()` returns `{ success: false, errors: <that map> }`
    - **Property 7: AuthContext register errors passthrough** — same shape for `register()`
    - **Property 8: AuthContext no spurious errors key** — mock a successful response with no `errors` field; assert the returned object has no truthy `errors` key
    - **Validates: Requirements 8.1, 8.2, 8.3, 8.4, 8.5**

- [ ] 4. Update shared components to use design tokens
  - [ ] 4.1 Redesign `src/components/Button.jsx`
    - Add `size` prop (`'sm' | 'md' | 'lg'`, default `'md'`)
    - Size classes: `sm` → `px-3 py-1.5 text-xs`; `md` → `px-4 py-2 text-sm`; `lg` → `px-6 py-3 text-base`
    - Variant token mapping: `primary` → `bg-primary hover:bg-primary-hover text-white focus:ring-primary`; `secondary` → `bg-gray-200 text-gray-900 hover:bg-gray-300`; `danger` → `bg-red-600 text-white hover:bg-red-700 focus:ring-red-500`; `outline` → `border border-border text-text-primary hover:bg-bg focus:ring-primary`
    - _Requirements: 14.1, 14.2, 14.3_

  - [ ] 4.2 Redesign `src/components/Input.jsx`
    - Add `fieldError` prop (field-level error string from backend)
    - Display priority: if `error` is set show `error`; else if `fieldError` is set show `fieldError`; both apply `border-red-500`
    - Without any error, apply `border-border`
    - Error text rendered in `text-red-600 text-sm mt-1` below the input
    - _Requirements: 14.5, 14.6_

  - [ ] 4.3 Redesign `src/components/Card.jsx`
    - Apply `bg-surface border border-border shadow-sm rounded-lg` as base classes
    - _Requirements: 14.4_

  - [ ] 4.4 Redesign `src/components/EmptyState.jsx`
    - CTA button updated to use `bg-primary hover:bg-primary-hover text-white`
    - _Requirements: 14.9_

  - [ ] 4.5 Redesign `src/components/Loader.jsx`
    - Spinner border updated to `border-primary`; centered layout
    - _Requirements: 14.7_

  - [ ] 4.6 Redesign `src/components/ErrorMessage.jsx`
    - Styled error container using token-aligned red palette (e.g., `bg-red-50 border border-red-200 text-red-700 rounded-md p-4`)
    - Only renders when `message` is non-empty
    - _Requirements: 14.8_

  - [ ]* 4.7 Write property tests for shared components
    - **Property 20: Input field error display** — render `Input` with `error` and `fieldError` props; assert error text appears and `border-red-500` class is applied
    - **Property 21: ErrorMessage renders non-empty messages** — render `ErrorMessage` with a non-empty string; assert it is visible in the output
    - **Property 22: EmptyState CTA button renders with provided text** — render `EmptyState` with `actionText`; assert button label matches
    - **Validates: Requirements 14.5, 14.8, 14.9**

- [ ] 5. Update route configuration
  - [ ] 5.1 Rewrite `src/routes/index.jsx`
    - Public routes tree: `PublicLayout` parent with child routes `/` → `LandingPage`, `/login` → `LoginPage`, `/register` → `RegisterPage`
    - Authenticated routes tree: `MainLayout` parent with `ProtectedRoute` wrapper, children: `/dashboard`, `/resumes`, `/interviews`, `/interviews/create`, `/interviews/:id`
    - Remove any existing redirect from `/` to `/login`
    - Import `LandingPage` from `../pages/LandingPage`
    - Import `PublicLayout` from `../layouts/PublicLayout`
    - _Requirements: 3.1, 3.2, 3.3, 3.4, 3.5_

- [ ] 6. Create `LandingPage`
  - [ ] 6.1 Create `src/pages/LandingPage.jsx` with hero and trust-bar sections
    - Hero: `bg-blue-50` full-width section with large headline (e.g., "Ace Your Next Interview"), subheadline, "Start Practicing Free" `<Link to="/register">` (styled `bg-primary`), and "Sign In" `<Link to="/login">` (styled `outline` variant)
    - Trust bar: 3 checkmark items in a horizontal row (e.g., "AI-powered questions", "Real-time feedback", "Free to start")
    - _Requirements: 4.1, 4.2, 4.7_

  - [ ] 6.2 Add "How It Works" and Features sections to `LandingPage`
    - "How It Works": 4 numbered steps in a grid (Upload Resume → Generate Questions → Practice Answering → Review Feedback)
    - Features: 2×2 card grid showing AI Questions, Real-time Feedback, Resume-tailored Questions, Progress Tracking
    - _Requirements: 4.3, 4.4, 4.7_

  - [ ] 6.3 Add Interview Preview and CTA Footer sections to `LandingPage`
    - Interview Preview: split panel — left side shows a mock question card, right side shows a mock answer text area
    - CTA Footer: `bg-navy text-white`, headline copy, single `<Link to="/register">` "Get Started Free" button styled with `bg-primary`
    - _Requirements: 4.5, 4.6, 4.7_

- [ ] 7. Checkpoint — Verify routing and token foundation
  - Ensure all routes resolve without errors (`/`, `/login`, `/register`, `/dashboard`)
  - Ensure design tokens are visible in the browser (Inter font loaded, navy navbar, white surface cards)
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 8. Redesign `LoginPage` and `RegisterPage`
  - [ ] 8.1 Redesign `src/pages/LoginPage.jsx` with split layout and field errors
    - Left column: `bg-navy text-white` (50% width), showing logo/brand name, a short tagline, and a decorative quote
    - Right column: `bg-surface` with the login form (email `Input`, password `Input` with show/hide toggle, "Sign In" `Button variant="primary"`)
    - State: `{ email, password, showPassword, formError, fieldErrors, loading }`
    - On submit: call `login()`; if `result.errors` map returned, set `fieldErrors`; if `result.message`, set `formError`
    - Render `formError` as `<ErrorMessage>` above the submit button
    - Pass `fieldErrors.email` to email `Input` as `fieldError` prop; pass `fieldErrors.password` to password `Input` as `fieldError` prop
    - Link to `/register` at the bottom of the right column
    - _Requirements: 6.1, 6.2, 6.3, 6.4, 6.5, 6.6_

  - [ ] 8.2 Redesign `src/pages/RegisterPage.jsx` with split layout, confirm-password, and field errors
    - Same split layout as `LoginPage`
    - Right column: Full Name, Email, Password, Confirm Password fields
    - Client-side check: if `password !== confirmPassword`, set `fieldErrors.confirmPassword` and return before API call
    - On API failure: map `result.errors` to `fieldErrors`; map `result.message` to `formError`
    - Pass field errors to each `Input` via `fieldError` prop
    - Link to `/login` at the bottom
    - _Requirements: 7.1, 7.2, 7.3, 7.4, 7.5, 7.6_

  - [ ]* 8.3 Write property tests for LoginPage and RegisterPage field errors
    - **Property 2: Login form-level error display** — mock `login()` returning `{ success: false, message: 'Invalid credentials' }`; assert that message text appears in the rendered output
    - **Property 3: Login field-level error display** — mock `login()` returning `{ success: false, errors: { email: 'Not found' } }`; assert error appears below the email input
    - **Property 4: Password confirmation mismatch rejection** — simulate form submit with mismatched passwords; assert fieldError appears on Confirm Password and no API call is made
    - **Property 5: Register field-level error display** — mock `register()` returning `{ success: false, errors: { email: 'Already taken' } }`; assert error appears below the email input
    - **Validates: Requirements 6.3, 6.4, 7.3, 7.4**

- [ ] 9. Redesign `DashboardPage`
  - [ ] 9.1 Redesign `src/pages/DashboardPage.jsx`
    - Stat cards using `Card` component (`bg-surface border-border`) for "Total Interviews" and "Resumes" counts
    - "Recent Interviews" section: render at most 5 entries; use `EmptyState` when none exist
    - Resumes section: use `EmptyState` when no resumes exist
    - Quick Actions row: "New Interview" button/link → `/interviews/create`, "Upload Resume" → `/resumes`
    - Apply `text-primary` to action links and CTA elements
    - _Requirements: 9.1, 9.2, 9.3, 9.4, 9.5_

  - [ ]* 9.2 Write property test for DashboardPage recent interviews cap
    - **Property 9: Dashboard shows at most five recent interviews** — render `DashboardPage` mocked with 8 interviews; assert exactly 5 entries are rendered in the Recent Interviews section
    - **Validates: Requirements 9.2**

- [ ] 10. Fix and redesign `ResumesPage`
  - [ ] 10.1 Fix and redesign `src/pages/ResumesPage.jsx`
    - **Bug fix**: change `accept=".pdf,.doc,.docx"` to `accept=".pdf"` on the file input
    - Update upload label text to "Select PDF File"
    - Display uploaded resumes in a list using `Card` components (`bg-surface border-border`)
    - Use `EmptyState` when no resumes exist
    - _Requirements: 10.1, 10.2, 10.3, 10.4_

- [ ] 11. Redesign `InterviewsPage`
  - [ ] 11.1 Redesign `src/pages/InterviewsPage.jsx`
    - Page header with "My Interviews" title and "Create Interview" `Button variant="primary"` (`text-primary` or navigates to `/interviews/create`)
    - `EmptyState` when no interviews exist
    - Each interview rendered as a card with: `jobTitle`, status badge (green/yellow/gray for COMPLETED/IN_PROGRESS/PENDING), formatted `createdAt`, "View" link → `/interviews/:id`, "Delete" button
    - _Requirements: 11.1, 11.2, 11.3, 11.4_

  - [ ]* 11.2 Write property tests for InterviewsPage
    - **Property 10: Interview list renders all required fields per row** — render `InterviewsPage` mocked with one interview; assert `jobTitle`, status, date, "View" link, and "Delete" button are all present
    - **Property 11: Status badge color correctness** — render cards with COMPLETED, IN_PROGRESS, and PENDING statuses; assert correct Tailwind color classes (green, yellow, gray) for each
    - **Validates: Requirements 11.3, 11.4**

- [ ] 12. Redesign `CreateInterviewPage`
  - [ ] 12.1 Redesign `src/pages/CreateInterviewPage.jsx`
    - Centered form card using `Card` component
    - Fields: Job Title `Input` (required), Job Description `<textarea>` (optional), Resume `<select>` (optional)
    - When no resumes available, display an informational note: "Upload a resume to get personalized questions"
    - On successful submission, navigate to `/interviews/:id`
    - Apply `bg-primary` to submit button, `border-border` to form field borders
    - _Requirements: 12.1, 12.2, 12.3, 12.4_

- [ ] 13. Redesign and fix `InterviewSessionPage`
  - [ ] 13.1 Implement one-at-a-time question layout in `src/pages/InterviewSessionPage.jsx`
    - Add `currentQuestionIndex` state (default `0`)
    - Derive `currentQuestion = questions[currentQuestionIndex]`
    - Render only the current question's text, answer textarea, and existing per-question feedback (if any)
    - Add "Previous" button: `setCurrentQuestionIndex(i => Math.max(i - 1, 0))`; disable when `currentQuestionIndex === 0`
    - Add "Next" button: `setCurrentQuestionIndex(i => Math.min(i + 1, questions.length - 1))`; disable when `currentQuestionIndex === questions.length - 1`
    - _Requirements: 13.1, 13.4, 13.5_

  - [ ] 13.2 Add "Question X of Y" label and `ProgressDot` indicators
    - Render `Question {currentQuestionIndex + 1} of {questions.length}` label
    - Render a row of dots: filled `bg-primary` for the active question, `bg-primary opacity-60` for answered questions, `border-2 border-border bg-transparent` for unanswered questions
    - _Requirements: 13.2, 13.3_

  - [ ] 13.3 Fix `handleGenerateFeedback` — remove `updateInterviewStatus` calls
    - When called with a `questionId`: call only `feedbackApi.generateFeedbackForQuestion(questionId)` then refresh feedback list; never call `updateInterviewStatus`
    - When called without `questionId` (interview-level): call `feedbackApi.generateFeedbackForInterview(id)`, refresh feedback list, then call `interviewApi.getInterviewById(id)` and set `setInterview(freshInterview.data)`; never call `updateInterviewStatus`
    - _Requirements: 13.6, 13.7, 13.8_

  - [ ] 13.4 Add "Generate Feedback" button visibility logic
    - Derive `allAnswered = questions.length > 0 && questions.every(q => q.answerText)`
    - Derive `hasFeedback = feedback.length > 0`
    - Show "Generate Feedback" `Button variant="primary"` only when `allAnswered && !hasFeedback`
    - Show feedback score and text on the current question card when feedback exists for that question
    - _Requirements: 13.9, 13.10_

  - [ ]* 13.5 Write property tests for InterviewSessionPage
    - **Property 12: InterviewSessionPage shows one question at a time** — render with 3 questions, index=1; assert question[1].text is rendered and question[0].text and question[2].text are not
    - **Property 13: Question counter label correctness** — render with 3 questions at index 1; assert "Question 2 of 3" label is present
    - **Property 14: ProgressDot state matches question answered state** — render with mixed answered/unanswered questions; assert filled dots for answered and outline dots for unanswered
    - **Property 15: Question navigation bounds clamping** — simulate clicking Next at last question; assert index stays at last; simulate clicking Previous at first question; assert index stays at 0
    - **Property 16: Interview-level feedback generation never calls updateInterviewStatus** — spy on `interviewApi.updateInterviewStatus`; call `handleGenerateFeedback()` without args; assert spy was never called
    - **Property 17: Per-question feedback generation never calls updateInterviewStatus** — spy on `interviewApi.updateInterviewStatus`; call `handleGenerateFeedback(questionId)`; assert spy was never called
    - **Property 18: Interview state after feedback reflects backend data** — mock `getInterviewById` return; after interview-level feedback generation, assert local `interview` state equals the mocked return value
    - **Property 19: Generate Feedback button appears only when all answered and no feedback** — render with all answers filled and empty feedback; assert button visible; render with feedback present; assert button hidden
    - **Validates: Requirements 13.1, 13.2, 13.3, 13.4, 13.5, 13.6, 13.7, 13.8, 13.9**

- [ ] 14. Checkpoint — Full integration pass
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 15. Create documentation artifact
  - [ ] 15.1 Create `docs/VOLUME-3-FRONTEND-DEFINITION.md`
    - Document design token definitions (all 9 tokens + font)
    - Document the `PublicLayout` / `MainLayout` routing split with route table
    - Document `LandingPage` section inventory (6 sections)
    - Document `InterviewSessionPage` one-at-a-time UX model
    - Document the 3 bug fixes: `updateInterviewStatus` removal, PDF-only accept fix, field-error propagation in `AuthContext`
    - Include complete inventory of created files and modified files
    - _Requirements: 15.1_

- [ ] 16. Build verification
  - Run `npm run build` (or `vite build`) inside the `frontend` directory and confirm zero TypeScript/ESLint errors and a successful bundle output
  - Fix any import errors, missing exports, or token-class resolution issues revealed by the build

---

## Notes

- Tasks marked with `*` are optional and can be skipped for a faster MVP
- Each task references specific requirements for traceability
- Checkpoints at steps 7 and 14 ensure incremental validation
- Property tests validate universal correctness properties; unit tests validate specific examples and edge cases
- The design tokens in `index.css` must be established (task 1.1) before any component or page work, as all other tasks depend on the token class names
- The `AuthContext` fix (task 3) must be complete before redesigning `LoginPage` and `RegisterPage` (task 8), since the pages consume `result.errors`
- No new npm packages are permitted; use only the existing stack

---

## Task Dependency Graph

```json
{
  "waves": [
    { "id": 0, "tasks": ["1.1"] },
    { "id": 1, "tasks": ["2.1", "2.2", "3.1", "3.2"] },
    { "id": 2, "tasks": ["3.3", "4.1", "4.2", "4.3", "4.4", "4.5", "4.6", "5.1"] },
    { "id": 3, "tasks": ["4.7", "6.1"] },
    { "id": 4, "tasks": ["6.2", "6.3", "8.1", "8.2", "9.1", "10.1", "11.1", "12.1"] },
    { "id": 5, "tasks": ["8.3", "9.2", "11.2", "13.1"] },
    { "id": 6, "tasks": ["13.2", "13.3"] },
    { "id": 7, "tasks": ["13.4"] },
    { "id": 8, "tasks": ["13.5", "15.1"] },
    { "id": 9, "tasks": ["16"] }
  ]
}
```
