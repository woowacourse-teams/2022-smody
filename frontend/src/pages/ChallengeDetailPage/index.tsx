import { usePostCycle } from 'apis/challengeApi';
import { useContext } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import styled, { ThemeContext } from 'styled-components';

import { FlexBox, Text, Button } from 'components';

import { CLIENT_PATH } from 'constants/path';

export const ChallengeDetailPage = () => {
  const navigate = useNavigate();
  const { mutate } = usePostCycle({
    onSuccess: () => {
      alert('챌린지 참여 성공!!');
      navigate(CLIENT_PATH.CERT);
    },
    onError: () => {
      alert('챌린지 참여 실패...');
    },
  });
  const themeContext = useContext(ThemeContext);
  const { id } = useParams();
  const challengeId = Number(id);
  const handleClickParticipate = () => {
    const startTime = new Date().toISOString();
    mutate({ startTime, challengeId });
  };

  return (
    <>
      <Wrapper>
        <Text fontWeight="bold" size={32} color={themeContext.onBackground}>
          미라클 모닝
        </Text>
      </Wrapper>
      <FixedButton size="large" onClick={handleClickParticipate}>
        함께 하기
      </FixedButton>
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
