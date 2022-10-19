import { ChallengeListProps } from './type';
import styled from 'styled-components';

import { FlexBox, MemoizedChallengeItem, EmptyContent } from 'components';

export const ChallengeList = ({ challengeInfiniteData }: ChallengeListProps) => {
  if (!challengeInfiniteData || challengeInfiniteData[0].data.length === 0) {
    return (
      <EmptyContentWrapper>
        <EmptyContent
          title="일치하는 검색 결과가 없습니다 :)"
          description="다른 챌린지를 찾아볼까요?"
        />
      </EmptyContentWrapper>
    );
  }

  return (
    <FlexBox as="ul" flexDirection="column" gap="27px">
      {challengeInfiniteData.map((page) =>
        page.data.map((challengeInfo) => (
          <li key={challengeInfo.challengeId}>
            <MemoizedChallengeItem {...challengeInfo} />
          </li>
        )),
      )}
    </FlexBox>
  );
};

const EmptyContentWrapper = styled.div`
  margin-top: 5rem;
`;
