import { Navigate } from 'react-router-dom';

import { LoadingSpinner } from 'components/LoadingSpinner';

import { CLIENT_PATH } from 'constants/path';

export const LandingNavigation = ({
  isLogin,
  isLoading,
}: {
  isLogin: boolean;
  isLoading: boolean;
}) => {
  if (isLoading) return <LoadingSpinner />;

  return isLogin ? (
    <Navigate to={CLIENT_PATH.CERT} />
  ) : (
    <Navigate to={CLIENT_PATH.HOME} />
  );
};
