import { useGetNotifications } from 'apis/pushNotificationApi';
import { setBadge } from 'push/badge';
import { useEffect } from 'react';
import { useRecoilState, useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';
import { isDarkState } from 'recoil/darkMode/atoms';

import useAuth from 'hooks/useAuth';
import useSnackBar from 'hooks/useSnackBar';
import useSubscribe from 'hooks/useSubscribe';

const broadcast = new BroadcastChannel('push-channel');

export const useHeader = () => {
  const [isDark, setIsDark] = useRecoilState(isDarkState);

  const handleDarkToggle = () => {
    localStorage.setItem('isDark', JSON.stringify(!isDark));
    setIsDark((prev) => !prev);
  };

  return {
    isDark,
    handleDarkToggle,
  };
};

export const useHeaderRightButton = () => {
  const renderSnackBar = useSnackBar();
  const isLogin = useRecoilValue(isLoginState);

  const { redirectGoogleLoginLink } = useAuth();

  const { isSubscribed, subscribe, isLoadingSubscribe } = useSubscribe();

  const { data: notificationData, refetch } = useGetNotifications();
  const notifications = notificationData?.data;

  const badgeNumber = notifications?.length;

  useEffect(() => {
    setBadge(badgeNumber);
  }, [setBadge, badgeNumber]);

  const handleLoginButton = () => {
    redirectGoogleLoginLink();
  };

  broadcast.onmessage = (event) => {
    console.log('@@@', event.data.message);
    const message = event.data.message;
    refetch();
    renderSnackBar({
      status: 'SUCCESS',
      message: `[알림] ${message}`,
    });
  };

  return {
    isLogin,
    handleLoginButton,
    notifications,
    isSubscribed,
    subscribe,
    isLoadingSubscribe,
  };
};
