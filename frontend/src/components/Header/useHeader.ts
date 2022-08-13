import { useGetNotifications } from 'apis/pushNotificationApi';
import { useRecoilState, useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';
import { isDarkState } from 'recoil/darkMode/atoms';

import useAuth from 'hooks/useAuth';
import useSubscribe from 'hooks/useSubscribe';

export const useHeader = () => {
  const [isDark, setIsDark] = useRecoilState(isDarkState);

  const isLogin = useRecoilValue(isLoginState);

  const { redirectGoogleLoginLink } = useAuth();

  const { isSubscribed, subscribe, isLoadingSubscribe } = useSubscribe();

  const { data: notificationData } = useGetNotifications({ suspense: false });
  const notifications = notificationData?.data;

  const handleDarkToggle = () => {
    localStorage.setItem('isDark', JSON.stringify(!isDark));
    setIsDark((prev) => !prev);
  };

  const handleLoginButton = () => {
    redirectGoogleLoginLink();
  };

  return {
    isDark,
    isLogin,
    handleDarkToggle,
    handleLoginButton,
    notifications,
    isSubscribed,
    subscribe,
    isLoadingSubscribe,
  };
};
