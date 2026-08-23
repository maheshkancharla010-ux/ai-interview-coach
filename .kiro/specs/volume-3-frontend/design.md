# Design Document

## AI Interview Coach — Volume 3 Frontend Definition & Complete Implementation

---

## Overview

This document defines the technical architecture, component interfaces, data flows, and correctness properties for the Volume 3 frontend work. The deliverable is a fully redesigned React SPA using design tokens declared in Tailwind CSS v4's CSS-first `@theme {}` syntax, two new files (`LandingPage`, `PublicLayout`), targeted rewrites of all existing pages and shared components, and three bug fixes.

Stack: **React 19 + Vite 8 + Tailwind CSS v4 (`@tailwindcss/postcss`) + Axios + React Router 7**. No new npm packages.

---

## Architecture

### High-Level Structure

```
src/
├── index.css               ← @theme {} tokens + @import "tailwindcss"
├── main.jsx
├── App.jsx
├── routes/
│   └── index.jsx           ← PublicLayout routes + MainLayout routes
├── layouts/
│   ├── PublicLayout.jsx    ← NEW: header (logo + sign in/get started) + <Outlet />
│   └── MainLayout.jsx      ← REDESIGN: navy navbar + content area
├── context/
│   └── AuthContext.jsx     ← BUG FIX: propagate errors object from backend
├── pages/
│   ├── LandingPage.jsx     ← NEW: 6-section marketing page
│   ├── LoginPage.jsx       ← REDESIGN: split layout + field errors
│   ├── RegisterPage.jsx    ← REDESIGN: split layout + confirm password + field errors
│   ├── DashboardPage.jsx   ← REDESIGN: token alignment
│   ├── ResumesPage.jsx     ← BUG FIX + REDESIGN: pdf-only accept
│   ├── InterviewsPage.jsx  ← REDESIGN: token alignment
│   ├── CreateInterviewPage.jsx ← REDESIGN: centered card
│   └── InterviewSessionPage.jsx ← BUG FIX + REDESIGN: one-at-a-time UX
└── components/
    ├── Button.jsx          ← REDESIGN: size prop + token alignment
    ├── Card.jsx            ← REDESIGN: token alignment
    ├── Input.jsx           ← REDESIGN: fieldError prop + token alignment
    ├── EmptyState.jsx      ← REDESIGN: token alignment
    ├── Loader.jsx          ← REDESIGN: token alignment
    └── ErrorMessage.jsx    ← REDESIGN: token alignment
```

### Routing Architecture

The router splits public and authenticated routes into two separate layout trees:

```
/                → PublicLayout > LandingPage
/login           → PublicLayout > LoginPage
/register        → PublicLayout > RegisterPage
/dashboard       → MainLayout > ProtectedRoute > DashboardPage
/resumes         → MainLayout > ProtectedRoute > ResumesPage
/interviews      → MainLayout > ProtectedRoute > InterviewsPage
/interviews/create → MainLayout > ProtectedRoute > CreateInterviewPage
/interviews/:id  → MainLayout > ProtectedRoute > InterviewSessionPage
```

**Key change from current state:** The root `/` route previously rendered `LoginPage` inside `MainLayout`. After this work it renders `LandingPage` inside `PublicLayout`. This removes the unintended redirect-to-login behavior for unauthenticated visitors.

---

## Design Token System

### `src/index.css`

```css
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');
@import "tailwindcss";

@theme {
  --color-primary: #2563EB;
  --color-primary-hover: #1D4ED8;
  --color-navy: #0F172A;
  --color-bg: #F8FAFC;
  --color-surface: #FFFFFF;
  --color-border: #E2E8F0;
  --color-text-primary: #111827;
  --color-text-secondary: #6B7280;
  --font-sans: 'Inter', system-ui, sans-serif;
}

body {
  margin: 0;
  font-family: var(--font-sans);
  background-color: var(--color-bg);
}
```

Tailwind v4 maps `@theme` CSS custom properties to utility classes automatically:
- `--color-primary` → `bg-primary`, `text-primary`, `border-primary`
- `--color-navy` → `bg-navy`, `text-navy`
- `--color-bg` → `bg-bg`
- `--color-surface` → `bg-surface`
- `--color-border` → `border-border`
- `--color-text-secondary` → `text-text-secondary`
- `--font-sans` → `font-sans`

