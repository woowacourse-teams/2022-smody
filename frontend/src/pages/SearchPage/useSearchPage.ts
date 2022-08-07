import { useGetAllChallenges } from 'apis';
import { useRef, RefObject, useMemo } from 'react';

import useIntersect from 'hooks/useIntersect';

export const useSearchPage = () => {
  const {
    isFetching,
    data,
    refetch: challengeListRefetch,
    hasNextPage,
    fetchNextPage,
  } = useGetAllChallenges({
    refetchOnWindowFocus: false,
  });

  const challengeListData = useMemo(() => {
    if (typeof data === 'undefined') {
      return null;
    }

    return data.pages
      .map((page) => {
        if (typeof page === 'undefined' || typeof page.data === 'undefined') {
          return [];
        }

        return page.data;
      })
      .flat();
  }, [data]);

  const rootRef = useRef() as RefObject<HTMLDivElement>;

  const options = useMemo(() => ({ root: rootRef.current, threshold: 0.5 }), []);

  const targetRef = useIntersect<HTMLLIElement>((entry, observer) => {
    if (hasNextPage) {
      fetchNextPage();
    }
    observer.unobserve(entry.target);
  }, options);

  return {
    rootRef,
    targetRef,
    isFetching,
    data,
    challengeListData,
    challengeListRefetch,
  };
};
