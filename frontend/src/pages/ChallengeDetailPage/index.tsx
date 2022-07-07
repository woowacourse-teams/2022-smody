import { usePostCycle } from 'apis/challengeApi';
import { useContext } from 'react';
import { useLocation, useParams, useNavigate } from 'react-router-dom';
import styled, { ThemeContext } from 'styled-components';

import { RouteChallengeDetailState } from 'pages/ChallengeDetailPage/type';

import { FlexBox, Text, Button } from 'components';

import { CLIENT_PATH } from 'constants/path';

const TIMEZONE_OFFSET = 9;

export const ChallengeDetailPage = () => {
  const navigate = useNavigate();

  //TODO: 후에 상세페이지 API 생성 후 제거
  const location = useLocation();
  const state = location.state as RouteChallengeDetailState;

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
  const { challengeId } = useParams();

  if (typeof challengeId === 'undefined') {
    return <p>존재하지 않는 챌린지입니다.</p>;
  }

  const handleClickParticipate = () => {
    const date = new Date();
    date.setHours(date.getHours() + TIMEZONE_OFFSET);

    const [startTime, _] = date.toISOString().split('.');

    mutate({ startTime, challengeId: Number(challengeId) });
  };

  // TODO: 챌린지 상세 조회 API 만들어서 리팩토링하기
  return (
    <>
      <Wrapper>
        <Text fontWeight="bold" size={32} color={themeContext.onBackground}>
          {state.challengeName}
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
