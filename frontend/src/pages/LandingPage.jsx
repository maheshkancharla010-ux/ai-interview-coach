import { Link } from 'react-router-dom';

const CheckIcon = () => (
  <svg className="w-5 h-5 text-green-500 flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
  </svg>
);

const LandingPage = () => (
  <div>
    <section className="bg-blue-50 py-20 px-4">
      <div className="max-w-4xl mx-auto">
        <div className="inline-flex items-center gap-2 bg-blue-100 text-primary text-sm font-medium px-3 py-1 rounded-full mb-6">AI-powered interview practice</div>
        <h1 className="text-5xl font-bold text-text-primary leading-tight mb-2">Practice smarter.</h1>
        <h1 className="text-5xl font-bold text-primary leading-tight mb-6">Interview better.</h1>
        <p className="text-lg text-text-secondary max-w-xl mb-8">Upload your resume, enter the job description, and get personalized interview questions with AI-generated feedback on every answer.</p>
        <div className="flex items-center gap-4 flex-wrap">
          <Link to="/register" className="px-6 py-3 bg-primary text-white font-semibold rounded-lg hover:bg-primary-hover transition-colors">Start Practicing — it's free</Link>
          <Link to="/login" className="px-6 py-3 border border-gray-300 text-text-primary font-semibold rounded-lg hover:bg-white transition-colors">Sign in</Link>
        </div>
        <p className="mt-3 text-sm text-text-secondary">No credit card required.</p>
      </div>
    </section>
    <section className="bg-white border-y border-border py-4">
      <div className="max-w-4xl mx-auto px-4 flex items-center justify-center gap-8 flex-wrap">
        {["Resume-personalized questions", "Instant AI scoring and feedback", "Track performance over time"].map(t => (
          <div key={t} className="flex items-center gap-2 text-sm text-text-secondary"><CheckIcon />{t}</div>
        ))}
      </div>
    </section>
    <section className="py-20 px-4 bg-white" id="how-it-works">
      <div className="max-w-5xl mx-auto">
        <p className="text-primary text-sm font-semibold uppercase tracking-wide mb-3">HOW IT WORKS</p>
        <h2 className="text-3xl font-bold text-text-primary mb-12">Four steps to confident interviews</h2>
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          {[
            {n:'01',t:'Upload Your Resume',d:'Add the resume you plan to submit for the role.'},
            {n:'02',t:'Enter the Job Details',d:'Paste the job title and description so the AI understands the context.'},
            {n:'03',t:'Start the Interview',d:'Answer one question at a time, just like a real interview.'},
            {n:'04',t:'Review Your Feedback',d:'Get a detailed score and improvement suggestions for every answer.'},
          ].map(step => (
            <div key={step.n}>
              <div className="text-6xl font-bold text-gray-100 mb-3 leading-none">{step.n}</div>
              <h3 className="text-base font-semibold text-text-primary mb-2">{step.t}</h3>
              <p className="text-sm text-primary">{step.d}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
    <section className="py-20 px-4 bg-bg" id="features">
      <div className="max-w-5xl mx-auto">
        <p className="text-primary text-sm font-semibold uppercase tracking-wide mb-3">FEATURES</p>
        <h2 className="text-3xl font-bold text-text-primary mb-12">Everything you need to prepare</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {[
            {icon:'📄',t:'Resume-Aware Questions',d:'Questions are generated based on your actual resume and the job description, making each practice session highly relevant.'},
            {icon:'⚡',t:'Instant AI Feedback',d:'Receive structured feedback on each answer — what worked, what to improve, and a score — so you can track progress.'},
            {icon:'📊',t:'Performance History',d:'Review all past sessions, compare scores across interviews, and see how your preparation is progressing over time.'},
            {icon:'🔒',t:'Secure & Private',d:'Your resume and interview data are stored securely. Nothing is shared without your consent.'},
          ].map(f => (
            <div key={f.t} className="bg-surface border border-border rounded-xl p-6">
              <div className="w-10 h-10 bg-blue-50 rounded-lg flex items-center justify-center text-xl mb-4">{f.icon}</div>
              <h3 className="text-base font-semibold text-text-primary mb-2">{f.t}</h3>
              <p className="text-sm text-text-secondary">{f.d}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
    <section className="bg-navy py-20 px-4 text-center">
      <div className="max-w-2xl mx-auto">
        <h2 className="text-3xl font-bold text-white mb-4">Ready to start preparing?</h2>
        <p className="text-gray-400 mb-8">Create an account, upload your resume, and start your first practice interview in minutes.</p>
        <Link to="/register" className="inline-block px-8 py-4 bg-primary text-white font-semibold rounded-lg hover:bg-primary-hover transition-colors text-base">Start Practicing — it's free</Link>
      </div>
    </section>
  </div>
);
export default LandingPage;
