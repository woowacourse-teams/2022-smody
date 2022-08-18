import useCertPage from './useCertPage';
import styled, { css } from 'styled-components';

import { EmptyContent, CertItem, InfiniteScroll, LoadingSpinner } from 'components';

import { CLIENT_PATH } from 'constants/path';

const CertPage = () => {
  const { cycleInfiniteData, isFetching, hasNextPage, fetchNextPage, getCycleCount } =
    useCertPage();

  if (typeof cycleInfiniteData === 'undefined') {
    return null;
  }

  if (getCycleCount() === 0) {
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
    <div>
      <InfiniteScroll
        loadMore={fetchNextPage}
        hasMore={hasNextPage}
        isFetching={isFetching}
        loader={<LoadingSpinner />}
      >
        <CycleList>
          {cycleInfiniteData?.pages.map((page) =>
            page?.data.map((cycleInfo) => (
              <li key={cycleInfo.cycleId}>
                <CertItem {...cycleInfo} />
              </li>
            )),
          )}
        </CycleList>
      </InfiniteScroll>
    </div>
  );
};

export default CertPage;

const CycleList = styled.ul`
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
