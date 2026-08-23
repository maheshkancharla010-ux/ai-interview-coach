import { createContext, useContext, useState, useEffect } from 'react';
import { authApi } from '../api/authApi';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const storedToken = localStorage.getItem('token');
    const storedUser = localStorage.getItem('user');
    if (storedToken && storedUser) {
      try { setToken(storedToken); setUser(JSON.parse(storedUser)); }
      catch { localStorage.removeItem('token'); localStorage.removeItem('user'); }
    }
    setLoading(false);
  }, []);

  const login = async (credentials) => {
    try {
      const response = await authApi.login(credentials);
      if (response.success && response.data) {
        const { token: newToken, user: newUser } = response.data;
        setToken(newToken); setUser(newUser);
        localStorage.setItem('token', newToken);
        localStorage.setItem('user', JSON.stringify(newUser));
        return { success: true };
      }
      return { success: false, message: response.message || 'Login failed', errors: response.data || null };
    } catch (error) {
      const errData = error.response?.data;
      return { success: false, message: errData?.message || error.message || 'Login failed', errors: errData?.data || null };
    }
  };

  const register = async (userData) => {
    try {
      const response = await authApi.register(userData);
      if (response.success && response.data) {
        const { token: newToken, user: newUser } = response.data;
        setToken(newToken); setUser(newUser);
        localStorage.setItem('token', newToken);
        localStorage.setItem('user', JSON.stringify(newUser));
        return { success: true };
      }
      return { success: false, message: response.message || 'Registration failed', errors: response.data || null };
    } catch (error) {
      const errData = error.response?.data;
      return { success: false, message: errData?.message || error.message || 'Registration failed', errors: errData?.data || null };
    }
  };

  const logout = () => {
    setToken(null); setUser(null);
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  };

  return (
    <AuthContext.Provider value={{ user, token, loading, login, register, logout, isAuthenticated: !!token }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within an AuthProvider');
  return ctx;
};
