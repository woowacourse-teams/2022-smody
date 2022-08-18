import { UseErrorFallbackSubscriptionButtonProps } from './type';
import { authApiClient } from 'apis/apiClient';
import { useEffect } from 'react';
import { useSetRecoilState } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import useSnackBar from 'hooks/useSnackBar';

import { CLIENT_PATH } from 'constants/path';

export const useErrorFallbackSubscriptionButton = ({
  errorCode,
  errorMessage,
}: UseErrorFallbackSubscriptionButtonProps) => {
  const renderSnackBar = useSnackBar();
  const setIsLogin = useSetRecoilState(isLoginState);

  useEffect(() => {
    renderSnackBar({
      message: errorMessage,
      status: 'ERROR',
      linkText: '문의하기',
      linkTo: CLIENT_PATH.VOC,
    });

    // 로그아웃 처리
    if (errorCode === 2002 || errorCode === 2003 || errorCode === 4001) {
      authApiClient.deleteAuth();
      setIsLogin(false);
    }
  }, [errorCode, errorMessage]);

  const handleClickToggleButton = () => {};

  return {
    handleClickToggleButton,
  };
};
