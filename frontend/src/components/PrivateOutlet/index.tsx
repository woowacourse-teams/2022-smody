import { Navigate, Outlet } from 'react-router-dom';

import useAuth from 'hooks/useAuth';
import useSnackBar from 'hooks/useSnackBar';

import { CLIENT_PATH } from 'constants/path';

export const PrivateOutlet = () => {
  const { isLogin } = useAuth();
  const renderSnackBar = useSnackBar();

  if (!isLogin) {
    renderSnackBar({
      message: '로그인이 필요한 페이지입니다.',
      status: 'ERROR',
    });

    return <Navigate to={CLIENT_PATH.HOME} />;
  }

  return <Outlet />;
};
