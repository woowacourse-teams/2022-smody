import { useGetMyCyclesInProgress, usePostCycleProgress } from 'apis/challengeApi';
import { Cycle } from 'commonType';
// import { cycleData } from 'mocks/data';
import styled from 'styled-components';

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
  // const { data } = data;

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

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  gap: '1rem',
  alignItems: 'center',
})`
  min-width: 330px;
`;
