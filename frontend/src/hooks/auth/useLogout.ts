import { authApiClient } from 'apis/apiClient';
import { AxiosError } from 'axios';
import { ErrorResponse } from 'commonType';
import { useSetRecoilState } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import useSnackBar from 'hooks/useSnackBar';

const useLogout = () => {
  const setIsLogin = useSetRecoilState(isLoginState);

  const renderSnackBar = useSnackBar();

  const logoutByError = (error: AxiosError<ErrorResponse>) => {
    if (typeof error.response === 'undefined') {
      return false;
    }

    const { code, message } = error.response.data;
    if (code === 2002) {
      authApiClient.deleteAuth();
      setIsLogin(false);

      renderSnackBar({
        message: '로그인이 필요한 서비스입니다',
        status: 'ERROR',
      });
      return true;
    }
    return false;
  };

  return logoutByError;
};

export default useLogout;
