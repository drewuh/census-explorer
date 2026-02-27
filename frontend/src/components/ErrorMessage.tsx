interface ErrorMessageProps {
  onRetry: () => void;
}

function ErrorMessage({ onRetry }: ErrorMessageProps) {
  return (
    <div className="error-message">
      <p>Unable to load population data. Please try again.</p>
      <button type="button" onClick={onRetry}>
        Try again
      </button>
    </div>
  );
}

export default ErrorMessage;
