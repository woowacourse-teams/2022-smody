import { useGetAllChallenges } from 'apis';
import { useRef, RefObject, useMemo } from 'react';
import styled from 'styled-components';

import useIntersect from 'hooks/useIntersect';
import { useManageAccessToken } from 'hooks/useManageAccessToken';
import useSnackBar from 'hooks/useSnackBar';

import { FlexBox, ChallengeItem, LoadingSpinner } from 'components';
import { ChallengeInfo } from 'components/ChallengeList/type';

import { CLIENT_PATH } from 'constants/path';

export const ChallengeList = () => {
  const checkLogout = useManageAccessToken();
  const { isFetching, data, refetch, hasNextPage, fetchNextPage } = useGetAllChallenges({
    refetchOnWindowFocus: false,
    onError: (error) => {
      if (checkLogout(error)) {
        return;
      }

      renderSnackBar({
        message: '챌린지 목록 조회 시 에러가 발생했습니다.',
        status: 'ERROR',
        linkText: '문의하기',
        linkTo: CLIENT_PATH.VOC,
      });
    },
  });

  const rootRef = useRef() as RefObject<HTMLUListElement>;

  const options = useMemo(() => ({ root: rootRef.current, threshold: 0.5 }), []);
  const targetRef = useIntersect<HTMLLIElement>((entry, observer) => {
    if (hasNextPage) {
      fetchNextPage();
    }
    observer.unobserve(entry.target);
  }, options);
  const renderSnackBar = useSnackBar();

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
