# Requirements Document

## Introduction

This document defines the requirements for the AI Interview Coach Volume 3 frontend definition and complete implementation. The work covers three categories:

1. **New artifacts** — A public `LandingPage` component, a `PublicLayout` layout wrapper, and a `VOLUME-3-FRONTEND-DEFINITION.md` documentation file.
2. **Redesign and enhancement** — Full visual and structural overhaul of all existing pages and shared components using a defined Figma token set (Inter font, `#2563EB` primary palette, `#F8FAFC` background) with Tailwind CSS v4 CSS-first `@theme {}` tokens.
3. **Bug fixes** — Three targeted defects: incorrect `updateInterviewStatus` call on per-question feedback generation in `InterviewSessionPage`, wrong file-type filter in `ResumesPage`, and field-level validation error propagation missing from `AuthContext` and the login/register pages.

The stack is React 19 + Vite 8 + Tailwind CSS v4 (`@tailwindcss/postcss`) + Axios + React Router 7. No new npm packages may be added.

---

## Glossary

- **App**: The AI Interview Coach single-page React application running in a browser.
- **AuthContext**: The React context module (`src/context/AuthContext.jsx`) that manages authentication state, exposes `login` and `register` functions, and propagates backend response data to consumers.
- **PublicLayout**: A React layout component (`src/layouts/PublicLayout.jsx`) that renders a public-facing navigation header and an `<Outlet />` for public routes, without the authenticated-app navigation bar.
- **MainLayout**: The existing React layout component (`src/layouts/MainLayout.jsx`) that renders the authenticated-app navigation bar and an `<Outlet />` for protected routes.
- **LandingPage**: The public home page rendered at the `/` route, containing a hero section, a "How It Works" section, a features section, an interview-preview section, and a CTA footer.
- **InterviewSessionPage**: The page rendered at `/interviews/:id` that presents interview questions one at a time, collects answers, and displays AI-generated feedback.
- **DesignToken**: A CSS custom property defined inside a `@theme {}` block in `index.css` using the Tailwind CSS v4 CSS-first configuration approach.
- **FieldError**: A validation error keyed to a specific form field returned by the backend API in the `errors` map of the response body, as opposed to a top-level `message` string.
- **EARS**: Easy Approach to Requirements Syntax — the pattern set used to write verifiable requirements.
- **SplitLayout**: A two-column page layout where the left column uses the dark-navy background (`#0F172A`) with branding copy and the right column uses the white surface (`#FFFFFF`) with the form.
- **ProgressDot**: A visual indicator (filled or unfilled circle) in `InterviewSessionPage` representing each question's completion state.

---

## Requirements

### Requirement 1 — Design Token Setup

**User Story:** As a developer, I want all brand colors, the Inter typeface, and spacing tokens declared in `index.css` via Tailwind v4's `@theme {}` block, so that every component can reference them as Tailwind utility classes without a JavaScript config file.

#### Acceptance Criteria

1. THE App SHALL import the Inter font from Google Fonts inside `src/index.css` using a CSS `@import` statement before any Tailwind directives.
2. THE App SHALL declare a `@theme {}` block in `src/index.css` that defines at minimum the following CSS custom properties: `--color-primary` (`#2563EB`), `--color-primary-hover` (`#1D4ED8`), `--color-navy` (`#0F172A`), `--color-bg` (`#F8FAFC`), `--color-surface` (`#FFFFFF`), `--color-border` (`#E2E8F0`), `--color-text-primary` (`#111827`), `--color-text-secondary` (`#6B7280`), and `--font-sans` (`Inter, system-ui, sans-serif`).
3. THE App SHALL replace the `@tailwind base;`, `@tailwind components;`, and `@tailwind utilities;` directives with a single `@import "tailwindcss";` directive compatible with Tailwind CSS v4.
4. WHEN a component uses a Tailwind class derived from a token (e.g., `bg-primary`, `text-text-secondary`), THE App SHALL resolve that class to the value declared in the `@theme {}` block without requiring changes to `tailwind.config.js`.

---

### Requirement 2 — PublicLayout Component

**User Story:** As a visitor, I want public pages (landing, login, register) to have a consistent header with the brand name and login/register links but without the authenticated navigation links, so that the public experience is clean and focused.

#### Acceptance Criteria

