import { useGetMyCyclesInProgress } from 'apis';
import { indexedDB } from 'pwa/indexedDB';
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
      indexedDB.clearCycle().then(() => {
        for (const cycle of cycles) {
          indexedDB.saveCycle(cycle);
        }
      });
    },
  });

  const [savedCycles, setSavedCycles] = useState([]);

  useEffect(() => {
    if (!isError) {
      return;
    }
    indexedDB.getCycles().then((cycles) => {
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