The existing `tailwind.config.js` is left untouched.

---

## Component Interfaces

### `Button`

```jsx
Button({
  children,
  onClick,
  type = 'button',
  variant = 'primary',   // 'primary' | 'secondary' | 'danger' | 'outline'
  size = 'md',           // 'sm' | 'md' | 'lg'  ← NEW
  disabled = false,
  className = ''
})
```

Size classes:
- `sm`: `px-3 py-1.5 text-xs`
- `md`: `px-4 py-2 text-sm` (current default)
- `lg`: `px-6 py-3 text-base`

Variant token mapping:
- `primary`: `bg-primary hover:bg-primary-hover text-white focus:ring-primary`
- `secondary`: `bg-gray-200 text-gray-900 hover:bg-gray-300`
- `danger`: `bg-red-600 text-white hover:bg-red-700 focus:ring-red-500`
- `outline`: `border border-border text-text-primary hover:bg-bg focus:ring-primary`

### `Input`

```jsx
Input({
  label,
  type = 'text',
  name,
  value,
  onChange,
  placeholder,
  error,          // top-level error string (existing)
  fieldError,     // field-level error from backend errors map ← NEW
  required = false,
  className = ''
})
```

Display priority: if `error` is set, show `error`; else if `fieldError` is set, show `fieldError`. Both apply a red border. The `fieldError` prop allows pages to pass backend-returned per-field validation messages directly without conflating them with generic form errors.

### `Card`

```jsx
Card({ children, className = '' })
```

Token update: `bg-surface border border-border shadow-sm rounded-lg`.

### `EmptyState`

```jsx
EmptyState({ title, description, actionText, onAction })
```

CTA button updated to use `bg-primary hover:bg-primary-hover`.

### `Loader`

```jsx
Loader()
```

Spinner border updated to `border-primary`.

### `ErrorMessage`

```jsx
ErrorMessage({ message })
```

Styling updated to use token-aligned red palette.

---

## Layout Components

### `PublicLayout` (new)

```
┌─────────────────────────────────────────────┐
│ Header: "AI Interview Coach" logo | Sign In · Get Started  │
│         (or "Go to Dashboard" when authenticated)          │
├─────────────────────────────────────────────┤
│                  <Outlet />                  │
└─────────────────────────────────────────────┘
```

- Header background: `bg-surface` with `border-b border-border`
- Page background: `bg-bg`
- Reads `isAuthenticated` from `useAuth()` to conditionally render links

### `MainLayout` (redesign)

```
┌─────────────────────────────────────────────┐
│ Navbar: [Logo] [Dashboard] [Resumes] [Interviews] | [Name] [Sign Out] │
│ bg-navy text-white                           │
├─────────────────────────────────────────────┤
│              bg-bg content area              │
│              <Outlet />                      │
└─────────────────────────────────────────────┘
```

- Navbar: `bg-navy` with white text, active-link underline or highlight
- Content area: `bg-bg`, `max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8`
- Unauthenticated fallback: renders a "Sign In" link instead of nav links

---

## Page Designs

### `LandingPage`

Six sections rendered in sequence, full-width layout:

1. **Hero** — `bg-blue-50` (or light variant), large headline, subtext, two `<Link>` CTAs
2. **Trust bar** — 3 checkmark items in a row
3. **How It Works** — 4 numbered steps in a grid or vertical flow
4. **Features** — 2×2 card grid: AI Questions, Real-time Feedback, Resume-tailored, Progress Tracking
5. **Interview Preview** — Split left (mock question) / right (mock answer) panel
6. **CTA Footer** — `bg-navy` background, white text, single `<Link>` "Get Started Free" → `/register`

No API calls; purely static JSX.

### `LoginPage` (SplitLayout)

```
┌──────────────────────┬──────────────────────┐
│  bg-navy (left 50%)  │  bg-surface (right)  │
│  ─────────────────── │  ──────────────────  │
│  Logo top-left       │  "Welcome back" h2   │
│  Quote centered      │  Subtitle            │
│  Blue tagline        │  Email input         │
│  Footer row          │  Password + eye icon │
│                      │  Form-level error    │
│                      │  Sign In button      │
│                      │  Register link       │
└──────────────────────┴──────────────────────┘
```

