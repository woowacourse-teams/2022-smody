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
      indexedDB.clearPost('myChallenge').then(() => {
        for (const myChallenge of myChallenges) {
          indexedDB.savePost('myChallenge', myChallenge);
        }
      });
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
