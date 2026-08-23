import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { interviewApi } from '../api/interviewApi';
import Card from '../components/Card';
import Button from '../components/Button';
import Loader from '../components/Loader';
import EmptyState from '../components/EmptyState';
import ErrorMessage from '../components/ErrorMessage';

const StatusBadge = ({ status }) => {
  const s = { COMPLETED: 'bg-green-100 text-green-700', IN_PROGRESS: 'bg-amber-100 text-amber-700', PENDING: 'bg-gray-100 text-gray-600' };
  return <span className={'text-xs font-medium px-2 py-0.5 rounded-full ' + (s[status] || s.PENDING)}>{status.replace('_', ' ')}</span>;
};

const InterviewsPage = () => {
  const navigate = useNavigate();
  const [interviews, setInterviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetch = async () => {
    try { setLoading(true); const r = await interviewApi.getAllInterviews(); if (r.success) setInterviews(r.data || []); }
    catch { setError('Failed to load interviews'); } finally { setLoading(false); }
  };

  useEffect(() => { fetch(); }, []);

  const handleDelete = async id => {
    if (!confirm('Delete this interview and all its data?')) return;
    try { await interviewApi.deleteInterview(id); await fetch(); } catch { setError('Failed to delete interview'); }
  };

  if (loading) return <Loader />;

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-text-primary">My Interviews</h1>
          <p className="text-text-secondary text-sm mt-0.5">Manage and review your interview sessions</p>
        </div>
        <Button onClick={() => navigate('/interviews/create')}>+ Create Interview</Button>
      </div>
      {error && <div className="mb-4"><ErrorMessage message={error} /></div>}
      <Card className="p-6">
        {interviews.length === 0 ? (
          <EmptyState title="No interviews yet" description="Create your first interview to start practicing" actionText="Create Interview" onAction={() => navigate('/interviews/create')} />
        ) : (
          <div className="space-y-3">
            {interviews.map(iv => (
              <div key={iv.id} className="flex items-center justify-between p-4 border border-border rounded-lg hover:bg-bg transition-colors">
                <div className="flex-1 min-w-0 mr-4">
                  <div className="flex items-center gap-3 mb-1">
                    <h3 className="font-semibold text-text-primary truncate">{iv.jobTitle}</h3>
                    <StatusBadge status={iv.status} />
                  </div>
                  {iv.jobDescription && <p className="text-sm text-text-secondary line-clamp-1 mb-1">{iv.jobDescription}</p>}
                  <p className="text-xs text-text-secondary">Created {new Date(iv.createdAt).toLocaleDateString()}</p>
                </div>
                <div className="flex gap-2 flex-shrink-0">
                  <Button variant="outline" size="sm" onClick={() => navigate('/interviews/'+iv.id)}>View</Button>
                  <Button variant="danger" size="sm" onClick={() => handleDelete(iv.id)}>Delete</Button>
                </div>
              </div>
            ))}
          </div>
        )}
      </Card>
    </div>
  );
};
export default InterviewsPage;
