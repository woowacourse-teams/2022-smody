import { useChallengePage } from './useChallengePage';
import { Suspense } from 'react';
import { useLocation } from 'react-router-dom';
import styled from 'styled-components';

import { EventPage, SearchPage } from 'pages';

import { FlexBox, Button, FixedButton, Tabs, LoadingSpinner } from 'components';

import { CLIENT_PATH } from 'constants/path';

const tabList = [
  {
    path: CLIENT_PATH.CHALLENGE_EVENT,
    tabName: '[우테코]이벤트',
  },
  //   {
  //     path: '/popular',
  //     tabName: '인기',
  //   },
  //   {
  //     path: '/random',
  //     tabName: '랜덤',
  //   },
  {
    path: CLIENT_PATH.CHALLENGE_SEARCH,
    tabName: '전체검색',
  },
];

const ChallengePage = () => {
  const { handleCreateChallengeButton } = useChallengePage();
  return (
    <FlexBox flexDirection="column">
      <Tabs tabList={tabList} />
      <ContentWrapper>
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
