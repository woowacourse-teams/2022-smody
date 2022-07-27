import { useGetAllChallenges } from 'apis';
import { useRef, RefObject, useMemo } from 'react';
import styled from 'styled-components';

import useIntersect from 'hooks/useIntersect';

import { FlexBox, ChallengeItem, LoadingSpinner } from 'components';
import { ChallengeInfo } from 'components/ChallengeList/type';

export const ChallengeList = () => {
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

  if (typeof data === 'undefined') {
    return <LoadingSpinner />;
  }

  return (
    <Wrapper as="ul" ref={rootRef}>
      {data.pages.map((page, pageIndex) => {
        if (typeof page === 'undefined' || typeof page.data === 'undefined') {
          return [];
        }

        return page.data.map((challengeInfo: ChallengeInfo, challengeIndex: number) => (
          <li
            key={challengeInfo.challengeId}
            ref={
              pageIndex === data.pages.length - 1 &&
              challengeIndex === page.data.length - 1
                ? targetRef
                : undefined
            }
          >
            <ChallengeItem {...challengeInfo} challengeListRefetch={refetch} />
          </li>
        ));
      })}
      {isFetching && <LoadingSpinner />}
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  gap: '27px',
})``;
