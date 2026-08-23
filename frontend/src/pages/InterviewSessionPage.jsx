import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { interviewApi } from '../api/interviewApi';
import { questionApi } from '../api/questionApi';
import { feedbackApi } from '../api/feedbackApi';
import Card from '../components/Card';
import Button from '../components/Button';
import Loader from '../components/Loader';
import ErrorMessage from '../components/ErrorMessage';
import EmptyState from '../components/EmptyState';

const StatusBadge = ({ status }) => {
  const s = { COMPLETED: 'bg-green-100 text-green-700', IN_PROGRESS: 'bg-amber-100 text-amber-700', PENDING: 'bg-gray-100 text-gray-600' };
  return <span className={'text-xs font-medium px-2.5 py-1 rounded-full ' + (s[status] || s.PENDING)}>{status.replace('_', ' ')}</span>;
};

const InterviewSessionPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [interview, setInterview] = useState(null);
  const [questions, setQuestions] = useState([]);
  const [feedback, setFeedback] = useState([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [currentAnswer, setCurrentAnswer] = useState({});
  const [loading, setLoading] = useState(true);
  const [generatingQuestions, setGeneratingQuestions] = useState(false);
  const [generatingFeedback, setGeneratingFeedback] = useState(false);
  const [submittingAnswer, setSubmittingAnswer] = useState(false);
  const [error, setError] = useState('');

  const refreshFeedback = async () => {
    const fr = await feedbackApi.getFeedbackByInterview(id);
    if (fr.success) setFeedback(fr.data || []);
  };

  useEffect(() => {
    const init = async () => {
      try {
        const [ir, qr, fr] = await Promise.all([
          interviewApi.getInterviewById(id),
          questionApi.getQuestionsByInterview(id),
          feedbackApi.getFeedbackByInterview(id),
        ]);
        if (ir.success) setInterview(ir.data);
        if (qr.success) setQuestions(qr.data || []);
        if (fr.success) setFeedback(fr.data || []);
      } catch { setError('Failed to load interview data'); } finally { setLoading(false); }
    };
    init();
  }, [id]);

  const handleGenerateQuestions = async () => {
    setGeneratingQuestions(true); setError('');
    try {
      const r = await questionApi.generateQuestions(id);
      if (r.success) {
        setQuestions(r.data || []);
        setCurrentIndex(0);
        const ir = await interviewApi.getInterviewById(id);
        if (ir.success) setInterview(ir.data);
      } else setError(r.message || 'Failed to generate questions');
    } catch { setError('Failed to generate questions'); } finally { setGeneratingQuestions(false); }
  };

  const handleSubmitAnswer = async questionId => {
    const answer = (currentAnswer[questionId] || '').trim();
    if (!answer) { setError('Please write an answer before submitting'); return; }
    setSubmittingAnswer(true); setError('');
    try {
      const r = await questionApi.submitAnswer(questionId, { answerText: answer });
      if (r.success) {
        setQuestions(prev => prev.map(q => q.id === questionId ? r.data : q));
        setCurrentAnswer(prev => ({ ...prev, [questionId]: '' }));
        if (currentIndex < questions.length - 1) setCurrentIndex(i => i + 1);
      } else setError(r.message || 'Failed to submit answer');
    } catch { setError('Failed to submit answer'); } finally { setSubmittingAnswer(false); }
  };

  const handleGenerateFeedback = async (questionId = null) => {
    setGeneratingFeedback(true); setError('');
    try {
      let r;
      if (questionId) {
        // Per-question feedback: only generate, never update status
        r = await feedbackApi.generateFeedbackForQuestion(questionId);
      } else {
        // Interview-level feedback: backend sets COMPLETED, re-fetch interview
        r = await feedbackApi.generateFeedbackForInterview(id);
      }
      if (r.success) {
        await refreshFeedback();
        if (!questionId) {
          // Re-fetch interview to get authoritative status from backend
          const fresh = await interviewApi.getInterviewById(id);
          if (fresh.success) setInterview(fresh.data);
        }
      } else setError(r.message || 'Failed to generate feedback');
    } catch { setError('Failed to generate feedback'); } finally { setGeneratingFeedback(false); }
  };

  if (loading) return <Loader />;

  if (!interview) return <div className="p-8"><ErrorMessage message="Interview not found" /></div>;

  const currentQuestion = questions[currentIndex];
  const allAnswered = questions.length > 0 && questions.every(q => q.answerText);
  const hasFeedback = feedback.length > 0;
  const getFeedback = qId => feedback.find(f => f.questionId === qId);

  return (
    <div className="max-w-3xl">
      <button onClick={() => navigate('/interviews')} className="flex items-center gap-2 text-sm text-text-secondary hover:text-text-primary mb-6 transition-colors">← Back to Interviews</button>
      <div className="flex items-center gap-3 mb-2">
        <h1 className="text-2xl font-bold text-text-primary">{interview.jobTitle}</h1>
        <StatusBadge status={interview.status} />
      </div>
      {interview.jobDescription && <p className="text-text-secondary text-sm mb-6">{interview.jobDescription}</p>}

      {error && <div className="mb-4"><ErrorMessage message={error} /></div>}

      {questions.length === 0 ? (
        <Card className="p-8 text-center">
          <EmptyState title="No questions yet" description="Generate interview questions to start your practice session" />
          <Button onClick={handleGenerateQuestions} disabled={generatingQuestions} className="mt-4">{generatingQuestions ? 'Generating questions...' : 'Generate Questions'}</Button>
        </Card>
      ) : (
        <Card className="p-6">
          <div className="flex items-center justify-between mb-5">
            <div>
              <p className="text-xs text-text-secondary mb-0.5">{interview.jobTitle}</p>
              <p className="text-sm font-semibold text-text-primary">Question {currentIndex + 1} of {questions.length}</p>
            </div>
            <div className="flex items-center gap-1.5">
              {questions.map((q, i) => (
                <button key={q.id} onClick={() => setCurrentIndex(i)} className={[
                  'w-2.5 h-2.5 rounded-full transition-all',
                  i === currentIndex ? 'bg-primary w-4' : q.answerText ? 'bg-primary opacity-50' : 'border-2 border-border bg-transparent'
                ].join(' ')} />
              ))}
            </div>
          </div>

          {currentQuestion && (
            <div>
              <p className="text-text-primary mb-4 leading-relaxed">{currentQuestion.questionText}</p>
              {currentQuestion.answerText ? (
                <div className="bg-bg rounded-lg p-4 mb-4">
                  <p className="text-xs font-semibold text-text-secondary uppercase tracking-wide mb-2">Your Answer</p>
                  <p className="text-text-primary text-sm">{currentQuestion.answerText}</p>
                </div>
              ) : (
                <textarea value={currentAnswer[currentQuestion.id] || ''} onChange={e => setCurrentAnswer(p => ({ ...p, [currentQuestion.id]: e.target.value }))} placeholder="Your answer here..." rows={4} className="w-full px-3 py-2.5 rounded-lg border border-border bg-surface text-text-primary text-sm focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary resize-none mb-4" />
              )}
              {(() => { const fb = getFeedback(currentQuestion.id); return fb ? (
                <div className="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-4">
                  <div className="flex items-center justify-between mb-2">
                    <p className="text-xs font-semibold text-primary uppercase tracking-wide">AI Feedback</p>
                    <span className="bg-primary text-white text-xs font-bold px-2 py-0.5 rounded-full">{fb.score}/100</span>
                  </div>
                  <p className="text-sm text-text-primary">{fb.feedbackText}</p>
                </div>
              ) : null; })()}
            </div>
          )}

          <div className="flex items-center justify-between mt-4 pt-4 border-t border-border">
            <button onClick={() => setCurrentIndex(i => Math.max(i - 1, 0))} disabled={currentIndex === 0} className="px-4 py-2 text-sm font-medium border border-border rounded-lg hover:bg-bg disabled:opacity-40 disabled:cursor-not-allowed transition-colors">Previous</button>
            <div className="flex items-center gap-2">
              {currentQuestion && !currentQuestion.answerText && (
                <button onClick={() => handleSubmitAnswer(currentQuestion.id)} disabled={submittingAnswer || !((currentAnswer[currentQuestion.id] || '').trim())} className="px-4 py-2 bg-primary text-white text-sm font-medium rounded-lg hover:bg-primary-hover disabled:opacity-40 disabled:cursor-not-allowed transition-colors">{submittingAnswer ? 'Submitting...' : 'Submit Answer'}</button>
              )}
              {currentIndex < questions.length - 1 && (
                <button onClick={() => setCurrentIndex(i => Math.min(i + 1, questions.length - 1))} className="px-4 py-2 text-sm font-medium border border-border rounded-lg hover:bg-bg transition-colors">Next</button>
              )}
            </div>
          </div>

          {allAnswered && !hasFeedback && (
            <div className="mt-4 pt-4 border-t border-border text-center">
              <p className="text-sm text-text-secondary mb-3">All questions answered. Ready to see your results?</p>
              <Button onClick={() => handleGenerateFeedback()} disabled={generatingFeedback}>{generatingFeedback ? 'Generating feedback...' : 'Generate Feedback'}</Button>
            </div>
          )}
        </Card>
      )}

      {hasFeedback && (
        <Card className="p-6 mt-6">
          <h3 className="font-semibold text-text-primary mb-4">Interview Summary</h3>
          <div className="grid grid-cols-3 gap-4 mb-4">
            <div className="bg-bg rounded-lg p-4 text-center">
              <p className="text-xs text-text-secondary mb-1">Questions</p>
              <p className="text-2xl font-bold text-text-primary">{questions.length}</p>
            </div>
            <div className="bg-bg rounded-lg p-4 text-center">
              <p className="text-xs text-text-secondary mb-1">Avg Score</p>
              <p className="text-2xl font-bold text-primary">{feedback.length ? Math.round(feedback.reduce((s,f) => s + (f.score||0), 0) / feedback.length) : 'N/A'}</p>
            </div>
            <div className="bg-bg rounded-lg p-4 text-center">
              <p className="text-xs text-text-secondary mb-1">Status</p>
              <p className="text-2xl font-bold text-green-600">Done</p>
            </div>
          </div>
        </Card>
      )}
    </div>
  );
};
export default InterviewSessionPage;