State: `{ email, password, showPassword, formError, fieldErrors, loading }`

`fieldErrors` shape: `{ email?: string, password?: string }`

On submit: call `login()`, on failure check `result.errors` (field errors) and `result.message` (form error).

### `RegisterPage` (SplitLayout)

Same split layout. Right column has 4 fields (Full Name, Email, Password, Confirm Password). Client-side confirm-password mismatch check before API call sets `fieldErrors.confirmPassword`. Backend field errors set `fieldErrors.fullName | email | password`.

### `DashboardPage`

- 2 stat cards: Total Interviews, Resumes
- Recent Interviews section (up to 5 items, `EmptyState` if none)
- Quick Actions row: "New Interview" → `/interviews/create`, "Upload Resume" → `/resumes`

### `ResumesPage` (bug fix)

Bug: `accept=".pdf,.doc,.docx"` → fix to `accept=".pdf"`. Label: "Select PDF File".

### `InterviewSessionPage` (redesign + bug fix)

#### State Model

```js
const [interview, setInterview] = useState(null);
const [questions, setQuestions] = useState([]);
const [feedback, setFeedback] = useState([]);
const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
const [currentAnswer, setCurrentAnswer] = useState({});
const [loading, setLoading] = useState(true);
const [generatingQuestions, setGeneratingQuestions] = useState(false);
const [generatingFeedback, setGeneratingFeedback] = useState(false);
const [error, setError] = useState('');
```

#### Question Navigation

```js
// Show only the current question
const currentQuestion = questions[currentQuestionIndex];

// Next — clamp at last index
const handleNext = () =>
  setCurrentQuestionIndex(i => Math.min(i + 1, questions.length - 1));

// Previous — clamp at 0
const handlePrevious = () =>
  setCurrentQuestionIndex(i => Math.max(i - 1, 0));
```

#### Progress Dots

```jsx
{questions.map((q, i) => (
  <span
    key={q.id}
    className={`w-3 h-3 rounded-full inline-block ${
      i === currentQuestionIndex
        ? 'bg-primary'
        : q.answerText
        ? 'bg-primary opacity-60'
        : 'border-2 border-border bg-transparent'
    }`}
  />
))}
```

#### Bug Fix — `handleGenerateFeedback`

**Before (buggy):**
```js
const handleGenerateFeedback = async (questionId = null) => {
  // ... generates feedback ...
  await interviewApi.updateInterviewStatus(id, 'COMPLETED');  // ← BUG
  setInterview(prev => ({ ...prev, status: 'COMPLETED' }));
};
```

**After (fixed):**
```js
const handleGenerateFeedback = async (questionId = null) => {
  setGeneratingFeedback(true);
  setError('');
  try {
    let response;
    if (questionId) {
      // Per-question feedback: only generate, never update status
      response = await feedbackApi.generateFeedbackForQuestion(questionId);
    } else {
      // Interview-level feedback: generate then re-fetch interview
      response = await feedbackApi.generateFeedbackForInterview(id);
    }

    if (response.success) {
      const feedbackResponse = await feedbackApi.getFeedbackByInterview(id);
      if (feedbackResponse.success) {
        setFeedback(feedbackResponse.data || []);
      }

      // Only re-fetch interview for interview-level feedback
      // Never call updateInterviewStatus — backend sets status on feedback generation
      if (!questionId) {
        const freshInterview = await interviewApi.getInterviewById(id);
        if (freshInterview.success) {
          setInterview(freshInterview.data);
        }
      }
    } else {
      setError(response.message || 'Failed to generate feedback');
    }
  } catch (err) {
    setError('Failed to generate feedback');
  } finally {
    setGeneratingFeedback(false);
  }
};
```

The backend sets `COMPLETED` status when interview-level feedback is generated. The frontend re-fetches the interview to reflect the authoritative backend state. No client-side status override.

#### "Generate Feedback" button appearance condition

