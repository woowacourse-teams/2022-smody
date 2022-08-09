import { InfiniteScrollProps } from './type';
import { RefObject, useMemo, useRef } from 'react';

import useIntersect from 'hooks/useIntersect';

import { FlexBox } from 'components';

export const InfiniteScroll = ({
  children,
  loadMore,
  hasMore,
  isFetching,
  loader,
  threshold = 0.5,
}: InfiniteScrollProps) => {
  const rootRef = useRef() as RefObject<HTMLDivElement>;

  const options = useMemo(() => ({ root: rootRef.current, threshold }), []);

  const targetRef = useIntersect<HTMLDivElement>((entry, observer) => {
    if (hasMore) {
      loadMore();
    }
    observer.unobserve(entry.target);
  }, options);

  return (
    <FlexBox ref={rootRef} flexDirection="column">
      {children}
      <div ref={targetRef} />
      {isFetching && loader}
    </FlexBox>
  );
};
