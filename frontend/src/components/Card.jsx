const Card = ({ children, className = '' }) => (
  <div className={'bg-surface border border-border shadow-sm rounded-lg ' + className}>{children}</div>
);
export default Card;
