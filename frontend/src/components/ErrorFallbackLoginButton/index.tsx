import { Button } from 'components';
import { ErrorFallbackLogInButtonProps } from 'components/ErrorFallbackLoginButton/type';

export const ErrorFallbackLogInButton = ({
  handleClickErrorFallbackLoginButton,
}: ErrorFallbackLogInButtonProps) => {
  return (
    <Button size="small" onClick={handleClickErrorFallbackLoginButton}>
      로그인
    </Button>
  );
};
