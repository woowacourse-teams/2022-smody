import { SelectOptions } from './type';
import { useGetMyCyclesByChallengeId } from 'apis';
import { useParams } from 'react-router-dom';

export const useCycleRecordList = (selectOption: SelectOptions) => {
  const { challengeId } = useParams();

  const {
    data: myCyclesInfiniteData,
    isFetching: isFetchingMyCycles,
    hasNextPage: hasNextPageMyCycles,
    fetchNextPage: fetchNextPageMyCycles,
  } = useGetMyCyclesByChallengeId(
    { challengeId: Number(challengeId), filter: selectOption },
    {
      refetchOnWindowFocus: false,
    },
  );

  return {
    challengeId: Number(challengeId),
    myCyclesInfiniteData,
    isFetchingMyCycles,
    hasNextPageMyCycles,
    fetchNextPageMyCycles,
  };
};
