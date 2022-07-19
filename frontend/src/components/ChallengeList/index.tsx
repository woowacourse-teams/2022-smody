import { useGetAllChallenges } from 'apis';
import styled from 'styled-components';

import useSnackBar from 'hooks/useSnackBar';

import { FlexBox, ChallengeItem } from 'components';
import { ChallengeInfo } from 'components/ChallengeList/type';

import { CLIENT_PATH } from 'constants/path';

export const ChallengeList = () => {
  const renderSnackBar = useSnackBar();
  const { isLoading, data, refetch, hasNextPage, fetchNextPage } = useGetAllChallenges({
    refetchOnWindowFocus: false,
    onSuccess: () => {
      console.log('챌린지 목록 조회에 성공했습니다.');
    },
    onError: () => {
      console.log('챌린지 목록 조회에 실패했습니다.');
    },
  });

  const loadMore = () => {
    console.log('hasNextPage', hasNextPage);

    if (hasNextPage) {
      fetchNextPage();
    }

    // 스낵바 사용 예시
    renderSnackBar({
      message: '챌린지 목록 조회에 성공했습니다.',
      status: 'SUCCESS',
      linkText: '더보기',
      linkTo: CLIENT_PATH.CERT,
    });
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
