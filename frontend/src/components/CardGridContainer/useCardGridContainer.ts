import { useGetMySuccessChallenges } from 'apis';

const useCardGridContainer = () => {
  const { data, hasNextPage, fetchNextPage } = useGetMySuccessChallenges();

  const loadMore = () => {
    if (hasNextPage) {
      fetchNextPage();
    }
  };

  return { data, loadMore };
};

export default useCardGridContainer;
