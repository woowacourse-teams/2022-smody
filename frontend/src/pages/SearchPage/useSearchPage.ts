import { useGetAllChallenges } from 'apis';

export const useSearchPage = () => {
  const {
    isFetching,
    data: challengeInfiniteData,
    hasNextPage,
    fetchNextPage,
  } = useGetAllChallenges({
    refetchOnWindowFocus: false,
  });

  return {
    isFetching,
    challengeInfiniteData,
    hasNextPage,
    fetchNextPage,
  };
};
