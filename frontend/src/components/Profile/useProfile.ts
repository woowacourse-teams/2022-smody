import { useGetMyCyclesStat, useGetMyInfo } from 'apis';
import { authApiClient } from 'apis/apiClient';
import { MouseEventHandler } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import { CLIENT_PATH } from 'constants/path';

const useProfile = () => {
  const setIsLogin = useSetRecoilState(isLoginState);
  const navigate = useNavigate();
  const { data: myInfo, isError: isErrorMyInfo } = useGetMyInfo({
    useErrorBoundary: false,
  });
  const { data: myCyclesStat, isError: isErrorMyCyclesStat } = useGetMyCyclesStat({
    useErrorBoundary: false,
  });

  const handleClickEdit: MouseEventHandler<HTMLButtonElement> = () => {
    navigate(CLIENT_PATH.PROFILE_EDIT);
  };

  const handleClickLogout: MouseEventHandler<HTMLButtonElement> = () => {
    authApiClient.deleteAuth();
    setIsLogin(false);
    navigate(CLIENT_PATH.HOME);
  };

  return {
    myInfo,
    myCyclesStat,
    handleClickEdit,
    handleClickLogout,
    isErrorMyInfo,
    isErrorMyCyclesStat,
  };
};

export default useProfile;
