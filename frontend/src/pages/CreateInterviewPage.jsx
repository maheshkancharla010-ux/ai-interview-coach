import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { interviewApi } from '../api/interviewApi';
import { resumeApi } from '../api/resumeApi';
import Input from '../components/Input';
import Button from '../components/Button';
import Card from '../components/Card';
import Loader from '../components/Loader';
import ErrorMessage from '../components/ErrorMessage';

const CreateInterviewPage = () => {
  const navigate = useNavigate();
  const [form, setForm] = useState({ jobTitle: '', jobDescription: '', resumeId: '' });
  const [resumes, setResumes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [fetchingResumes, setFetchingResumes] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    resumeApi.getAllResumes()
      .then(r => { if (r.success) setResumes(r.data || []); })
      .catch(() => {})
      .finally(() => setFetchingResumes(false));
  }, []);

  const handleChange = e => { setForm(p => ({ ...p, [e.target.name]: e.target.value })); setError(''); };

  const handleSubmit = async e => {
    e.preventDefault(); setError(''); setLoading(true);
    try {
      const payload = { jobTitle: form.jobTitle, jobDescription: form.jobDescription || null, resumeId: form.resumeId ? Number(form.resumeId) : null };
      const r = await interviewApi.createInterview(payload);
      if (r.success) navigate('/interviews/' + r.data.id);
      else setError(r.message || 'Failed to create interview');
    } catch { setError('Failed to create interview'); } finally { setLoading(false); }
  };

  if (fetchingResumes) return <Loader />;

  return (
    <div>
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-text-primary">Create Interview</h1>
        <p className="text-text-secondary text-sm mt-0.5">Set up a new practice interview session</p>
      </div>
      <Card className="max-w-2xl p-8">
        {error && <div className="mb-5"><ErrorMessage message={error} /></div>}
        <form onSubmit={handleSubmit} className="space-y-5">
          <Input label="Job Title" name="jobTitle" value={form.jobTitle} onChange={handleChange} placeholder="e.g., Senior Software Engineer" required />
          <div>
            <label className="block text-sm font-medium text-text-primary mb-1.5">Job Description <span className="text-text-secondary font-normal">(optional)</span></label>
            <textarea name="jobDescription" value={form.jobDescription} onChange={handleChange} placeholder="Describe the role, requirements, and responsibilities..." rows={4} className="w-full px-3 py-2.5 rounded-lg border border-border bg-surface text-text-primary text-sm focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary resize-none" /></div>
          <div>
            <label className="block text-sm font-medium text-text-primary mb-1.5">Resume <span className="text-text-secondary font-normal">(optional)</span></label>
            <select name="resumeId" value={form.resumeId} onChange={handleChange} className="w-full px-3 py-2.5 rounded-lg border border-border bg-surface text-text-primary text-sm focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
              <option value="">No resume selected</option>
              {resumes.map(r => <option key={r.id} value={r.id}>{r.fileName}</option>)}
            </select>
            {resumes.length === 0 && <p className="text-xs text-text-secondary mt-1">Upload a resume first to get personalized questions.</p>}
            {resumes.length > 0 && <p className="text-xs text-text-secondary mt-1">Selecting a resume generates more relevant questions.</p>}
          </div>
          <div className="flex gap-3 pt-2">
            <Button type="submit" disabled={loading} className="flex-1">{loading ? 'Creating...' : 'Create Interview'}</Button>
            <Button type="button" variant="outline" onClick={() => navigate('/interviews')} disabled={loading}>Cancel</Button>
          </div>
        </form>
      </Card>
    </div>
  );
};
export default CreateInterviewPage;
