import { useGetMyCyclesByChallengeId } from 'apis';
import { useParams } from 'react-router-dom';

export const CycleRecords = () => {
  const { challengeId } = useParams();

  const {
    data: cyclesByChallengeIdInfiniteData,
    isFetching,
    hasNextPage,
    fetchNextPage,
  } = useGetMyCyclesByChallengeId(
    { challengeId: Number(challengeId) },
    {
      refetchOnWindowFocus: false,
    },
  );

  if (typeof cyclesByChallengeIdInfiniteData === 'undefined') {
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
