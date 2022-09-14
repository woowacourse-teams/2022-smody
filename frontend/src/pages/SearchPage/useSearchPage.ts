import { useGetAllChallenges } from 'apis';
import { GetChallengeResponse } from 'apis/challengeApi/type';
import { indexedDB } from 'pwa/indexedDB';
import { FormEvent, useEffect, useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import useDebounce from 'hooks/useDebounce';
import useSnackBar from 'hooks/useSnackBar';

import { CLIENT_PATH } from 'constants/path';

const EMPTY_REGEX_RULE = /\s/g;

export const useSearchPage = () => {
  const isLogin = useRecoilValue(isLoginState);
  const renderSnackBar = useSnackBar();
  const navigate = useNavigate();

  const searchInput = useRef<null | HTMLInputElement>(null);
  const [searchValue, setSearchValue] = useState('');

  const {
    isFetching,
    data: challengeInfiniteData,
    hasNextPage,
    fetchNextPage,
    refetch,
    isError,
  } = useGetAllChallenges(
    { searchValue },
    {
      useErrorBoundary: false,
      onSuccess: (data) => {
        // TODO: 첫 번째 페이지만 존재할 때, indexDB에 데이터를 저장하도록 수정. 즉, 새로운 키워드를 검색했을 때만 indexDB에 데이터 추가
        const challenges = data.pages[0].data;
        indexedDB.clearPost('challenge').then(() => {
          for (const challenge of challenges) {
            indexedDB.savePost('challenge', challenge);
          }
        });
      },
    },
  );

  const { debounce: debounceRefetchAllChallenges } = useDebounce();
  const [savedChallenges, setSavedChallenges] = useState<GetChallengeResponse[]>([]);

  useEffect(() => {
    if (!isError) {
      return;
    }
    indexedDB.getPosts('challenge').then((challenges) => {
      setSavedChallenges(challenges);
    });
  }, [isError]);

  useEffect(() => {
    refetch();
  }, [searchValue]);

  // TODO: 키워드 입력 시 자동 검색 기능이 구현됐기 때문에, submit event handler는 필요하지 않다. 따라서 해당 함수는 제거할 필요가 있다.
  // 추가로, 검색 버튼을 클릭했을 때, 검색이 가능하도록 하기 위해 click event handler 구현이 필요하다.
  const handleSubmitSearch = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    if (searchInput.current === null) {
      return;
    }

    const currentValue = searchInput.current.value;

    if (
      currentValue.length !== 0 &&
      currentValue.replace(EMPTY_REGEX_RULE, '').length === 0
    ) {
      renderSnackBar({
        status: 'ERROR',
        message: '검색어를 입력해주세요',
      });
      return;
    }

    if (currentValue.length > 30) {
      renderSnackBar({ status: 'ERROR', message: '검색어는 30자 이내로 입력해주세요' });
      return;
    }

    refetch();
  };

  const handleChangeSearch = () => {
    if (searchInput.current === null) {
      return;
    }

    const currentValue = searchInput.current.value;

    if (
      currentValue.length !== 0 &&
      currentValue.replace(EMPTY_REGEX_RULE, '').length === 0
    ) {
      renderSnackBar({
        status: 'ERROR',
        message: '검색어를 입력해주세요',
      });
      return;
    }

    if (currentValue.length > 30) {
      renderSnackBar({ status: 'ERROR', message: '검색어는 30자 이내로 입력해주세요' });
      return;
    }

    debounceRefetchAllChallenges(() => {
      setSearchValue(currentValue);
    });
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
    searchInput,
    handleSubmitSearch,
    handleChangeSearch,
    fetchNextPage,
    handleCreateChallengeButton,
    savedChallenges,
    isError,
  };
};
