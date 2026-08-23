const Loader = ({ className = '' }) => (
  <div className={'flex justify-center items-center py-12 ' + className}>
    <div className="animate-spin rounded-full h-10 w-10 border-2 border-border border-t-primary"></div>
  </div>
);
export default Loader;
