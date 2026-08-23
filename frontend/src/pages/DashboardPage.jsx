import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { interviewApi } from '../api/interviewApi';
import { resumeApi } from '../api/resumeApi';
import Card from '../components/Card';
import Loader from '../components/Loader';
import EmptyState from '../components/EmptyState';

const StatusBadge = ({ status }) => {
  const styles = { COMPLETED: 'bg-green-100 text-green-700', IN_PROGRESS: 'bg-amber-100 text-amber-700', PENDING: 'bg-gray-100 text-gray-600' };
  return <span className={'inline-flex px-2 py-0.5 rounded-full text-xs font-medium ' + (styles[status] || styles.PENDING)}>{status}</span>;
};

const DashboardPage = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [interviews, setInterviews] = useState([]);
  const [resumes, setResumes] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([interviewApi.getAllInterviews(), resumeApi.getAllResumes()])
      .then(([ir, rr]) => {
        if (ir.success) setInterviews(ir.data || []);
        if (rr.success) setResumes(rr.data || []);
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <Loader />;

  const recent = [...interviews].sort((a,b) => new Date(b.createdAt)-new Date(a.createdAt)).slice(0,5);
  const completed = interviews.filter(i => i.status === 'COMPLETED').length;

  return (
    <div>
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-text-primary">Welcome back, {user?.fullName?.split(' ')[0] || 'there'}!</h1>
        <p className="text-text-secondary mt-1">Here's an overview of your interview preparation.</p>
      </div>
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-8">
        {[{label:'Total Interviews',value:interviews.length,icon:'🎯'},{label:'Resumes',value:resumes.length,icon:'📄'},{label:'Completed',value:completed,icon:'✅'}].map(s => (
          <Card key={s.label} className="p-5">
            <div className="flex items-center gap-3">
              <span className="text-2xl">{s.icon}</span>
              <div>
                <p className="text-xs text-text-secondary font-medium uppercase tracking-wide">{s.label}</p>
                <p className="text-3xl font-bold text-text-primary">{s.value}</p>
              </div>
            </div>
          </Card>
        ))}
      </div>
      <div className="flex gap-3 mb-8">
        <Link to="/interviews/create" className="px-4 py-2 bg-primary text-white text-sm font-medium rounded-lg hover:bg-primary-hover transition-colors">+ New Interview</Link>
        <Link to="/resumes" className="px-4 py-2 border border-border text-text-primary text-sm font-medium rounded-lg hover:bg-bg transition-colors">Upload Resume</Link>
      </div>
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card className="p-5">
          <div className="flex justify-between items-center mb-4">
            <h2 className="font-semibold text-text-primary">Recent Interviews</h2>
            <Link to="/interviews" className="text-xs text-primary hover:underline">View all</Link>
          </div>
          {recent.length === 0 ? (
            <EmptyState title="No interviews yet" description="Create your first interview to get started" actionText="Create Interview" onAction={() => navigate('/interviews/create')} />
          ) : (
            <div className="space-y-2">
              {recent.map(iv => (
                <Link key={iv.id} to={'/interviews/'+iv.id} className="flex items-center justify-between p-3 rounded-lg border border-border hover:bg-bg transition-colors">
                  <div>
                    <p className="font-medium text-text-primary text-sm">{iv.jobTitle}</p>
                    <p className="text-xs text-text-secondary">{new Date(iv.createdAt).toLocaleDateString()}</p>
                  </div>
                  <StatusBadge status={iv.status} />
                </Link>
              ))}
            </div>
          )}
        </Card>
        <Card className="p-5">
          <div className="flex justify-between items-center mb-4">
            <h2 className="font-semibold text-text-primary">Your Resumes</h2>
            <Link to="/resumes" className="text-xs text-primary hover:underline">View all</Link>
          </div>
          {resumes.length === 0 ? (
            <EmptyState title="No resumes uploaded" description="Upload a resume to get personalized questions" actionText="Upload Resume" onAction={() => navigate('/resumes')} />
          ) : (
            <div className="space-y-2">
              {resumes.slice(0,5).map(r => (
                <div key={r.id} className="flex items-center gap-3 p-3 rounded-lg border border-border">
                  <span className="text-lg">📄</span>
                  <div className="flex-1 min-w-0">
                    <p className="text-sm font-medium text-text-primary truncate">{r.fileName}</p>
                    <p className="text-xs text-text-secondary">{new Date(r.createdAt).toLocaleDateString()}</p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </Card>
      </div>
    </div>
  );
};
export default DashboardPage;
