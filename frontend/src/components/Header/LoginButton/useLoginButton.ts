import { useLocation } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import useAuth from 'hooks/useAuth';
import useSnackBar from 'hooks/useSnackBar';

export const useLoginButton = () => {
  const { redirectGoogleLoginLink } = useAuth();
  const isLogin = useRecoilValue(isLoginState);
  const renderSnackbar = useSnackBar();
  const { pathname } = useLocation();

  const handleClickLoginButton = () => {
    redirectGoogleLoginLink();
  };

  const handleClickErrorFallbackLoginButton = () => {
    renderSnackbar({
      status: 'ERROR',
      message: '로그인 요청 시 에러가 발생했습니다. 새로 고침을 해주세요.',
    });
  };

  return {
    isLogin,
    pathname,
    handleClickLoginButton,
    handleClickErrorFallbackLoginButton,
  };
};
