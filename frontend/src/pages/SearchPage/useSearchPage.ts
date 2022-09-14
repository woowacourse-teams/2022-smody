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
const MAX_VALUE_LENGTH = 30;

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

  const { debounce: debounceSetSearchValue } = useDebounce();
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

  const handleClickSearchButton = () => {
    const currentValue = searchInput.current?.value;

    if (currentValue === undefined) {
      return;
    }

    if (!checkSearchValueValid(currentValue)) {
      return;
    }

    refetch();
  };

  const handleChangeSearch = () => {
    const currentValue = searchInput.current?.value;

    if (currentValue === undefined) {
      return;
    }

    if (!checkSearchValueValid(currentValue)) {
      return;
    }

    debounceSetSearchValue(() => {
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

  const checkSearchValueValid = (value: string) => {
    const checkingResult = { isValid: true, message: '' };

    if (checkBlankSpaceValue(value)) {
      checkingResult.isValid = false;
      checkingResult.message = '검색어를 입력해주세요.';
    }

    if (checkValueLength(value)) {
      checkingResult.isValid = false;
      checkingResult.message = '검색어는 30자 이내로 입력해주세요.';
    }

    if (!checkingResult.isValid) {
      renderSnackBar({ status: 'ERROR', message: checkingResult.message });
      return false;
    }

    return true;
  };

  const checkBlankSpaceValue = (value: string) =>
    value.length !== 0 && value.replace(EMPTY_REGEX_RULE, '').length === 0;

  const checkValueLength = (value: string) => value.length > MAX_VALUE_LENGTH;

  return {
    isFetching,
    challengeInfiniteData,
    hasNextPage,
    searchInput,
    handleChangeSearch,
    handleClickSearchButton,
    fetchNextPage,
    handleCreateChallengeButton,
    savedChallenges,
    isError,
  };
};
