import { BsFillPlusCircleFill } from 'react-icons/bs';
import styled, { css } from 'styled-components';

import { useSearchPage } from 'pages/SearchPage/useSearchPage';

import {
  LoadingSpinner,
  FlexBox,
  InfiniteScroll,
  SearchBar,
  ChallengeList,
} from 'components';

import { Z_INDEX } from 'constants/css';

const SearchPage = () => {
  const {
    isFetching,
    challengeInfiniteData,
    hasNextPage,
    search,
    handleSubmitSearch,
    fetchNextPage,
    handleCreateChallengeButton,
  } = useSearchPage();

  if (typeof challengeInfiniteData === 'undefined') {
    return null;
  }

  return (
    <Wrapper flexDirection="column">
      <SearchBar search={search} handleSubmitSearch={handleSubmitSearch} />
      <InfiniteScroll
        loadMore={fetchNextPage}
        hasMore={hasNextPage}
        isFetching={isFetching}
        loader={<LoadingSpinner />}
      >
        <ChallengeList challengeInfiniteData={challengeInfiniteData.pages} />
      </InfiniteScroll>
      <CreateChallengeButtonBackground>
        <CreateChallengeButton onClick={handleCreateChallengeButton} />
      </CreateChallengeButtonBackground>
    </Wrapper>
  );
};

export default SearchPage;

const Wrapper = styled(FlexBox)`
  margin-top: 60px;
`;

const CreateChallengeButtonBackground = styled.div`
  ${({ theme }) => css`
    position: fixed;
    border-radius: 50%;
    box-sizing: border-box;
    background-color: ${theme.surface};
    z-index: ${Z_INDEX.FIXED_BUTTON};

    /* PC (해상도 1024px)*/
    /* 테블릿 가로, 테블릿 세로 (해상도 768px ~ 1023px)*/
    @media all and (min-width: 768px) {
      width: 4rem;
      height: 4rem;
      right: 4rem;
      bottom: 6rem;
    }

    /* 모바일 가로, 모바일 세로 (해상도 480px ~ 767px)*/
    @media all and (max-width: 767px) {
      width: 3rem;
      height: 3rem;
      right: 2rem;
      bottom: 5rem;
    }
  `}
`;

const CreateChallengeButton = styled(BsFillPlusCircleFill)`
  ${({ theme }) => css`
    color: ${theme.onSecondary};
    filter: brightness(1.9);
    z-index: ${Z_INDEX.FIXED_BUTTON};
    cursor: pointer;

    /* PC (해상도 1024px)*/
    /* 테블릿 가로, 테블릿 세로 (해상도 768px ~ 1023px)*/
    @media all and (min-width: 768px) {
      width: 4rem;
      height: 4rem;
    }

    /* 모바일 가로, 모바일 세로 (해상도 480px ~ 767px)*/
    @media all and (max-width: 767px) {
      width: 3rem;
      height: 3rem;
    }
  `}
`;
