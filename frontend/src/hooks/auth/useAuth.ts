import { useGetMyInfo, useGetTokenGoogle } from 'apis';
import { authApiClient } from 'apis/apiClient';
import { useEffect } from 'react';
import { useRecoilState } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';
import { getUrlParameter } from 'utils';

import useLogout from 'hooks/auth/useLogout';
import useSnackBar from 'hooks/useSnackBar';

const useAuth = () => {
  const renderSnackBar = useSnackBar();
  const [isLogin, setIsLogin] = useRecoilState(isLoginState);
  const logoutByError = useLogout();
  const googleAuthCode = getUrlParameter('code');

  const { refetch: getTokenGoogle } = useGetTokenGoogle(googleAuthCode, {
    refetchOnWindowFocus: false,
    enabled: false,
    onSuccess: ({ data: { accessToken } }) => {
      authApiClient.updateAuth(accessToken);
      setIsLogin(true);

      renderSnackBar({
        message: '환영합니다 🎉 오늘 도전도 화이팅!',
        status: 'SUCCESS',
      });
    },
  });

  const { isLoading } = useGetMyInfo({
    refetchOnMount: false,
    refetchOnWindowFocus: false,
    refetchOnReconnect: false,
    onSuccess: () => {
      setIsLogin(true);
    },
    onError: (error) => {
      logoutByError(error);
    },
  });

  useEffect(() => {
    if (googleAuthCode.length === 0) {
      return;
    }
    getTokenGoogle();
  }, []);

  return { isLogin, isLoading };
};

export default useAuth;
