import { useGetMyCyclesInProgress, usePostCycleProgress } from 'apis/challengeApi';
import styled, { css } from 'styled-components';

import { FlexBox, CertItem } from 'components';

export const CertPage = () => {
  const { isLoading, data, refetch } = useGetMyCyclesInProgress({
    refetchOnWindowFocus: false,
    onError: () => {
      alert('나의 모든 진행중인 챌린지 사이클 조회 실패...');
    },
  });

  const { mutate } = usePostCycleProgress({
    onSuccess: () => {
      alert('사이클 진척도 추가 성공!');
      refetch();
    },
    onError: (error) => {
      alert('사이클 진척도 추가 실패ㅠ_ㅠ');
    },
  });

  const handleClickCertification = (cycleId: number) => {
    mutate({ cycleId });
  };

  if (isLoading || typeof data === 'undefined') {
    return <p>로딩중...</p>;
  }

  return (
    <Wrapper>
      {data.data.map((cycle) => (
        <CertItem
          key={cycle.cycleId}
          handleClickCertification={handleClickCertification}
          {...cycle}
        />
      ))}
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${({ theme }) => css`
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(366px, max-content));
    grid-gap: 16px;
    justify-content: center;
    padding: initial;
    background-color: ${theme.secondary};
  `}
`;
