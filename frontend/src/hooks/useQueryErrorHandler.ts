import { authApiClient } from 'apis/apiClient';
import { AxiosError } from 'axios';
import { ErrorResponse } from 'commonType';
import { useSetRecoilState } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import useSnackBar from 'hooks/useSnackBar';

import { ERROR_MESSAGE } from 'constants/message';
import { CLIENT_PATH } from 'constants/path';

const useQueryErrorHandler = () => {
  const renderSnackBar = useSnackBar();
  const setIsLogin = useSetRecoilState(isLoginState);

  const queryErrorHandler = (error: AxiosError<ErrorResponse>): void => {
    if (typeof error.response === 'undefined') {
      return;
    }

    const { code } = error.response.data;

    const message = ERROR_MESSAGE[code];

    renderSnackBar({
      message: message ?? '알 수 없는 오류가 발생했습니다',
      status: 'ERROR',
      linkText: '문의하기',
      linkTo: CLIENT_PATH.VOC,
    });

    // 로그아웃 처리
    if (code === 2002) {
      authApiClient.deleteAuth();
      setIsLogin(false);
    }
  };

  return queryErrorHandler;
};

export default useQueryErrorHandler;
