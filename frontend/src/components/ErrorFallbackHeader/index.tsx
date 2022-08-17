import { useErrorFallbackHeader } from './useErrorFallbackHeader';

import { Button, Dropdown, Bell } from 'components';

export const ErrorFallbackHeader = () => {
  const { isLogin, handleLoginButton } = useErrorFallbackHeader();

  if (isLogin) {
    return (
      <Dropdown
        disabled={true}
        button={<Bell count={undefined} isSubscribed={false} />}
      />
    );
  }

  return (
    <Button size="small" onClick={handleLoginButton}>
      로그인
    </Button>
  );
};
