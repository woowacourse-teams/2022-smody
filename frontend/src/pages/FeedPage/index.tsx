import styled, { css } from 'styled-components';

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
      <FeedList as="ul" flexDirection="column" alignItems="center">
        {feedInfiniteData?.pages.map((page) =>
          page?.data.map((feedInfo) => (
            <li key={feedInfo.cycleDetailId}>
              <FeedItem {...feedInfo} />
            </li>
          )),
        )}
      </FeedList>
    </InfiniteScroll>
  );
};

export default FeedPage;

const FeedList = styled(FlexBox)`
  ${({ theme }) => css`
    & li::after {
      content: '';
      display: block;
      width: 100%;
      height: 20px;
      background-color: ${theme.secondary};
    }

    & li:last-child::after {
      display: none;
    }
  `}
`;
