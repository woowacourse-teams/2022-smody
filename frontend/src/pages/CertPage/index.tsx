import { useGetTokenGoogle } from 'apis';
import { authApiClient } from 'apis/apiClient';
import { useEffect } from 'react';
import { useRecoilState } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';
import { getUrlParameter } from 'utils';

import { CertAuth, CertUnAuth } from 'components';

export const CertPage = () => {
  const [isLogin, setIsLogin] = useRecoilState(isLoginState);

  const googleCode = getUrlParameter('code');

  const { refetch: getTokenGoogle } = useGetTokenGoogle(googleCode, {
    enabled: false,
    onSuccess: ({ data: { accessToken } }) => {
      setIsLogin(true);

      authApiClient.updateAuth(accessToken);
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
