import { useContext } from 'react';
import { MdArrowBackIosNew } from 'react-icons/md';
import { useLocation, useParams, useNavigate } from 'react-router-dom';
import styled, { ThemeContext } from 'styled-components';

import usePostJoinChallenge from 'hooks/api/usePostJoinChallenge';

import {
  ChallengeExplanationTextProps,
  RouteChallengeDetailState,
} from 'pages/ChallengeDetailPage/type';

import { FlexBox, Text, FixedButton, ThumbnailWrapper } from 'components';

import { CLIENT_PATH } from 'constants/path';

const challengerCount = 10;

const makeCursorPointer = {
  cursor: 'pointer',
};

export const ChallengeDetailPage = () => {
  const navigate = useNavigate();

  //TODO: 후에 상세페이지 API 생성 후 제거
  const location = useLocation();
  const state = location.state as RouteChallengeDetailState;

  const themeContext = useContext(ThemeContext);
  const { challengeId } = useParams();

  if (typeof challengeId === 'undefined') {
    return <p>존재하지 않는 챌린지입니다.</p>;
  }

  const backToPreviousPage = () => {
    navigate(CLIENT_PATH.SEARCH);
  };

  const { joinChallenge } = usePostJoinChallenge(Number(challengeId));

  // TODO: 챌린지 상세 조회 API 만들어서 리팩토링하기
  return (
    <Wrapper>
      <TitleWrapper style={makeCursorPointer} onClick={backToPreviousPage}>
        <MdArrowBackIosNew size={24} />
        <Text fontWeight="bold" size={24} color={themeContext.onBackground}>
          {state.challengeName}
        </Text>
        <div />
      </TitleWrapper>
      <ChallengeDetailWrapper>
        <ChallengeTextWrapper>
          <Text size={16} color={themeContext.primary}>
            현재 {challengerCount}명이 함께 도전 중이에요
          </Text>
          <ChallengeExplanationText color={themeContext.onBackground}>
            건강을 위해 하루에 만보씩 걷고 걷기 앱을 캡처하여 인증해주세요
          </ChallengeExplanationText>
        </ChallengeTextWrapper>
        <ThumbnailWrapper size="medium" bgColor="#FED6D6">
          🎁
        </ThumbnailWrapper>
      </ChallengeDetailWrapper>
      <FixedButton size="large" onClick={joinChallenge}>
        도전하기
      </FixedButton>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  margin: 0 1.25rem;
`;

const TitleWrapper = styled(FlexBox).attrs({
  flexDirection: 'row',
  justifyContent: 'space-between',
})`
  margin-bottom: 2rem;
`;

const ChallengeDetailWrapper = styled(FlexBox).attrs({
  flexDirection: 'row',
  justifyContent: 'space-between',
  gap: '1rem',
})`
  line-height: 1rem;
`;

const ChallengeTextWrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  gap: '1rem',
})``;

const ChallengeExplanationText = styled(Text).attrs<ChallengeExplanationTextProps>(
  ({ color }) => ({
    size: 20,
    color: color,
  }),
)<ChallengeExplanationTextProps>`
  line-height: 1.7rem;
`;
