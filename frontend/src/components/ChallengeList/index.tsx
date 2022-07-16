import { useGetAllChallenges } from 'apis';
import styled from 'styled-components';

import { FlexBox, ChallengeItem } from 'components';
import { ChallengeInfo } from 'components/ChallengeList/type';

export const ChallengeList = () => {
  const { isLoading, data, refetch } = useGetAllChallenges({
    refetchOnWindowFocus: false,
    onError: () => {
      console.log('나의 모든 진행중인 챌린지 사이클 조회 실패...');
    },
  });

  if (isLoading || typeof data === 'undefined') {
    return <p>로딩중...</p>;
  }

  return (
    <Wrapper as="ul">
      {data.data.map(({ challengeId, challengeName, challengerCount }: ChallengeInfo) => (
        <li key={challengeId}>
          <ChallengeItem
            challengeId={challengeId}
            challengeName={challengeName}
            challengerCount={challengerCount}
            challengeListRefetch={refetch}
          />
        </li>
      ))}
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  gap: '27px',
})``;
