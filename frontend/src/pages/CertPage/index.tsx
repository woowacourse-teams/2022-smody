import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import { CertAuth, CertUnAuth } from 'components';

export const CertPage = () => {
  const isLogin = useRecoilValue(isLoginState);

  return isLogin ? <CertAuth /> : <CertUnAuth />;
};
