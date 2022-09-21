import { useGetMyChallenges } from 'apis';
import { indexedDB } from 'pwa/indexedDB';

const useCardGridContainer = () => {
  const {
    data: myChallengeInfiniteData,
    hasNextPage,
    fetchNextPage,
    isFetching,
  } = useGetMyChallenges({
    useErrorBoundary: false,
    onSuccess: (data) => {
      const myChallenges = data.pages[0].data;
      indexedDB.putPost('myChallenge', myChallenges);
    },
  });

  return {
    myChallengeInfiniteData,
    hasNextPage,
    fetchNextPage,
    isFetching,
  };
};

export default useCardGridContainer;
