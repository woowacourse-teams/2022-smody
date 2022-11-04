import { ErrorFallbackLogInButton } from '../ErrorFallbackLoginButton';
import { useLoginButton } from './useLoginButton';

import { Button } from 'components/@shared/Button';
import { ErrorBoundary } from 'components/@shared/ErrorBoundary';

export const LoginButton = () => {
  const { pathname, handleClickLoginButton, handleClickErrorFallbackLoginButton } =
    useLoginButton();

  return (
    <ErrorBoundary
      pathname={pathname}
      renderFallback={(renderFallbackParams) => (
        <ErrorFallbackLogInButton
          handleClickErrorFallbackLoginButton={handleClickErrorFallbackLoginButton}
        />
      )}
    >
      <Button size="small" onClick={handleClickLoginButton}>
        로그인
      </Button>
    </ErrorBoundary>
  );
};
