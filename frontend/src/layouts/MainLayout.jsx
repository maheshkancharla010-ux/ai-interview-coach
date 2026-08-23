import { Outlet, Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const MainLayout = () => {
  const { user, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => { logout(); navigate('/login'); };

  const navLink = (to, label) => {
    const active = location.pathname === to || location.pathname.startsWith(to + '/');
    return (
      <Link to={to} className={"text-sm font-medium transition-colors px-1 pb-0.5 border-b-2 " + (active ? "text-white border-white" : "text-gray-400 border-transparent hover:text-white hover:border-gray-400")}>{label}</Link>
    );
  };

  return (
    <div className="min-h-screen bg-bg">
      <nav className="bg-navy shadow-md">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
          <div className="flex items-center gap-8">
            <Link to="/dashboard" className="flex items-center gap-2 text-lg font-bold text-white">
              <span className="w-8 h-8 bg-primary rounded-lg flex items-center justify-center text-white text-sm font-bold">IC</span>
              Interview Coach
            </Link>
            {isAuthenticated && (
              <div className="hidden sm:flex items-center gap-6">
                {navLink('/dashboard', 'Dashboard')}
                {navLink('/resumes', 'Resumes')}
                {navLink('/interviews', 'Interviews')}
              </div>
            )}
          </div>
          <div className="flex items-center gap-4">
            {isAuthenticated ? (
              <>
                <span className="text-sm text-gray-300 hidden sm:block">{user?.fullName}</span>
                <button onClick={handleLogout} className="px-4 py-2 text-sm font-medium text-white border border-gray-600 rounded-lg hover:bg-gray-700 transition-colors">Sign Out</button>
              </>
            ) : (
              <Link to="/login" className="text-sm text-gray-300 hover:text-white">Sign In</Link>
            )}
          </div>
        </div>
      </nav>
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <Outlet />
      </main>
    </div>
  );
};
export default MainLayout;
