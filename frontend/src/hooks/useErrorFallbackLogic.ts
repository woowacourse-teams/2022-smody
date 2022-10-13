import { authApiClient } from 'apis/apiClient';
import { useEffect } from 'react';
import { useSetRecoilState } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';
import { ErrorType } from 'types/internal';

import useSnackBar from 'hooks/useSnackBar';

import { CLIENT_PATH } from 'constants/path';

export const useErrorFallbackLogic = ({ errorCode, errorMessage }: ErrorType) => {
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
    if (
      errorCode === 2002 ||
      errorCode === 2003 ||
      errorCode === 4001 ||
      errorCode === 9001 ||
      errorCode === null
    ) {
      authApiClient.deleteAuth();
      setIsLogin(false);
    }
  }, [errorCode, errorMessage]);
};
