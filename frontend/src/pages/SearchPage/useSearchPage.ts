import { useGetAllChallenges } from 'apis';
import { indexedDB, saveDataToCache } from 'pwa/indexedDB';
import { useEffect, useState, useRef } from 'react';
import { AdditionalChallengeInfo } from 'types/challenge';

import useDebounce from 'hooks/useDebounce';
import useSnackBar from 'hooks/useSnackBar';

import { MAX_CHALLENGE_NAME_LENGTH } from 'constants/domain';
import { EMPTY_REGEX_RULE } from 'constants/regex';
import { INDEXED_DB } from 'constants/storage';

const checkBlankSpaceValue = (value: string) =>
  value.length !== 0 && value.replace(EMPTY_REGEX_RULE, '').length === 0;

const checkIsExceedMaxLength = (value: string) =>
  value.length > MAX_CHALLENGE_NAME_LENGTH;

export const useSearchPage = () => {
  const flagCheck = useRef(false);
  const renderSnackBar = useSnackBar();

  const searchInput = useRef<HTMLInputElement>(null);
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
        const challenges = data.pages[0].data;
        saveDataToCache(INDEXED_DB.CHALLENGE, data.pages.length, challenges);
      },
    },
  );

  const { debounce: debounceSetSearchValue } = useDebounce();
  const [savedChallenges, setSavedChallenges] = useState<AdditionalChallengeInfo[]>([]);

  useEffect(() => {
    if (!isError) {
      return;
    }
    indexedDB.getPosts(INDEXED_DB.CHALLENGE).then((challenges) => {
      setSavedChallenges(challenges);
    });
  }, [isError]);

  useEffect(() => {
    if (!flagCheck.current) {
      flagCheck.current = true;
      return;
    }
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

  const checkSearchValueValid = (value: string) => {
    if (checkBlankSpaceValue(value)) {
      renderSnackBar({
        status: 'ERROR',
        message: '검색어를 입력해주세요.',
      });

      return false;
    }

    if (checkIsExceedMaxLength(value)) {
      renderSnackBar({
        status: 'ERROR',
        message: '검색어는 30자 이내로 입력해주세요.',
      });

      return false;
    }

    return true;
  };

  return {
    isFetching,
    challengeInfiniteData,
    hasNextPage,
    searchInput,
    handleChangeSearch,
    handleClickSearchButton,
    fetchNextPage,

    savedChallenges,
    isError,
  };
};
