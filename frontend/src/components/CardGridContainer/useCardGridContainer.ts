import { useGetMyChallenges } from 'apis';
import { indexedDB } from 'pwa/indexedDB';
import { useEffect, useState } from 'react';

const useCardGridContainer = () => {
  const {
    data: myChallengeInfiniteData,
    hasNextPage,
    fetchNextPage,
    isFetching,
    isError,
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

  const [savedMyChallenges, setSavedMyChallenges] = useState([]);

  useEffect(() => {
    if (!isError) {
      return;
    }
    indexedDB.getPosts('myChallenge').then((myChallenges) => {
      setSavedMyChallenges(myChallenges);
    });
  }, [isError]);

  return {
    myChallengeInfiniteData,
    hasNextPage,
    fetchNextPage,
    isFetching,
    isError,
    savedMyChallenges,
  };
};

export default useCardGridContainer;
