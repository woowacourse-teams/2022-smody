import { Navigate } from 'react-router-dom';

import useAuth from 'hooks/useAuth';

import { CLIENT_PATH } from 'constants/path';

export const LandingNavigation = () => {
  const { isLogin } = useAuth();

  if (isLogin) return <Navigate to={CLIENT_PATH.CERT} />;

  return <Navigate to={CLIENT_PATH.HOME} />;
};
