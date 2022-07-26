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
    console.log(error);

    const message = ERROR_MESSAGE[code];
    console.log(message);
    renderSnackBar({
      message,
      status: 'ERROR',
      linkText: '문의하기',
      linkTo: CLIENT_PATH.VOC,
    });

    if (code === 2002) {
      authApiClient.deleteAuth();
      setIsLogin(false);
    }
  };

  return queryErrorHandler;
};

export default useQueryErrorHandler;
