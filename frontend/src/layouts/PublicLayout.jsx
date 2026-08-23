import { Outlet, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const PublicLayout = () => {
  const { isAuthenticated } = useAuth();
  return (
    <div className="min-h-screen bg-bg">
      <header className="bg-surface border-b border-border">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
          <Link to="/" className="flex items-center gap-2 text-lg font-bold text-navy">
            <span className="w-8 h-8 bg-primary rounded-lg flex items-center justify-center text-white text-sm font-bold">IC</span>
            Interview Coach
          </Link>
          <nav className="flex items-center gap-4">
            {isAuthenticated ? (
              <Link to="/dashboard" className="px-4 py-2 bg-primary text-white text-sm font-medium rounded-lg hover:bg-primary-hover">Go to Dashboard</Link>
            ) : (
              <>
                <Link to="/login" className="text-sm text-text-secondary hover:text-text-primary">Sign In</Link>
                <Link to="/register" className="px-4 py-2 bg-primary text-white text-sm font-medium rounded-lg hover:bg-primary-hover">Get Started</Link>
              </>
            )}
          </nav>
        </div>
      </header>
      <Outlet />
    </div>
  );
};
export default PublicLayout;
