import { useGetMyCyclesInProgress } from 'apis';
import styled, { css } from 'styled-components';

import { EmptyContent, CertItem } from 'components';

import { CLIENT_PATH } from 'constants/path';

export const CertAuth = () => {
  const { isLoading, data, refetch } = useGetMyCyclesInProgress({
    refetchOnWindowFocus: false,
    onError: () => {
      console.log('나의 모든 진행중인 챌린지 사이클 조회 실패...');
    },
  });

  if (isLoading || typeof data === 'undefined') {
    return <p>로딩중...</p>;
  }

  if (data.data.length === 0) {
    return (
      <EmptyContent
        title="도전 중인 챌린지가 없습니다 :)"
        description="새로운 챌린지에 도전해보아요!!"
        linkText="검색 페이지로 이동하기"
        linkTo={CLIENT_PATH.SEARCH}
      />
    );
  }

  return (
    <Wrapper>
      {data.data.map((cycle) => (
        <CertItem key={cycle.cycleId} refetch={refetch} {...cycle} />
      ))}
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${({ theme }) => css`
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(370px, max-content));
    grid-gap: 16px;
    justify-content: center;
    background-color: ${theme.secondary};
  `}
`;
