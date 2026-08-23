import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Input from '../components/Input';
import ErrorMessage from '../components/ErrorMessage';

const LoginPage = () => {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [form, setForm] = useState({ email: '', password: '' });
  const [showPassword, setShowPassword] = useState(false);
  const [formError, setFormError] = useState('');
  const [fieldErrors, setFieldErrors] = useState({});
  const [loading, setLoading] = useState(false);

  const handleChange = e => { setForm(p => ({ ...p, [e.target.name]: e.target.value })); setFormError(''); setFieldErrors(p => ({ ...p, [e.target.name]: '' })); };

  const handleSubmit = async e => {
    e.preventDefault();
    setFormError(''); setFieldErrors({});
    setLoading(true);
    try {
      const result = await login(form);
      if (result.success) { navigate('/dashboard'); }
      else {
        if (result.errors && typeof result.errors === 'object') setFieldErrors(result.errors);
        else setFormError(result.message || 'Login failed');
      }
    } finally { setLoading(false); }
  };

  return (
    <div className="min-h-screen flex">
      <div className="hidden lg:flex lg:w-1/2 bg-navy flex-col p-12">
        <Link to="/" className="flex items-center gap-2 text-white text-lg font-bold mb-auto">
          <span className="w-8 h-8 bg-primary rounded-lg flex items-center justify-center text-sm font-bold">IC</span>Interview Coach</Link>
        <div className="my-auto">
          <blockquote className="text-xl text-white italic leading-relaxed mb-6">
            "The best way to prepare for an interview is to practice answering real questions out loud — until the words come naturally."
          </blockquote>
          <p className="text-primary text-sm font-medium">Focused practice. Honest feedback. Better interviews.</p>
        </div>
        <div className="flex items-center gap-3 text-gray-500 text-sm mt-auto">
          <span>Secure</span><span>·</span><span>Private</span><span>·</span><span>Free</span>
        </div>
      </div>
      <div className="w-full lg:w-1/2 flex items-center justify-center p-8 bg-surface">
        <div className="w-full max-w-sm">
          <h1 className="text-2xl font-bold text-text-primary mb-1">Welcome back</h1>
          <p className="text-sm text-text-secondary mb-8">Continue your interview preparation.</p>
          {formError && <div className="mb-4"><ErrorMessage message={formError} /></div>}
          <form onSubmit={handleSubmit} className="space-y-5">
            <Input label="Email" type="email" name="email" value={form.email} onChange={handleChange} placeholder="you@example.com" fieldError={fieldErrors.email} required />
            <div>
              <Input label="Password" type={showPassword ? 'text' : 'password'} name="password" value={form.password} onChange={handleChange} placeholder="Your password" fieldError={fieldErrors.password} required />
              <button type="button" onClick={() => setShowPassword(p => !p)} className="text-xs text-text-secondary hover:text-text-primary mt-1">{showPassword ? 'Hide' : 'Show'} password</button>
            </div>
            <button type="submit" disabled={loading} className="w-full py-3 bg-primary text-white font-semibold rounded-lg hover:bg-primary-hover disabled:opacity-50 disabled:cursor-not-allowed transition-colors">{loading ? 'Signing in...' : 'Sign In'}</button>
          </form>
          <p className="mt-6 text-center text-sm text-text-secondary">Don't have an account? <Link to="/register" className="text-primary font-medium hover:underline">Create account</Link></p>
        </div>
      </div>
    </div>
  );
};
export default LoginPage;
