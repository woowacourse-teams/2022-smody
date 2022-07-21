import { authApiClient } from 'apis/apiClient';
import { AxiosError } from 'axios';
import { ErrorResponse } from 'commonType';
import { useSetRecoilState } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

export const useManageAccessToken = () => {
  const setIsLogin = useSetRecoilState(isLoginState);
  const checkLogout = (error: AxiosError<ErrorResponse>) => {
    if (typeof error.response === 'undefined') {
      return false;
    }

    const { code, message } = error.response.data;
    if (code === 2002) {
      authApiClient.deleteAuth();
      setIsLogin(false);
      return true;
    }
    return false;
  };

  return checkLogout;
};
