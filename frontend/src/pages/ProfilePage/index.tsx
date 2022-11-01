import { CardGridContainer } from './components/CardGridContainer';
import styled from 'styled-components';

import { Profile, FlexBox } from 'components';

const ProfilePage = () => {
  return (
    <Wrapper
      flexDirection="column"
      alignItems="center"
      justifyContent="center"
      gap="3rem"
    >
      <Profile />
      <CardsWrapper>
        <CardGridContainer />
      </CardsWrapper>
    </Wrapper>
  );
};

export default ProfilePage;

const Wrapper = styled(FlexBox)`
  margin: 0 auto;
  align-self: center;

  /* PC (해상도 1024px)*/
  /* 테블릿 가로, 테블릿 세로 (해상도 768px ~ 1023px)*/
  @media all and (min-width: 768px) {
    flex-direction: row;
    flex-grow: 1;
  }

  /* 모바일 가로, 모바일 세로 (해상도 480px ~ 767px)*/
  @media all and (max-width: 767px) {
    flex-direction: column;
  }
`;

const CardsWrapper = styled.div`
  min-width: 390px;
`;
