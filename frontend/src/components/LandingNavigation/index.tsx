import { Navigate } from 'react-router-dom';

import { LandingNavigationProps } from 'components/LandingNavigation/type';
import { LoadingSpinner } from 'components/LoadingSpinner';

import { CLIENT_PATH } from 'constants/path';

export const LandingNavigation = ({ isLogin, isLoading }: LandingNavigationProps) => {
  if (isLoading) return <LoadingSpinner />;

  if (isLogin) return <Navigate to={CLIENT_PATH.CERT} />;

  return <Navigate to={CLIENT_PATH.HOME} />;
};
