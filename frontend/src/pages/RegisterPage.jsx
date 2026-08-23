import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Input from '../components/Input';
import ErrorMessage from '../components/ErrorMessage';

const RegisterPage = () => {
  const navigate = useNavigate();
  const { register } = useAuth();
  const [form, setForm] = useState({ fullName: '', email: '', password: '', confirmPassword: '' });
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [formError, setFormError] = useState('');
  const [fieldErrors, setFieldErrors] = useState({});
  const [loading, setLoading] = useState(false);

  const handleChange = e => { setForm(p => ({ ...p, [e.target.name]: e.target.value })); setFormError(''); setFieldErrors(p => ({ ...p, [e.target.name]: '' })); };

  const handleSubmit = async e => {
    e.preventDefault();
    setFormError(''); setFieldErrors({});
    if (form.password !== form.confirmPassword) {
      setFieldErrors({ confirmPassword: 'Passwords do not match' });
      return;
    }
    setLoading(true);
    try {
      const result = await register({ fullName: form.fullName, email: form.email, password: form.password });
      if (result.success) { navigate('/dashboard'); }
      else {
        if (result.errors && typeof result.errors === 'object') setFieldErrors(result.errors);
        else setFormError(result.message || 'Registration failed');
      }
    } finally { setLoading(false); }
  };

  return (
    <div className="min-h-screen flex">
      <div className="hidden lg:flex lg:w-1/2 bg-navy flex-col p-12">
        <Link to="/" className="flex items-center gap-2 text-white text-lg font-bold">
          <span className="w-8 h-8 bg-primary rounded-lg flex items-center justify-center text-sm font-bold">IC</span>Interview Coach</Link>
        <div className="my-auto">
          <p className="text-primary text-sm font-medium mb-4">Your interview journey starts here</p>
          <h2 className="text-3xl font-bold text-white mb-8">From upload to offer-ready.</h2>
          <ol className="space-y-3">
            {['Upload your resume','Practice with personalized questions','Get AI feedback on every answer','Track your progress'].map((s,i) => (
              <li key={i} className="flex items-center gap-3 text-gray-300 text-sm">
                <span className="w-6 h-6 rounded-full border border-primary text-primary text-xs flex items-center justify-center font-medium flex-shrink-0">{i+1}</span>{s}
              </li>
            ))}
          </ol>
        </div>
        <p className="text-gray-500 text-sm mt-auto">Free to use. No credit card required.</p>
      </div>
      <div className="w-full lg:w-1/2 flex items-center justify-center p-8 bg-surface">
        <div className="w-full max-w-sm">
          <h1 className="text-2xl font-bold text-text-primary mb-1">Create your account</h1>
          <p className="text-sm text-text-secondary mb-8">Start practicing for interviews today.</p>
          {formError && <div className="mb-4"><ErrorMessage message={formError} /></div>}
          <form onSubmit={handleSubmit} className="space-y-5">
            <Input label="Full name" name="fullName" value={form.fullName} onChange={handleChange} placeholder="Mahesh Rathod" fieldError={fieldErrors.fullName} required />
            <Input label="Email" type="email" name="email" value={form.email} onChange={handleChange} placeholder="you@example.com" fieldError={fieldErrors.email} required />
            <div>
              <Input label="Password" type={showPassword ? 'text' : 'password'} name="password" value={form.password} onChange={handleChange} placeholder="Create a password" fieldError={fieldErrors.password} required />
              <button type="button" onClick={() => setShowPassword(p => !p)} className="text-xs text-text-secondary mt-1">{showPassword ? 'Hide' : 'Show'} password</button>
            </div>
            <div>
              <Input label="Confirm password" type={showConfirm ? 'text' : 'password'} name="confirmPassword" value={form.confirmPassword} onChange={handleChange} placeholder="Repeat your password" fieldError={fieldErrors.confirmPassword} required />
              <button type="button" onClick={() => setShowConfirm(p => !p)} className="text-xs text-text-secondary mt-1">{showConfirm ? 'Hide' : 'Show'}</button>
            </div>
            <button type="submit" disabled={loading} className="w-full py-3 bg-primary text-white font-semibold rounded-lg hover:bg-primary-hover disabled:opacity-50 disabled:cursor-not-allowed transition-colors">{loading ? 'Creating account...' : 'Create Account'}</button>
          </form>
          <p className="mt-4 text-center text-sm text-text-secondary">Already have an account? <Link to="/login" className="text-primary font-medium hover:underline">Sign in</Link></p>
          <p className="mt-3 text-center text-xs text-gray-400">By creating an account, you agree to our terms of service and privacy policy.</p>
        </div>
      </div>
    </div>
  );
};
export default RegisterPage;
