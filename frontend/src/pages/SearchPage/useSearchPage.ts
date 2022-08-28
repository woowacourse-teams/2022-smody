import { useGetAllChallenges } from 'apis';
import { GetChallengeResponse } from 'apis/challengeApi/type';
import { indexedDB } from 'pwa/indexedDB';
import { FormEvent, useEffect, useState } from 'react';
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
    isError,
  } = useGetAllChallenges(
    { searchValue: search.value },
    {
      useErrorBoundary: false,
      onSuccess: (data) => {
        const challenges = data.pages[0].data;
        indexedDB.clearPost('challenge').then(() => {
          for (const challenge of challenges) {
            indexedDB.savePost('challenge', challenge);
            console.log('data', challenge);
          }
        });
      },
    },
  );

  const [savedChallenges, setSavedChallenges] = useState<GetChallengeResponse[]>([]);

  useEffect(() => {
    if (!isError) {
      return;
    }
    indexedDB.getPosts('challenge').then((challenges) => {
      setSavedChallenges(challenges);
    });
  }, [isError]);

  const handleSubmitSearch = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const emptyRegexRule = /\s/g;

    if (search.value.replace(emptyRegexRule, '') === '') {
      renderSnackBar({
        status: 'ERROR',
        message: '검색어를 입력해주세요',
      });

      return;
    }

    if (search.value.length > 30) {
      renderSnackBar({ status: 'ERROR', message: '검색어는 30자 이내로 입력해주세요' });
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
    savedChallenges,
    isError,
  };
};
