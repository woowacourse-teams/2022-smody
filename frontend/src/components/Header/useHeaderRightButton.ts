import { useState } from 'react';
import { useLocation } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import useSnackBar from 'hooks/useSnackBar';

export const useHeaderRightButton = () => {
  const isLogin = useRecoilValue(isLoginState);
  const [notificationCount, setNotificationCount] = useState<number | undefined>(
    undefined,
  );
  const [isSubscribed, setIsSubscribed] = useState(false);

  const { pathname } = useLocation();
  const renderSnackbar = useSnackBar();

  const handleClickErrorFallbackLoginButton = () => {
    renderSnackbar({
      status: 'ERROR',
      message: '로그인 요청 시 에러가 발생했습니다. 새로 고침을 해주세요.',
    });
  };

  const updateNotificationCount = (updatedNotificationCount: number) => {
    setNotificationCount(updatedNotificationCount);
  };

  const updateIsSubscribed = (updatedIsSubscribed: boolean) => {
    setIsSubscribed(updatedIsSubscribed);
  };

  return {
    isLogin,
    pathname,
    notificationCount,
    isSubscribed,
    handleClickErrorFallbackLoginButton,
    updateNotificationCount,
    updateIsSubscribed,
  };
};
