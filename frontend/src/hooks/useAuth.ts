import { useGetLinkGoogle, useGetMyInfo, useGetTokenGoogle } from 'apis';
import { authApiClient } from 'apis/apiClient';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';
import { getUrlParameter } from 'utils';

import useSnackBar from 'hooks/useSnackBar';

import { CLIENT_PATH } from 'constants/path';

const useAuth = () => {
  const navigate = useNavigate();
  const renderSnackBar = useSnackBar();
  const isLogin = useRecoilValue(isLoginState);
  const { isLoading: isLoadingMyInfo, refetch: refetchMyInfo } = useGetMyInfo();

  const { refetch: redirectGoogleLoginLink } = useGetLinkGoogle({
    onSuccess: ({ data: redirectionUrl }) => {
      window.location.href = redirectionUrl;
    },
  });

  const googleAuthCode = getUrlParameter('code');
  const { refetch: getTokenGoogle } = useGetTokenGoogle(googleAuthCode, {
    onSuccess: ({ data: { accessToken } }) => {
      authApiClient.updateAuth(accessToken);
      refetchMyInfo();
      navigate(CLIENT_PATH.CERT);
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

  return { redirectGoogleLoginLink, isLoadingMyInfo, isLogin };
};

export default useAuth;