```js
const allAnswered = questions.length > 0 && questions.every(q => q.answerText);
const hasFeedback = feedback.length > 0;

// Show Generate Feedback button when all answered AND no feedback yet
{allAnswered && !hasFeedback && (
  <Button variant="primary" onClick={() => handleGenerateFeedback()}>
    Generate Feedback
  </Button>
)}
```

---

## Data Models

### Auth Response Shape

```ts
// Success (200)
{
  success: true,
  data: { token: string, user: { id, fullName, email } }
}

// Validation failure (400)
{
  success: false,
  message: string,
  errors?: {          // field-level errors map
    fullName?: string,
    email?: string,
    password?: string
  }
}
```

### `AuthContext` return shape from `login()` / `register()`

```ts
// Success
{ success: true }

// Failure
{
  success: false,
  message: string,
  errors?: { [fieldName: string]: string }
}
```

The `errors` key mirrors the backend response body's `errors` map verbatim.

---

## Error Handling

| Scenario | Handling |
|---|---|
| Network error in auth | Catch block returns `{ success: false, message: '...' }` without `errors` |
| Backend 400 with field errors | `result.errors` populated; page maps to `fieldErrors` state |
| Backend 400 with message only | `result.message` shown as form-level error |
| API failures in page hooks | `setError(message)` → `<ErrorMessage>` renders |
| Interview/resume not found | `<ErrorMessage>` + back navigation link |
| File upload wrong type (client) | Browser enforces via `accept=".pdf"` attribute |

---

## Correctness Properties

*A property is a characteristic or behavior that should hold true across all valid executions of a system — essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.*

**Property Reflection:** After completing prework, redundant properties have been identified and consolidated:
- Properties 13.4 and 13.5 (Next/Previous bounds clamping) are merged into one navigation bounds property.
- Properties 6.3 and 7.5 (form-level error display) are structurally identical and covered by a single shared pattern; two properties are retained to keep requirements traceability clear.
- Properties 8.1 and 8.3 are consolidated (success path and error path for `login` errors passthrough), and similarly for 8.2 and 8.4.

---

### Property 1: MainLayout displays authenticated user's name

*For any* user object with any `fullName` string, when `MainLayout` is rendered in authenticated state, the rendered output SHALL contain that `fullName` string.

**Validates: Requirements 5.2**

---

### Property 2: Login form-level error display

*For any* non-empty error message string returned by the backend as the top-level `message` field, when `LoginPage` renders after a failed login attempt, the rendered output SHALL contain that error message string as visible text.

**Validates: Requirements 6.3**

---

### Property 3: Login field-level error display

*For any* `errors` map returned by the backend (with any combination of `email` and `password` keys), when `LoginPage` renders after a failed login, each field whose key appears in the errors map SHALL have the corresponding error string rendered inline below its input element.

**Validates: Requirements 6.4**

---

### Property 4: Password confirmation mismatch rejection

*For any* pair of non-empty strings where `password !== confirmPassword`, submitting the `RegisterPage` form SHALL display a field-level error on the Confirm Password field and SHALL NOT make any API call.

**Validates: Requirements 7.3**

---

### Property 5: Register field-level error display

*For any* `errors` map returned by the backend (with any combination of `fullName`, `email`, and `password` keys), when `RegisterPage` renders after a failed registration, each field whose key appears in the errors map SHALL have the corresponding error string rendered inline below its input element.

**Validates: Requirements 7.4**

---

### Property 6: AuthContext login errors passthrough

*For any* errors map present in the backend API response (whether a direct success/failure response body or an Axios error response body), `AuthContext.login()` SHALL include that map verbatim as the `errors` key in its returned result object.

**Validates: Requirements 8.1, 8.3**

---

### Property 7: AuthContext register errors passthrough

*For any* errors map present in the backend API response (whether a direct success/failure response body or an Axios error response body), `AuthContext.register()` SHALL include that map verbatim as the `errors` key in its returned result object.

**Validates: Requirements 8.2, 8.4**

---

### Property 8: AuthContext no spurious errors key

*For any* successful authentication response that contains no `errors` field, `AuthContext.login()` and `AuthContext.register()` SHALL return a result object where `errors` is either absent or `null`/`undefined`, never a truthy value.

**Validates: Requirements 8.5**

---

