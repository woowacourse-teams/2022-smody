import { useGetMyCyclesInProgress } from 'apis';

const useCertPage = () => {
  const {
    data: cycleInfiniteData,
    isFetching,
    hasNextPage,
    fetchNextPage,
  } = useGetMyCyclesInProgress({
    refetchOnWindowFocus: false,
  });

  const getCycleCount = () => {
    if (typeof cycleInfiniteData === 'undefined') {
      return 0;
    }

    return cycleInfiniteData.pages.reduce((acc, page) => acc + page.data.length, 0);
  };

  return { cycleInfiniteData, isFetching, hasNextPage, fetchNextPage, getCycleCount };
};

export default useCertPage;