1. THE App SHALL provide a `PublicLayout` component at `src/layouts/PublicLayout.jsx` that renders a top navigation bar and an `<Outlet />` beneath it.
2. WHEN `PublicLayout` is rendered, THE App SHALL display the brand name "AI Interview Coach" as a link navigating to `/`.
3. WHEN the user is not authenticated, THE App SHALL render "Sign In" and "Get Started" navigation links in the `PublicLayout` header pointing to `/login` and `/register` respectively.
4. WHEN the user is authenticated, THE App SHALL render a "Go to Dashboard" link in the `PublicLayout` header pointing to `/dashboard`, replacing the Sign In and Get Started links.
5. THE App SHALL apply the `--color-bg` token as the page background color inside `PublicLayout`.

---

### Requirement 3 — Router Configuration

**User Story:** As a developer, I want the router to correctly separate public routes (using `PublicLayout`) from authenticated routes (using `MainLayout`), so that each group of pages gets the appropriate navigation shell.

#### Acceptance Criteria

1. THE App SHALL configure the `/` route to render `LandingPage` inside `PublicLayout`.
2. THE App SHALL configure the `/login` route to render `LoginPage` inside `PublicLayout`.
3. THE App SHALL configure the `/register` route to render `RegisterPage` inside `PublicLayout`.
4. THE App SHALL configure the `/dashboard`, `/resumes`, `/interviews`, `/interviews/create`, and `/interviews/:id` routes to render inside `MainLayout` wrapped by `ProtectedRoute`.
5. WHEN a user navigates to the root path `/`, THE App SHALL render `LandingPage` and NOT redirect to `LoginPage`.

---

### Requirement 4 — LandingPage

**User Story:** As a prospective user, I want a compelling landing page at `/` that explains what the product does and invites me to sign up, so that I can understand the value before registering.

#### Acceptance Criteria

1. THE App SHALL render a `LandingPage` component at `src/pages/LandingPage.jsx` served at the `/` route.
2. WHEN `LandingPage` is rendered, THE App SHALL display a hero section containing a headline, a subheadline, and two CTA buttons: "Start Practicing Free" (linking to `/register`) and "Sign In" (linking to `/login`).
3. WHEN `LandingPage` is rendered, THE App SHALL display a "How It Works" section with at least three numbered steps describing the product workflow.
4. WHEN `LandingPage` is rendered, THE App SHALL display a features section listing the key product benefits (AI-generated questions, real-time feedback, resume-tailored questions, and progress tracking).
5. WHEN `LandingPage` is rendered, THE App SHALL display an interview-preview section that shows a mock question card with a mock answer to illustrate the session experience.
6. WHEN `LandingPage` is rendered, THE App SHALL display a CTA footer section containing a call-to-action headline and a "Get Started Free" button linking to `/register`.
7. THE App SHALL apply the primary color (`--color-primary`) and design tokens throughout `LandingPage` for consistency with the rest of the application.

---

### Requirement 5 — MainLayout Redesign

**User Story:** As an authenticated user, I want a polished navigation bar that uses the brand color palette and clearly indicates the current section, so that navigation feels professional and cohesive.

#### Acceptance Criteria

1. WHEN `MainLayout` is rendered with an authenticated user, THE App SHALL display navigation links for "Dashboard", "Resumes", and "Interviews" in the top navigation bar.
2. WHEN `MainLayout` is rendered with an authenticated user, THE App SHALL display the authenticated user's `fullName` and a "Sign Out" button in the navigation bar.
3. WHEN the user clicks "Sign Out", THE App SHALL call `logout()` from `AuthContext` and navigate to `/login`.
4. THE App SHALL apply the `--color-navy` token as the background color of the `MainLayout` navigation bar.
5. THE App SHALL apply `--color-bg` as the page background for the content area rendered by `MainLayout`.
6. WHEN `MainLayout` is rendered with an unauthenticated user, THE App SHALL NOT render the authenticated navigation links; THE App SHALL render a "Sign In" link instead.

---

### Requirement 6 — LoginPage Redesign

**User Story:** As a returning user, I want a polished split-layout login page that matches the brand design tokens, so that the sign-in experience feels professional.

#### Acceptance Criteria

1. WHEN `LoginPage` is rendered, THE App SHALL display a SplitLayout with the left column using `--color-navy` background and the right column using `--color-surface` background.
2. THE App SHALL display the login form (email field, password field, and "Sign In" submit button) in the right column.
3. WHEN the user submits the form and the backend returns a top-level `message` error, THE App SHALL display that message as a form-level error above the submit button.
4. WHEN the user submits the form and the backend returns a `FieldError` for a specific field, THE App SHALL display the field-level error message inline below the corresponding input field.
5. THE App SHALL display a link to `/register` for users who do not yet have an account.
6. THE App SHALL apply `--color-primary` to the submit button background with `--color-primary-hover` on hover.

---

