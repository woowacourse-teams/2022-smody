import { Navigate, Outlet } from 'react-router-dom';

import useAuth from 'hooks/useAuth';
import useSnackBar from 'hooks/useSnackBar';

import { LoadingSpinner } from 'components/LoadingSpinner';

import { CLIENT_PATH } from 'constants/path';

export const PrivateOutlet = () => {
  const { isLogin, isLoadingMyInfo } = useAuth();
  const renderSnackBar = useSnackBar();

  if (isLoadingMyInfo) return <LoadingSpinner />;

  if (!isLogin) {
    renderSnackBar({
      message: '로그인이 필요한 페이지입니다.',
      status: 'ERROR',
    });

    return <Navigate to={CLIENT_PATH.HOME} />;
  }

  return <Outlet />;
};
