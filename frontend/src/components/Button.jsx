const Button = ({ children, onClick, type = 'button', variant = 'primary', size = 'md', disabled = false, className = '' }) => {
  const base = 'inline-flex items-center justify-center font-medium rounded-lg transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2';
  const sizes = { sm: 'px-3 py-1.5 text-xs', md: 'px-4 py-2 text-sm', lg: 'px-6 py-3 text-base' };
  const variants = {
    primary: 'bg-primary text-white hover:bg-primary-hover focus:ring-primary',
    secondary: 'bg-gray-200 text-gray-900 hover:bg-gray-300 focus:ring-gray-400',
    danger: 'bg-red-600 text-white hover:bg-red-700 focus:ring-red-500',
    outline: 'border border-border text-text-primary bg-transparent hover:bg-bg focus:ring-primary',
    ghost: 'text-text-primary hover:bg-gray-100 focus:ring-gray-400',
  };
  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={[base, sizes[size] || sizes.md, variants[variant] || variants.primary, disabled ? 'opacity-50 cursor-not-allowed' : '', className].join(' ')}
    >
      {children}
    </button>
  );
};
export default Button;