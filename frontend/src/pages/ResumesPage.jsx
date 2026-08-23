import { useEffect, useState, useRef } from 'react';
import { resumeApi } from '../api/resumeApi';
import Card from '../components/Card';
import Button from '../components/Button';
import Loader from '../components/Loader';
import EmptyState from '../components/EmptyState';
import ErrorMessage from '../components/ErrorMessage';

const ResumesPage = () => {
  const [resumes, setResumes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState('');
  const [file, setFile] = useState(null);
  const fileRef = useRef(null);

  const fetchResumes = async () => {
    try { const r = await resumeApi.getAllResumes(); if (r.success) setResumes(r.data || []); }
    catch { setError('Failed to load resumes'); } finally { setLoading(false); }
  };

  useEffect(() => { fetchResumes(); }, []);

  const handleFileChange = e => { const f = e.target.files[0]; if (f) { setFile(f); setError(''); } };

  const handleUpload = async e => {
    e.preventDefault();
    if (!file) { setError('Please select a PDF file'); return; }
    setUploading(true); setError('');
    try {
      const r = await resumeApi.uploadResume(file);
      if (r.success) { setFile(null); if (fileRef.current) fileRef.current.value = ''; await fetchResumes(); }
      else setError(r.message || 'Upload failed');
    } catch { setError('Failed to upload resume'); } finally { setUploading(false); }
  };

  const handleDelete = async id => {
    if (!confirm('Delete this resume?')) return;
    try { await resumeApi.deleteResume(id); await fetchResumes(); } catch { setError('Failed to delete resume'); }
  };

  const handleDownload = async (id, fileName) => {
    try {
      const r = await resumeApi.downloadResume(id);
      const url = URL.createObjectURL(new Blob([r.data]));
      const a = Object.assign(document.createElement('a'), { href: url, download: fileName });
      document.body.appendChild(a); a.click(); a.remove(); URL.revokeObjectURL(url);
    } catch { setError('Failed to download resume'); }
  };

  if (loading) return <Loader />;

  return (
    <div>
      <h1 className="text-2xl font-bold text-text-primary mb-6">My Resumes</h1>
      <Card className="p-6 mb-6">
        <h2 className="font-semibold text-text-primary mb-4">Upload Resume</h2>
        {error && <div className="mb-3"><ErrorMessage message={error} /></div>}
        <form onSubmit={handleUpload} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-text-primary mb-1.5">Select PDF file</label>
            <input ref={fileRef} type="file" onChange={handleFileChange} accept=".pdf" className="w-full text-sm text-text-secondary border border-border rounded-lg px-3 py-2.5 cursor-pointer file:mr-3 file:py-1 file:px-3 file:rounded-md file:border-0 file:text-sm file:font-medium file:bg-blue-50 file:text-primary hover:file:bg-blue-100" />
            <p className="text-xs text-text-secondary mt-1">PDF files only. Max recommended size: 5MB.</p>
          </div>
          <Button type="submit" disabled={uploading || !file}>{uploading ? 'Uploading...' : 'Upload Resume'}</Button>
        </form>
      </Card>
      <Card className="p-6">
        <h2 className="font-semibold text-text-primary mb-4">Uploaded Resumes</h2>
        {resumes.length === 0 ? (
          <EmptyState title="No resumes yet" description="Upload your first resume to get personalized interview questions" />
        ) : (
          <div className="space-y-3">
            {resumes.map(r => (
              <div key={r.id} className="flex items-center gap-4 p-4 border border-border rounded-lg">
                <span className="text-2xl">📄</span>
                <div className="flex-1 min-w-0">
                  <p className="font-medium text-text-primary text-sm truncate">{r.fileName}</p>
                  <p className="text-xs text-text-secondary">{r.fileType || 'PDF'} · {new Date(r.createdAt).toLocaleDateString()}</p>
                </div>
                <div className="flex gap-2">
                  <Button variant="outline" size="sm" onClick={() => handleDownload(r.id, r.fileName)}>Download</Button>
                  <Button variant="danger" size="sm" onClick={() => handleDelete(r.id)}>Delete</Button>
                </div>
              </div>
            ))}
          </div>
        )}
      </Card>
    </div>
  );
};
export default ResumesPage;
