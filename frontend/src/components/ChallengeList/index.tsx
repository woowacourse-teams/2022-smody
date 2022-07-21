import { useChallengeList } from './useChallengeList';
import styled from 'styled-components';

import { FlexBox, ChallengeItem } from 'components';
import { ChallengeInfo } from 'components/ChallengeList/type';
import Loading from 'components/LoadingSpinner';

export const ChallengeList = () => {
  const { isFetching, data, refetch, rootRef, targetRef } = useChallengeList();

  return (
    <Wrapper as="ul" ref={rootRef}>
      {data?.pages.map((page, pageIndex) =>
        page?.data?.map((challengeInfo: ChallengeInfo, challengeIndex: number) => (
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
        )),
      )}
      {isFetching && <Loading />}
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  gap: '27px',
})``;
