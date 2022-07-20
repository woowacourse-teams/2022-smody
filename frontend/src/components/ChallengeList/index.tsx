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
    onError: () => {
      renderSnackBar({
        message: '챌린지 목록 조회 시 에러가 발생했습니다.',
        status: 'ERROR',
        linkText: '문의하기',
        linkTo: CLIENT_PATH.VOC,
      });
    },
  });

  const loadMore = () => {
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
