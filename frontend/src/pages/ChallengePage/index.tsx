import { useChallengePage } from './useChallengePage';
import { useLocation, useNavigate } from 'react-router-dom';
import styled, { css } from 'styled-components';

import { EventPage, SearchPage } from 'pages';

import { FlexBox, Button, FixedButton } from 'components';

import { CLIENT_PATH } from 'constants/path';

const ChallengePage = () => {
  const { handleCreateChallengeButton, handleEventPageButton, handleSearchPageButton } =
    useChallengePage();

  return (
    <FlexBox flexDirection="column">
      <TabWrapper alignItems="center" gap="0.5rem">
        <EventButton size="small" onClick={handleEventPageButton}>
          [이벤트] 우테코 프로젝트 챌린지
        </EventButton>
        <Button size="small" onClick={handleSearchPageButton}>
          검색
        </Button>
      </TabWrapper>
      <ContentWrapper>
        <ContentPage />
      </ContentWrapper>
      <FixedButton onClick={handleCreateChallengeButton}>챌린지 생성하기</FixedButton>
    </FlexBox>
  );
};

export default ChallengePage;

const ContentPage = () => {
  const navigate = useNavigate();
  const { pathname } = useLocation();

  if (pathname === CLIENT_PATH.CHALLENGE) {
    navigate(CLIENT_PATH.EVENT);
  }

  if (pathname === CLIENT_PATH.EVENT) {
    return <EventPage />;
  }

  if (pathname === CLIENT_PATH.SEARCH) {
    return <SearchPage />;
  }
  return <h2>not found</h2>;
};

const TabWrapper = styled(FlexBox)`
  ${({ theme }) => css`
    width: 100%;
    height: 50px;
    position: fixed;
    background-color: ${theme.background};
    margin-top: -7px;
  `}
`;

const EventButton = styled(Button)`
  background-image: linear-gradient(to bottom right, #ffd700, orange);
`;

const ContentWrapper = styled.div`
  margin-top: 60px;
  padding-bottom: 65px;
`;
