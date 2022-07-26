import { useGetMyInfo, useGetTokenGoogle } from 'apis';
import { authApiClient } from 'apis/apiClient';
import { useEffect } from 'react';
import { useRecoilState } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';
import { getUrlParameter } from 'utils';

import { useManageAccessToken } from 'hooks/useManageAccessToken';
import useSnackBar from 'hooks/useSnackBar';

const useAuth = () => {
  const renderSnackBar = useSnackBar();
  const [isLogin, setIsLogin] = useRecoilState(isLoginState);
  const checkLogout = useManageAccessToken();
  const googleCode = getUrlParameter('code');

  const { refetch: getTokenGoogle } = useGetTokenGoogle(googleCode, {
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
    refetchOnWindowFocus: false,
    onSuccess: () => {
      setIsLogin(true);
    },
    onError: (error) => {
      checkLogout(error);
    },
  });

  useEffect(() => {
    if (googleCode.length === 0) {
      return;
    }

    getTokenGoogle();
  }, []);

  return { isLogin, isLoading };
};

export default useAuth;
