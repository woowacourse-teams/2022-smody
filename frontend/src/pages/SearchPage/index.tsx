import { useSearchPage } from 'pages/SearchPage/useSearchPage';

import { LoadingSpinner, ChallengeItem, FlexBox, InfiniteScroll } from 'components';

const SearchPage = () => {
  const { isFetching, challengeInfiniteData, hasNextPage, fetchNextPage } =
    useSearchPage();

  return (
    <FlexBox flexDirection="column">
      <InfiniteScroll
        loadMore={fetchNextPage}
        hasMore={hasNextPage}
        isFetching={isFetching}
        loader={<LoadingSpinner />}
      >
        <FlexBox as="ul" flexDirection="column" gap="27px">
          {challengeInfiniteData?.pages.map((page) =>
            page?.data.map((challengeInfo) => (
              <li key={challengeInfo.challengeId}>
                <ChallengeItem {...challengeInfo} />
              </li>
            )),
          )}
        </FlexBox>
      </InfiniteScroll>
    </FlexBox>
  );
};

export default SearchPage;
