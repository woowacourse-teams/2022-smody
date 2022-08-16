import { Navigate } from 'react-router-dom';

import useAuth from 'hooks/useAuth';

import { LoadingSpinner } from 'components/LoadingSpinner';

import { CLIENT_PATH } from 'constants/path';

export const LandingNavigation = () => {
  const { isLogin, isLoadingMyInfo } = useAuth();

  if (isLoadingMyInfo) return <LoadingSpinner />;

  if (isLogin) return <Navigate to={CLIENT_PATH.CERT} />;

  return <Navigate to={CLIENT_PATH.HOME} />;
};
