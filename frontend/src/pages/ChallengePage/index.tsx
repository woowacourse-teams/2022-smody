import { useChallengePage } from './useChallengePage';
import { Suspense } from 'react';
import { useLocation } from 'react-router-dom';
import styled from 'styled-components';

import { EventPage, PopularChallengePage, RandomChallengePage, SearchPage } from 'pages';

import { FlexBox, Button, FixedButton, Tabs, LoadingSpinner } from 'components';

import { CLIENT_PATH } from 'constants/path';

const tabList = [
  {
    path: CLIENT_PATH.CHALLENGE_RANDOM,
    tabName: '🎲 추천',
  },
  {
    path: CLIENT_PATH.CHALLENGE_POPULAR,
    tabName: '🔥 인기',
  },
  {
    path: CLIENT_PATH.CHALLENGE_SEARCH,
    tabName: '🔎 검색',
  },
  {
    path: CLIENT_PATH.CHALLENGE_EVENT,
    tabName: '⭐ 이벤트',
  },
];

const ChallengePage = () => {
  const { handleCreateChallengeButton } = useChallengePage();
  return (
    <FlexBox flexDirection="column">
      <Tabs tabList={tabList} ariaLabel="챌린지 페이지 탭 목록" />
      <ContentWrapper aria-label="탭 컨텐츠">
        <Suspense fallback={<LoadingSpinner />}>
          <ContentPage />
        </Suspense>
      </ContentWrapper>
      <FixedButton onClick={handleCreateChallengeButton}>챌린지 생성하기</FixedButton>
    </FlexBox>
  );
};

export default ChallengePage;

const ContentPage = () => {
  const { pathname } = useLocation();

  if (pathname === CLIENT_PATH.CHALLENGE_EVENT) {
    return <EventPage />;
  }

  if (pathname === CLIENT_PATH.CHALLENGE_POPULAR) {
    return <PopularChallengePage />;
  }

  if (pathname === CLIENT_PATH.CHALLENGE_RANDOM) {
    return <RandomChallengePage />;
  }

  if (pathname === CLIENT_PATH.CHALLENGE_SEARCH) {
    return <SearchPage />;
  }
  return <h2>not found</h2>;
};

const ContentWrapper = styled.div`
  margin-top: 74px;
  padding-bottom: 65px;
`;

// 나중에 언젠가 활용?
const EventButton = styled(Button)`
  background-image: linear-gradient(to bottom right, #ffd700, orange);
`;
