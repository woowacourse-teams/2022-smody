import { useGetAllChallenges } from 'apis';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import useSnackBar from 'hooks/useSnackBar';

import { CLIENT_PATH } from 'constants/path';

export const useSearchPage = () => {
  const isLogin = useRecoilValue(isLoginState);
  const renderSnackBar = useSnackBar();
  const navigate = useNavigate();

  const {
    isFetching,
    data: challengeInfiniteData,
    hasNextPage,
    fetchNextPage,
  } = useGetAllChallenges({
    refetchOnWindowFocus: false,
  });

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

  return {
    isFetching,
    challengeInfiniteData,
    hasNextPage,
    fetchNextPage,
    handleCreateChallengeButton,
  };
};
