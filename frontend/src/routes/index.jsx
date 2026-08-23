import { createBrowserRouter } from 'react-router-dom';
import PublicLayout from '../layouts/PublicLayout';
import MainLayout from '../layouts/MainLayout';
import ProtectedRoute from '../components/ProtectedRoute';
import LandingPage from '../pages/LandingPage';
import LoginPage from '../pages/LoginPage';
import RegisterPage from '../pages/RegisterPage';
import DashboardPage from '../pages/DashboardPage';
import ResumesPage from '../pages/ResumesPage';
import InterviewsPage from '../pages/InterviewsPage';
import CreateInterviewPage from '../pages/CreateInterviewPage';
import InterviewSessionPage from '../pages/InterviewSessionPage';

const router = createBrowserRouter([
  {
    element: <PublicLayout />,
    children: [
      { path: '/', element: <LandingPage /> },
      { path: '/login', element: <LoginPage /> },
      { path: '/register', element: <RegisterPage /> },
    ],
  },
  {
    element: <MainLayout />,
    children: [
      { path: '/dashboard', element: <ProtectedRoute><DashboardPage /></ProtectedRoute> },
      { path: '/resumes', element: <ProtectedRoute><ResumesPage /></ProtectedRoute> },
      { path: '/interviews', element: <ProtectedRoute><InterviewsPage /></ProtectedRoute> },
      { path: '/interviews/create', element: <ProtectedRoute><CreateInterviewPage /></ProtectedRoute> },
      { path: '/interviews/:id', element: <ProtectedRoute><InterviewSessionPage /></ProtectedRoute> },
    ],
  },
]);

export default router;