### Requirement 7 — RegisterPage Redesign

**User Story:** As a new user, I want a polished split-layout registration page with a confirm-password field and inline validation errors, so that I can create an account confidently.

#### Acceptance Criteria

1. WHEN `RegisterPage` is rendered, THE App SHALL display a SplitLayout matching the layout structure defined for `LoginPage`.
2. THE App SHALL display a registration form containing: Full Name, Email, Password, and Confirm Password fields.
3. WHEN the user submits the form and the Password and Confirm Password values do not match, THE App SHALL display a field-level error on the Confirm Password field before making any API call.
4. WHEN the user submits the form and the backend returns a `FieldError` for a specific field (e.g., `email` already taken), THE App SHALL display the field-level error message inline below the corresponding input field.
5. WHEN the user submits the form and the backend returns a top-level `message` error, THE App SHALL display that message as a form-level error.
6. THE App SHALL display a link to `/login` for users who already have an account.

---

### Requirement 8 — AuthContext Field Error Propagation

**User Story:** As a developer, I want `AuthContext` to return field-level validation errors from the backend so that `LoginPage` and `RegisterPage` can display them inline, giving users precise feedback on what to correct.

#### Acceptance Criteria

1. WHEN the backend returns a response with a top-level `errors` object (a map of field names to error strings), THE AuthContext `login` function SHALL include those errors in the returned result object under an `errors` key.
2. WHEN the backend returns a response with a top-level `errors` object, THE AuthContext `register` function SHALL include those errors in the returned result object under an `errors` key.
3. IF an Axios error occurs during `login` and the error response body contains an `errors` object, THE AuthContext `login` function SHALL include those errors in the returned result object under an `errors` key.
4. IF an Axios error occurs during `register` and the error response body contains an `errors` object, THE AuthContext `register` function SHALL include those errors in the returned result object under an `errors` key.
5. WHEN the backend returns neither a top-level `errors` object nor field-level errors, THE AuthContext `login` and `register` functions SHALL return a result object without an `errors` key (or with `errors` set to `null`).

---

### Requirement 9 — DashboardPage Redesign

**User Story:** As an authenticated user, I want a visually refined dashboard that uses the brand tokens and clearly surfaces my recent activity, so that I can quickly navigate to where I need to go.

#### Acceptance Criteria

1. WHEN `DashboardPage` is rendered, THE App SHALL display stat cards for "Total Interviews" and "Resumes" using the `--color-surface` background and `--color-border` border.
2. WHEN `DashboardPage` is rendered, THE App SHALL display a "Recent Interviews" section showing the five most recent interviews.
3. WHEN `DashboardPage` is rendered and the user has no interviews, THE App SHALL render an `EmptyState` component in the Recent Interviews section.
4. WHEN `DashboardPage` is rendered and the user has no resumes, THE App SHALL render an `EmptyState` component in the Resumes section.
5. THE App SHALL apply `--color-primary` to action links and CTA elements within `DashboardPage`.

---

### Requirement 10 — ResumesPage Bug Fix and Redesign

**User Story:** As a user, I want to upload only PDF resumes and see the page styled with the brand tokens, so that the upload experience is consistent and safe.

#### Acceptance Criteria

1. WHEN `ResumesPage` renders the file input, THE App SHALL set the `accept` attribute to `".pdf"` only, removing `.doc` and `.docx` from the accepted file types.
2. WHEN `ResumesPage` renders the upload label, THE App SHALL reflect the PDF-only constraint in the label text (e.g., "Select PDF File").
3. WHEN `ResumesPage` is rendered, THE App SHALL display uploaded resumes in a list using `--color-surface` cards with `--color-border` borders.
4. WHEN there are no resumes, THE App SHALL display an `EmptyState` component.

---

### Requirement 11 — InterviewsPage Redesign

**User Story:** As an authenticated user, I want the interviews list page to use the brand design tokens and have a prominent "Create Interview" CTA, so that starting a new session is easy.

#### Acceptance Criteria

1. WHEN `InterviewsPage` is rendered, THE App SHALL display a page header with the title "My Interviews" and a "Create Interview" button using `--color-primary`.
2. WHEN `InterviewsPage` is rendered and the user has no interviews, THE App SHALL display an `EmptyState` component.
3. WHEN `InterviewsPage` is rendered and interviews exist, THE App SHALL display each interview in a card showing job title, status badge, creation date, and action buttons (View, Delete).
4. THE App SHALL style status badges using distinct background/text color pairs: green for `COMPLETED`, yellow for `IN_PROGRESS`, and gray for `PENDING`.

---

