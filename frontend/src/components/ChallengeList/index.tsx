import { useGetAllChallenges } from 'apis/challengeApi';
import styled, { css } from 'styled-components';

import { FlexBox, ChallengeItem } from 'components';
import { ChallengeItemProps } from 'components/ChallengeItem/type';

export const ChallengeList = () => {
  const { isLoading, data } = useGetAllChallenges({
    refetchOnWindowFocus: false,
    onError: () => {
      alert('나의 모든 진행중인 챌린지 사이클 조회 실패...');
    },
  });

  if (isLoading || typeof data === 'undefined') {
    return <p>로딩중...</p>;
  }

  return (
    <Wrapper as="ul">
      {data.data.map(
        ({ challengeId, challengeName, challengerCount }: ChallengeItemProps) => (
          <li key={challengeId}>
            <ChallengeItem
              challengeId={challengeId}
              challengeName={challengeName}
              challengerCount={challengerCount}
            />
            {/* <Line /> */}
          </li>
        ),
      )}
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  gap: '27px',
})``;

const Line = styled.hr`
  ${({ theme }) => css`
    color: ${theme.border};
    border-style: solid none none;
    margin: 0 1rem;
    padding: 0;
  `}
`;
