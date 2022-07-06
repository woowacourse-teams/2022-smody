import { useContext } from 'react';
import { useParams } from 'react-router-dom';
import styled, { ThemeContext } from 'styled-components';

import { FlexBox, Text, Button } from 'components';

export const ChallengeDetailPage = () => {
  const themeContext = useContext(ThemeContext);
  const { id } = useParams();

  return (
    <>
      <Wrapper>
        <Text fontWeight="bold" size={32} color={themeContext.onBackground}>
          미라클 모닝
        </Text>
      </Wrapper>
      <FixedButton size="large">함께 하기</FixedButton>
    </>
  );
};

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  alignItems: 'center',
})``;

const FixedButton = styled(Button).attrs({
  size: 'large',
})`
  position: fixed;
  bottom: 0;
  margin: 0 4rem 6.25rem;
  left: 0;
  right: 0;
  min-width: 70%;
  z-index: 101;
`;
