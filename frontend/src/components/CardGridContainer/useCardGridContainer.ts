import { useGetMyChallenges } from 'apis';

const useCardGridContainer = () => {
  const {
    data: myChallengeInfiniteData,
    hasNextPage,
    fetchNextPage,
    isFetching,
  } = useGetMyChallenges();

  return { myChallengeInfiniteData, hasNextPage, fetchNextPage, isFetching };
};

export default useCardGridContainer;