### Requirement 12 — CreateInterviewPage Redesign

**User Story:** As a user, I want the interview creation form to be visually polished and clearly guide me through providing the job title, description, and optional resume selection.

#### Acceptance Criteria

1. WHEN `CreateInterviewPage` is rendered, THE App SHALL display a centered form card with fields for Job Title (required), Job Description (optional textarea), and Resume (optional select).
2. WHEN `CreateInterviewPage` is rendered and the user has no uploaded resumes, THE App SHALL display a note explaining that uploading a resume enables personalized questions.
3. WHEN the form is submitted successfully, THE App SHALL navigate to the new interview's session page at `/interviews/:id`.
4. THE App SHALL apply `--color-primary` to the submit button and `--color-border` to form field borders.

---

### Requirement 13 — InterviewSessionPage Redesign and Bug Fix (updateInterviewStatus)

**User Story:** As an interview candidate, I want to see one question at a time with Previous/Next navigation, progress dots, and a "Question X of Y" counter, so that the session feels focused and I can track my progress.

#### Acceptance Criteria

1. WHEN `InterviewSessionPage` is rendered with questions loaded, THE App SHALL display one question at a time based on the current question index.
2. WHEN `InterviewSessionPage` is rendered with questions, THE App SHALL display a "Question X of Y" label where X is the 1-based current question number and Y is the total question count.
3. WHEN `InterviewSessionPage` is rendered with questions, THE App SHALL display a row of `ProgressDot` indicators — one per question — filled for answered questions and unfilled for unanswered questions.
4. WHEN the user clicks the "Next" button, THE App SHALL advance the current question index by one, not exceeding the last question index.
5. WHEN the user clicks the "Previous" button, THE App SHALL decrement the current question index by one, not going below zero.
6. WHEN `handleGenerateFeedback` is called for the entire interview (not a single question), THE App SHALL NOT call `interviewApi.updateInterviewStatus`; instead THE App SHALL re-fetch the interview record via `interviewApi.getInterviewById` after feedback generation succeeds.
7. WHEN per-question feedback is generated via `handleGenerateFeedback(questionId)`, THE App SHALL NOT call `interviewApi.updateInterviewStatus` at any point during that operation.
8. WHEN the interview-level feedback generation succeeds, THE App SHALL update the local `interview` state with the freshly fetched interview data (including its new `status` from the backend).
9. WHEN all questions have been answered and no feedback exists yet, THE App SHALL display a "Generate Feedback" button on the current question card.
10. WHEN feedback exists for the current question, THE App SHALL display the feedback score and feedback text on the current question card.

---

### Requirement 14 — Shared Component Redesign

**User Story:** As a developer, I want all shared UI components (`Button`, `Card`, `Input`, `EmptyState`, `Loader`, `ErrorMessage`) updated to use the brand design tokens, so that the visual system is consistent across the entire application.

#### Acceptance Criteria

1. WHEN `Button` renders with `variant="primary"`, THE App SHALL apply `--color-primary` as the background color and `--color-primary-hover` on hover.
2. WHEN `Button` renders with `variant="danger"`, THE App SHALL apply a red background.
3. WHEN `Button` renders with `variant="outline"`, THE App SHALL apply a transparent background with a `--color-border` border.
4. WHEN `Card` is rendered, THE App SHALL apply `--color-surface` as the background and `--color-border` as the border color.
5. WHEN `Input` is rendered with an `error` prop, THE App SHALL display the error message text below the input field in red and apply a red border to the input element.
6. WHEN `Input` is rendered without an `error` prop, THE App SHALL apply `--color-border` as the input border color.
7. WHEN `Loader` is rendered, THE App SHALL display a centered spinner using `--color-primary`.
8. WHEN `ErrorMessage` is rendered with a non-empty `message` prop, THE App SHALL display the message in a styled error container.
9. WHEN `EmptyState` is rendered with `actionText` and `onAction` props, THE App SHALL display a CTA button using `--color-primary`.

---

### Requirement 15 — Documentation

**User Story:** As a team member, I want a `VOLUME-3-FRONTEND-DEFINITION.md` document describing the frontend architecture, design decisions, component inventory, and bug fixes implemented in this volume, so that the project history is traceable.

#### Acceptance Criteria

1. THE App SHALL include a file at `docs/VOLUME-3-FRONTEND-DEFINITION.md` that documents the design token definitions, the `PublicLayout` / `MainLayout` routing split, the `LandingPage` section inventory, the `InterviewSessionPage` one-at-a-time UX model, the three bug fixes (status update removal, PDF-only upload, field error propagation), and the complete list of created and modified files.
