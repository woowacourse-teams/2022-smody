import { useGetLinkGoogle } from 'apis';
import { useRecoilState, useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';
import { isDarkState } from 'recoil/darkMode/atoms';

import useThemeContext from 'hooks/useThemeContext';

export const useHeader = () => {
  const [isDark, setIsDark] = useRecoilState(isDarkState);

  const isLogin = useRecoilValue(isLoginState);
  const themeContext = useThemeContext();

  const { refetch: redirectGoogleLoginLink } = useGetLinkGoogle();

  const handleDarkToggle = () => {
    localStorage.setItem('isDark', JSON.stringify(!isDark));
    setIsDark((prev) => !prev);
  };

  const handleLoginButton = () => {
    redirectGoogleLoginLink();
  };

  return {
    themeContext,
    isDark,
    isLogin,
    handleDarkToggle,
    handleLoginButton,
  };
};
