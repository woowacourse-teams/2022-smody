import { useGetAllChallenges } from 'apis';
import { useRef, RefObject, useMemo } from 'react';

import useIntersect from 'hooks/useIntersect';

export const useSearchPage = () => {
  const { isFetching, data, refetch, hasNextPage, fetchNextPage } = useGetAllChallenges({
    refetchOnWindowFocus: false,
  });

  const rootRef = useRef() as RefObject<HTMLUListElement>;

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
    refetch,
  };
};
