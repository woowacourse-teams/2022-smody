import styled, { css } from 'styled-components';

import useSnackBar from 'hooks/useSnackBar';

import { useSearchPage } from 'pages/SearchPage/useSearchPage';

import {
  LoadingSpinner,
  FlexBox,
  InfiniteScroll,
  SearchBar,
  ChallengeList,
  ChallengeItem,
  Button,
  FixedButton,
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
    handleCreateChallengeButton,
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
    <FlexBox flexDirection="column">
      <TabWrapper alignItems="center">
        <EventButton size="small">이벤트 페이지</EventButton>
      </TabWrapper>
      <SearchContentWrapper flexDirection="column">
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
      <FixedButton onClick={handleCreateChallengeButton}>챌린지 생성하기</FixedButton>
    </FlexBox>
  );
};

export default SearchPage;

const TabWrapper = styled(FlexBox)`
  ${({ theme }) => css`
    width: 100%;
    height: 50px;
    position: fixed;
    background-color: ${theme.background};
    margin-top: -7px;
  `}
`;

const SearchContentWrapper = styled(FlexBox)`
  margin-top: 110px;
  padding-bottom: 65px;
`;

const EventButton = styled(Button)`
  background-image: linear-gradient(to bottom right, #ffd700, orange);
`;
