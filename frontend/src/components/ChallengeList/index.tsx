import { useGetAllChallenges } from 'apis';
import styled from 'styled-components';

import { FlexBox, ChallengeItem } from 'components';
import { ChallengeInfo } from 'components/ChallengeList/type';

export const ChallengeList = () => {
  const { isLoading, data, refetch, hasNextPage, fetchNextPage } = useGetAllChallenges({
    refetchOnWindowFocus: false,
    onError: () => {
      console.log('나의 모든 진행중인 챌린지 사이클 조회 실패...');
    },
  });

  const loadMore = () => {
    console.log('hasNextPage', hasNextPage);
    if (hasNextPage) {
      fetchNextPage();
    }
  };

  if (isLoading || typeof data === 'undefined') {
    return <p>로딩중...</p>;
  }

  return (
    <Wrapper as="ul">
      {data.pages.map((page) => {
        if (typeof page === 'undefined' || typeof page.data === 'undefined') {
          return [];
        }

        return page.data.map((challengeInfo: ChallengeInfo) => (
          <li key={challengeInfo.challengeId}>
            <ChallengeItem {...challengeInfo} challengeListRefetch={refetch} />
          </li>
        ));
      })}
      <button onClick={loadMore}>다음페이지</button>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  gap: '27px',
})``;
