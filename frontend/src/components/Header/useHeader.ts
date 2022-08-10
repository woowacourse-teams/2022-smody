import { useGetLinkGoogle } from 'apis';
import { useState } from 'react';
import { useRecoilState, useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';
import { isDarkState } from 'recoil/darkMode/atoms';

export const useHeader = () => {
  const [isSubscribed, setIsSubscribed] = useState(false);

  const [isDark, setIsDark] = useRecoilState(isDarkState);

  const isLogin = useRecoilValue(isLoginState);

  const { refetch: redirectGoogleLoginLink } = useGetLinkGoogle();

  const handleDarkToggle = () => {
    localStorage.setItem('isDark', JSON.stringify(!isDark));
    setIsDark((prev) => !prev);
  };

  const handleSubscriptionButton = () => {
    setIsSubscribed((prev) => !prev);
  };

  const handleLoginButton = () => {
    redirectGoogleLoginLink();
  };

  return {
    isDark,
    isLogin,
    handleDarkToggle,
    handleLoginButton,
    isSubscribed,
    handleSubscriptionButton,
  };
};
