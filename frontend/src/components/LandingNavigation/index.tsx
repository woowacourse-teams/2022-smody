import { Navigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import { CLIENT_PATH } from 'constants/path';

export const LandingNavigation = () => {
  const isLogin = useRecoilValue(isLoginState);

  if (isLogin) return <Navigate to={CLIENT_PATH.CERT} />;

  return <Navigate to={CLIENT_PATH.HOME} />;
};
