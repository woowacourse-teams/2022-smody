import styled from 'styled-components';

import useSnackBar from 'hooks/useSnackBar';

import { useSearchPage } from 'pages/SearchPage/useSearchPage';

import {
  LoadingSpinner,
  FlexBox,
  InfiniteScroll,
  SearchBar,
  ChallengeList,
  ChallengeItem,
  SROnly,
} from 'components';

const SearchPage = () => {
  const renderSnackBar = useSnackBar();

  const {
    isFetching,
    challengeInfiniteData,
    hasNextPage,
    searchInput,
    handleChangeSearch,
    handleClickSearchButton,
    fetchNextPage,

    isError,
    savedChallenges,
  } = useSearchPage();

  if (isError) {
    const errorMessage = !navigator.onLine
      ? '네트워크가 오프라인입니다. 이전에 캐싱된 데이터가 표시됩니다.'
      : '서버 에러가 발생했습니다. 이전에 캐싱된 데이터가 표시됩니다';
    renderSnackBar({
      status: 'ERROR',
      message: errorMessage,
    });

    return (
      <FlexBox as="ul" flexDirection="column" gap="27px">
        {savedChallenges.map((challengeInfo) => (
          <li key={challengeInfo.challengeId}>
            <ChallengeItem {...challengeInfo} />
          </li>
        ))}
      </FlexBox>
    );
  }

  if (typeof challengeInfiniteData === 'undefined') {
    return null;
  }

  return (
    <SearchContentWrapper
      flexDirection="column"
      aria-label="챌린지 검색 페이지"
      as="section"
    >
      <SROnly as="h1">챌린지를 검색할 수 있습니다.</SROnly>
      <SearchBar
        searchInput={searchInput}
        handleChangeSearch={handleChangeSearch}
        handleClickSearchButton={handleClickSearchButton}
      />
      <InfiniteScroll
        loadMore={fetchNextPage}
        hasMore={hasNextPage}
        isFetching={isFetching}
        loader={<LoadingSpinner />}
      >
        <ChallengeList challengeInfiniteData={challengeInfiniteData.pages} />
      </InfiniteScroll>
    </SearchContentWrapper>
  );
};

export default SearchPage;

const SearchContentWrapper = styled(FlexBox)`
  margin-top: 50px;
`;
