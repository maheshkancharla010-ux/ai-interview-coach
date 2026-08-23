const Input = ({ label, type = 'text', name, value, onChange, placeholder, error, fieldError, required = false, className = '', ...rest }) => {
  const displayError = error || fieldError;
  return (
    <div className={className}>
      {label && (
        <label htmlFor={name} className="block text-sm font-medium text-text-primary mb-1.5">
          {label}{required && <span className="text-red-500 ml-1">*</span>}
        </label>
      )}
      <input
        type={type}
        name={name}
        id={name}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        required={required}
        className={[
          'w-full px-3 py-2.5 rounded-lg border bg-surface text-text-primary placeholder-gray-400',
          'focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary',
          'text-sm transition-colors',
          displayError ? 'border-red-400 focus:ring-red-400 focus:border-red-400' : 'border-border',
        ].join(' ')}
        {...rest}
      />
      {displayError && <p className="mt-1 text-xs text-red-600">{displayError}</p>}
    </div>
  );
};
export default Input;