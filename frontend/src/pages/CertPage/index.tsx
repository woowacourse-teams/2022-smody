import { useGetMyCyclesInProgress } from 'apis';
import styled, { css } from 'styled-components';

import { useManageAccessToken } from 'hooks/useManageAccessToken';
import useSnackBar from 'hooks/useSnackBar';

import { EmptyContent, CertItem, LoadingSpinner } from 'components';

import { CLIENT_PATH } from 'constants/path';

export const CertPage = () => {
  const renderSnackBar = useSnackBar();

  const checkLogout = useManageAccessToken();

  const { isLoading, data, refetch } = useGetMyCyclesInProgress({
    refetchOnWindowFocus: false,
    onError: (error) => {
      if (checkLogout(error)) {
        return;
      }

      renderSnackBar({
        message: '진행중인 챌린지 조회 시 에러가 발생했습니다.',
        status: 'ERROR',
        linkText: '문의하기',
        linkTo: CLIENT_PATH.VOC,
      });
    },
  });

  if (isLoading || typeof data === 'undefined') {
    return <LoadingSpinner />;
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
    grid-gap: 16px;
    justify-content: center;
    background-color: ${theme.secondary};

    @media all and (min-width: 1761px) {
      grid-template-columns: repeat(4, minmax(370px, max-content));
    }

    @media all and (min-width: 1321px) and (max-width: 1760px) {
      grid-template-columns: repeat(3, minmax(370px, max-content));
    }

    @media all and (min-width: 881px) and (max-width: 1320px) {
      grid-template-columns: repeat(2, minmax(370px, max-content));
    }

    @media all and (max-width: 880px) {
      grid-template-columns: repeat(1, minmax(370px, max-content));
    }
  `}
`;
