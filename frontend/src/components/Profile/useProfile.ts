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
  const { data: myInfo } = useGetMyInfo();
  const { data: myCyclesStat } = useGetMyCyclesStat();

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
  };
};

export default useProfile;
