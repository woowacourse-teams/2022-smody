import { useGetAllChallenges } from 'apis';
import styled from 'styled-components';

import useSnackBar from 'hooks/useSnackBar';

import { FlexBox, ChallengeItem } from 'components';
import { ChallengeInfo } from 'components/ChallengeList/type';

export const ChallengeList = () => {
  const renderSnackBar = useSnackBar();
  const { isLoading, data, refetch, hasNextPage, fetchNextPage } = useGetAllChallenges({
    refetchOnWindowFocus: false,
    onSuccess: () => {
      console.log('챌린지 목록 조회에 성공했습니다.');
      renderSnackBar({ message: '챌린지 목록 조회에 성공했습니다.', status: 'SUCCESS' });
    },
    onError: () => {
      console.log('챌린지 목록 조회에 실패했습니다.');
      renderSnackBar({ message: '챌린지 목록 조회에 실패했습니다.', status: 'ERROR' });
    },
  });

  const loadMore = () => {
    console.log('hasNextPage', hasNextPage);
    renderSnackBar({
      message: '챌린지 목록 조회에 성공했습니다.',
      status: 'SUCCESS',
    });

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
