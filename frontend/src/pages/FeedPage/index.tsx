import { Feed } from 'commonType';
import styled from 'styled-components';

import useSnackBar from 'hooks/useSnackBar';

import { useFeedPage } from 'pages/FeedPage/useFeedPage';

import { FeedItem, InfiniteScroll, LoadingSpinner } from 'components';

const FeedPage = () => {
  const renderSnackBar = useSnackBar();

  const {
    feedInfiniteData,
    isFetching,
    hasNextPage,
    fetchNextPage,
    isError,
    savedFeeds,
  } = useFeedPage();

  if (isError) {
    const errorMessage = !navigator.onLine
      ? '네트워크가 오프라인 상태입니다. 이전에 캐싱된 데이터가 표시됩니다.'
      : '서버 에러가 발생했습니다. 이전에 캐싱된 데이터가 표시됩니다';
    renderSnackBar({
      status: 'ERROR',
      message: errorMessage,
    });

    return (
      <FeedContainer as="ul">
        {savedFeeds.map((feedInfo: Feed) => (
          <li key={feedInfo.cycleDetailId}>
            <FeedItem {...feedInfo} />
          </li>
        ))}
      </FeedContainer>
    );
  }

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
      <FeedContainer as="ul">
        {feedInfiniteData?.pages.map((page) =>
          page?.data.map((feedInfo) => (
            <li key={feedInfo.cycleDetailId}>
              <FeedItem {...feedInfo} />
            </li>
          )),
        )}
      </FeedContainer>
    </InfiniteScroll>
  );
};

export default FeedPage;

const FeedContainer = styled.div`
  display: grid;
  grid-gap: 16px;
  justify-content: center;

  @media all and (min-width: 1761px) {
    grid-template-columns: repeat(4, minmax(370px, max-content));
  }

  @media all and (min-width: 1321px) and (max-width: 1760px) {
    grid-template-columns: repeat(3, minmax(370px, max-content));
  }

  @media all and (min-width: 881px) and (max-width: 1320px) {
    grid-template-columns: repeat(2, minmax(370px, max-content));
  }

  @media all and (max-width: 880px) {
    grid-template-columns: repeat(1, minmax(370px, max-content));
  }

  @media all and (max-width: 366px) {
    grid-template-columns: 100%;
  }
`;
