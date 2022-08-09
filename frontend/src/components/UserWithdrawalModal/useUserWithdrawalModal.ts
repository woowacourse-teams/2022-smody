import { UseUserWithdrawalModalProps } from './type';
import { useDeleteMyInfo } from 'apis';
import { authApiClient } from 'apis/apiClient';
import { MouseEventHandler } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import useInput from 'hooks/useInput';
import useSnackBar from 'hooks/useSnackBar';

import { CLIENT_PATH } from 'constants/path';

const useUserWithdrawalModal = ({ email }: UseUserWithdrawalModalProps) => {
  const navigate = useNavigate();
  const renderSnackBar = useSnackBar();
  const emailInput = useInput('');

  const setIsLogin = useSetRecoilState(isLoginState);

  const { mutate: deleteMyInfo } = useDeleteMyInfo({
    onSuccess: () => {
      renderSnackBar({
        message:
          '회원 정보를 성공적으로 삭제했습니다. 그동안 스모디를 이용해 주셔서 감사합니다.',
        status: 'SUCCESS',
      });

      authApiClient.deleteAuth();
      setIsLogin(false);

      navigate(CLIENT_PATH.HOME);
    },
  });

  const canWithdrawal = email === emailInput.value;

  const handleClickUserWithdrawal: MouseEventHandler<HTMLButtonElement> = () => {
    deleteMyInfo();
  };
  return { canWithdrawal, handleClickUserWithdrawal, emailInput };
};

export default useUserWithdrawalModal;
