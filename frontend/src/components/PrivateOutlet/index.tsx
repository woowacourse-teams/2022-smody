import { Navigate, Outlet } from 'react-router-dom';

import useSnackBar from 'hooks/useSnackBar';

import { LoadingSpinner } from 'components/LoadingSpinner';
import { PrivateOutletProps } from 'components/PrivateOutlet/type';

import { CLIENT_PATH } from 'constants/path';

export const PrivateOutlet = ({ isLogin, isLoading }: PrivateOutletProps) => {
  const renderSnackBar = useSnackBar();

  if (isLoading) return <LoadingSpinner />;

  if (!isLogin) {
    renderSnackBar({
      message: '로그인이 필요한 페이지입니다.',
      status: 'ERROR',
    });
  }

  return isLogin ? <Outlet /> : <Navigate to={CLIENT_PATH.HOME} />;
};
