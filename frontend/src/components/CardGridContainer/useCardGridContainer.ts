import { useGetMyChallenges } from 'apis';

const useCardGridContainer = () => {
  const {
    data: myChallengeInfiniteData,
    hasNextPage,
    fetchNextPage,
    isFetching,
  } = useGetMyChallenges({
    refetchOnWindowFocus: false,
  });

  return { myChallengeInfiniteData, hasNextPage, fetchNextPage, isFetching };
};

export default useCardGridContainer;
