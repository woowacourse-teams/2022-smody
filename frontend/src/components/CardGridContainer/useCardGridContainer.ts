import { useGetMySuccessChallenges } from 'apis';

const useCardGridContainer = () => {
  const {
    data: successChallengeInfiniteData,
    hasNextPage,
    fetchNextPage,
    isFetching,
  } = useGetMySuccessChallenges();

  return { successChallengeInfiniteData, hasNextPage, fetchNextPage, isFetching };
};

export default useCardGridContainer;
