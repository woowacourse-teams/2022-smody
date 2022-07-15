import { apiClient } from 'apis/apiClient';
import { useEffect } from 'react';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import { CertAuth, CertUnAuth } from 'components';

const getUrlParameter = (name: string) => {
  name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
  const regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
  const results = regex.exec(window.location.search);
  return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
};

export const CertPage = () => {
  const getCallback = async (code: string) => {
    console.log('code?: ', code);
    const response = await apiClient.axios.get(`/oauth/login/google?code=${code}`);
    console.log(response);
  };

  useEffect(() => {
    const payload = getUrlParameter('code');
    if (payload.length === 0) {
      return;
    }
    console.log('hi');
    getCallback(payload);
  }, []);

  const isLogin = useRecoilValue(isLoginState);

  return isLogin ? <CertAuth /> : <CertUnAuth />;
};