### Property 9: Dashboard shows at most five recent interviews

*For any* list of N interviews returned by the API, the "Recent Interviews" section of `DashboardPage` SHALL render exactly `min(N, 5)` interview entries.

**Validates: Requirements 9.2**

---

### Property 10: Interview list renders all required fields per row

*For any* interview object in the interviews list, `InterviewsPage` SHALL render an element containing the interview's `jobTitle`, an element reflecting its `status`, its formatted `createdAt` date, and both a "View" link and a "Delete" button.

**Validates: Requirements 11.3**

---

### Property 11: Status badge color correctness

*For any* interview with any `status` value, the status badge rendered by `InterviewsPage` (and `DashboardPage`) SHALL use green coloring for `COMPLETED`, yellow/amber coloring for `IN_PROGRESS`, and gray coloring for `PENDING` or any unrecognized status.

**Validates: Requirements 11.4**

---

### Property 12: InterviewSessionPage shows one question at a time

*For any* list of N questions and any `currentQuestionIndex` i in `[0, N-1]`, `InterviewSessionPage` SHALL render the question text of `questions[i]` and SHALL NOT render the question text of any other question simultaneously.

**Validates: Requirements 13.1**

---

### Property 13: Question counter label correctness

*For any* `currentQuestionIndex` i and total question count N > 0, the "Question X of Y" label SHALL display `i + 1` as X and `N` as Y.

**Validates: Requirements 13.2**

---

### Property 14: ProgressDot state matches question answered state

*For any* list of questions with any combination of answered/unanswered states, each `ProgressDot` at index i SHALL render as filled when `questions[i].answerText` is truthy and as unfilled (outline) when it is falsy.

**Validates: Requirements 13.3**

---

### Property 15: Question navigation bounds clamping

*For any* `currentQuestionIndex` i and questions array of length N:
- Clicking "Next" when `i < N - 1` SHALL advance the index to `i + 1`; when `i = N - 1` it SHALL remain at `N - 1`
- Clicking "Previous" when `i > 0` SHALL decrement the index to `i - 1`; when `i = 0` it SHALL remain at `0`

**Validates: Requirements 13.4, 13.5**

---

### Property 16: Interview-level feedback generation never calls updateInterviewStatus

*For any* interview ID, calling `handleGenerateFeedback()` without a `questionId` argument SHALL invoke `feedbackApi.generateFeedbackForInterview` and `interviewApi.getInterviewById`, and SHALL NOT invoke `interviewApi.updateInterviewStatus` at any point during that execution path.

**Validates: Requirements 13.6**

---

### Property 17: Per-question feedback generation never calls updateInterviewStatus

*For any* question ID, calling `handleGenerateFeedback(questionId)` SHALL invoke `feedbackApi.generateFeedbackForQuestion` and SHALL NOT invoke `interviewApi.updateInterviewStatus` at any point during that execution path.

**Validates: Requirements 13.7**

---

### Property 18: Interview state after feedback reflects backend data

*For any* interview, after `generateFeedbackForInterview` succeeds, the local `interview` state in `InterviewSessionPage` SHALL equal the data returned by the subsequent `getInterviewById` call, not a manually patched version of the previous state.

**Validates: Requirements 13.8**

---

### Property 19: Generate Feedback button appears only when all answered and no feedback

*For any* questions array and feedback array, the "Generate Feedback" button SHALL be visible if and only if all questions have a truthy `answerText` AND the feedback array is empty.

**Validates: Requirements 13.9**

---

### Property 20: Input field error display

*For any* non-empty string passed as the `error` or `fieldError` prop to `Input`, the rendered output SHALL contain an element with that error string as text content, and the input element SHALL have a red border class applied.

**Validates: Requirements 14.5**

---

### Property 21: ErrorMessage renders non-empty messages

*For any* non-empty string passed as the `message` prop to `ErrorMessage`, the rendered output SHALL contain a visible element with that string as text content.

**Validates: Requirements 14.8**

---

### Property 22: EmptyState CTA button renders with provided text

*For any* non-empty `actionText` string and any `onAction` function passed to `EmptyState`, the rendered output SHALL contain a button element with the `actionText` as its label.

**Validates: Requirements 14.9**
