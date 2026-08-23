const EmptyState = ({ title, description, actionText, onAction, icon }) => (
  <div className="text-center py-12 px-4">
    <div className="mx-auto w-12 h-12 rounded-full bg-blue-50 flex items-center justify-center mb-4">
      {icon || (
        <svg className="w-6 h-6 text-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
        </svg>
      )}
    </div>
    <h3 className="text-sm font-semibold text-text-primary mb-1">{title}</h3>
    {description && <p className="text-sm text-text-secondary mb-6">{description}</p>}
    {actionText && onAction && (
      <button
        onClick={onAction}
        className="inline-flex items-center px-4 py-2 bg-primary text-white text-sm font-medium rounded-lg hover:bg-primary-hover transition-colors focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2"
      >
        {actionText}
      </button>
    )}
  </div>
);
export default EmptyState;