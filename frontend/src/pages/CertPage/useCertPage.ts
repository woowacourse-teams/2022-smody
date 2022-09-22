import { useGetMyCyclesInProgress } from 'apis';
import { indexedDB, saveDataToCache } from 'pwa/indexedDB';
import { useEffect, useState } from 'react';

const useCertPage = () => {
  const {
    data: cycleInfiniteData,
    isFetching,
    hasNextPage,
    fetchNextPage,
    isError,
  } = useGetMyCyclesInProgress({
    useErrorBoundary: false,
    onSuccess: (data) => {
      const cycles = data.pages[0].data;
      saveDataToCache('cycle', data.pages.length, cycles);
    },
  });

  const [savedCycles, setSavedCycles] = useState([]);

  useEffect(() => {
    if (!isError) {
      return;
    }
    indexedDB.getPosts('cycle').then((cycles) => {
      setSavedCycles(cycles);
    });
  }, [isError]);

  const getCycleCount = () => {
    if (typeof cycleInfiniteData === 'undefined') {
      return 0;
    }

    return cycleInfiniteData.pages.reduce((acc, page) => acc + page.data.length, 0);
  };

  return {
    cycleInfiniteData,
    isFetching,
    hasNextPage,
    fetchNextPage,
    getCycleCount,
    isError,
    savedCycles,
  };
};

export default useCertPage;
