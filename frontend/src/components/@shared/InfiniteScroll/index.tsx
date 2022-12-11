import { InfiniteScrollProps } from './type';
import { RefObject, useMemo, useRef, PropsWithChildren } from 'react';
import styled from 'styled-components';

import useIntersect, { OnIntersect } from 'hooks/useIntersect';

import { FlexBox } from 'components';

export const InfiniteScroll = ({
  children,
  loadMore,
  hasMore,
  isFetching,
  loader,
  threshold = 0.5,
}: PropsWithChildren<InfiniteScrollProps>) => {
  const rootRef = useRef() as RefObject<HTMLDivElement>;

  const onIntersect: OnIntersect = (entry, observer) => {
    if (hasMore) {
      loadMore();
    }
    observer.unobserve(entry.target);
  };
  const options = useMemo(() => ({ root: rootRef.current, threshold }), []);

  const targetRef = useIntersect<HTMLDivElement>(onIntersect, options);

  return (
    <Wrapper ref={rootRef} flexDirection="column">
      {children}
      <div ref={targetRef} />
      {isFetching && loader}
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox)`
  width: 100%;
`;
