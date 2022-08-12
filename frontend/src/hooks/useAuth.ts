import { useGetMyInfo, useGetTokenGoogle } from 'apis';
import { authApiClient } from 'apis/apiClient';
import { useEffect } from 'react';
import { useRecoilState } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';
import { getUrlParameter } from 'utils';

import useSnackBar from 'hooks/useSnackBar';

const removeQueryParams = () => {
  window.location.href = window.location.href.split('?')[0];
};

const useAuth = () => {
  const renderSnackBar = useSnackBar();
  const [isLogin, setIsLogin] = useRecoilState(isLoginState);

  const { isLoading, refetch: refetchGetMyInfo } = useGetMyInfo({
    refetchOnMount: false,
    refetchOnWindowFocus: false,
    refetchOnReconnect: false,
    suspense: false,
    onSuccess: () => {
      setIsLogin(true);
    },
    onError: () => {
      // error swallow
      return null;
    },
  });

  const googleAuthCode = getUrlParameter('code');

  const { refetch: getTokenGoogle } = useGetTokenGoogle(googleAuthCode, {
    refetchOnWindowFocus: false,
    enabled: false,
    onSuccess: ({ data: { accessToken } }) => {
      authApiClient.updateAuth(accessToken);
      refetchGetMyInfo();

      setIsLogin(true);
      removeQueryParams();

      renderSnackBar({
        message: '환영합니다 🎉 오늘 도전도 화이팅!',
        status: 'SUCCESS',
      });
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
