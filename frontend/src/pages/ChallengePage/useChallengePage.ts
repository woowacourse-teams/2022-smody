import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import useSnackBar from 'hooks/useSnackBar';

import { CLIENT_PATH } from 'constants/path';

export const useChallengePage = () => {
  const renderSnackBar = useSnackBar();

  const navigate = useNavigate();
  const isLogin = useRecoilValue(isLoginState);

  const handleCreateChallengeButton = () => {
    if (!isLogin) {
      renderSnackBar({
        message: '로그인이 필요한 서비스입니다.',
        status: 'ERROR',
      });

      return;
    }

    navigate(CLIENT_PATH.CHALLENGE_CREATE);
  };

  const handleEventPageButton = () => {
    navigate(CLIENT_PATH.EVENT);
  };

  const handleSearchPageButton = () => {
    navigate(CLIENT_PATH.SEARCH);
  };
  return {
    handleCreateChallengeButton,
    handleEventPageButton,
    handleSearchPageButton,
  };
};
