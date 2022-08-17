import { useGetMyCyclesByChallengeId } from 'apis';
import { useParams } from 'react-router-dom';

export const useCycleRecords = () => {
  const { challengeId } = useParams();

  const {
    data: myCyclesInfiniteData,
    isFetching: isFetchingMyCycles,
    hasNextPage: hasNextPageMyCycles,
    fetchNextPage: fetchNextPageMyCycles,
  } = useGetMyCyclesByChallengeId(
    { challengeId: Number(challengeId) },
    {
      refetchOnWindowFocus: false,
    },
  );

  return {
    myCyclesInfiniteData,
    isFetchingMyCycles,
    hasNextPageMyCycles,
    fetchNextPageMyCycles,
  };
};
