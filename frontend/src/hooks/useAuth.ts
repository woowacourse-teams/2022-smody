import { useGetLinkGoogle, useGetTokenGoogle } from 'apis';
import { authApiClient } from 'apis/apiClient';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useRecoilState } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';
import { getUrlParameter } from 'utils';

import useSnackBar from 'hooks/useSnackBar';

import { CLIENT_PATH } from 'constants/path';

const useAuth = () => {
  const navigate = useNavigate();
  const renderSnackBar = useSnackBar();
  const [isLogin, setIsLogin] = useRecoilState(isLoginState);

  const { refetch: redirectGoogleLoginLink } = useGetLinkGoogle({
    onSuccess: ({ data: redirectionUrl }) => {
      window.location.href = redirectionUrl;
    },
  });

  const googleAuthCode = getUrlParameter('code');
  const { refetch: getTokenGoogle } = useGetTokenGoogle(googleAuthCode, {
    onSuccess: ({ data: { accessToken } }) => {
      authApiClient.updateAuth(accessToken);
      setIsLogin(true);
      navigate(CLIENT_PATH.FEED);
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

  return { redirectGoogleLoginLink, isLogin };
};

export default useAuth;
