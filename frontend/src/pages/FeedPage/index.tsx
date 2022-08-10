import { useFeedPage } from 'pages/FeedPage/useFeedPage';

import { FlexBox, FeedItem, InfiniteScroll, LoadingSpinner } from 'components';

const FeedPage = () => {
  const { feedInfiniteData, isFetching, hasNextPage, fetchNextPage } = useFeedPage();

  if (feedInfiniteData === undefined) {
    return null;
  }

  return (
    <InfiniteScroll
      loadMore={fetchNextPage}
      hasMore={hasNextPage}
      isFetching={isFetching}
      loader={<LoadingSpinner />}
    >
      <FlexBox as="ul" flexDirection="column" alignItems="center">
        {feedInfiniteData?.pages.map((page) =>
          page?.data.map((feedInfo) => (
            <li key={feedInfo.cycleDetailId}>
              <FeedItem {...feedInfo} />
            </li>
          )),
        )}
      </FlexBox>
    </InfiniteScroll>
  );
};

export default FeedPage;
