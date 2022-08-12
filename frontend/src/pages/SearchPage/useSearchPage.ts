import { useGetAllChallenges } from 'apis';
import { FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import useInput from 'hooks/useInput';
import useSnackBar from 'hooks/useSnackBar';

import { CLIENT_PATH } from 'constants/path';

export const useSearchPage = () => {
  const isLogin = useRecoilValue(isLoginState);
  const renderSnackBar = useSnackBar();
  const navigate = useNavigate();

  const search = useInput('');

  const {
    isFetching,
    data: challengeInfiniteData,
    hasNextPage,
    fetchNextPage,
    refetch,
  } = useGetAllChallenges(
    { searchValue: search.value },
    {
      refetchOnWindowFocus: false,
    },
  );

  const handleSubmitSearch = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    if (search.value === '') {
      return;
    }

    refetch();
  };

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
    search,
    handleSubmitSearch,
    fetchNextPage,
    handleCreateChallengeButton,
  };
};
