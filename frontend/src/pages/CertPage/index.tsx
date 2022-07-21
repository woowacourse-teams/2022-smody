import { useGetTokenGoogle } from 'apis';
import { authApiClient } from 'apis/apiClient';
import { useEffect } from 'react';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/selectors';
import { getUrlParameter } from 'utils';

import useSnackBar from 'hooks/useSnackBar';

import { CertAuth, CertUnAuth } from 'components';

export const CertPage = () => {
  const renderSnackBar = useSnackBar();
  const isLogin = useRecoilValue(isLoginState);

  const googleCode = getUrlParameter('code');

  const { refetch: getTokenGoogle } = useGetTokenGoogle(googleCode, {
    enabled: false,
    onSuccess: ({ data: { accessToken } }) => {
      authApiClient.updateAuth(accessToken);

      renderSnackBar({
        message: '환영합니다 🎉 오늘 도전도 화이팅!',
        status: 'SUCCESS',
      });
    },
  });

  useEffect(() => {
    if (googleCode.length === 0) {
      return;
    }

    getTokenGoogle();
  }, []);

  return isLogin ? <CertAuth /> : <CertUnAuth />;
};
