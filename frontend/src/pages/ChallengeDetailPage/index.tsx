import { useGetChallengeById } from 'apis';
import { useContext } from 'react';
import { MdArrowBackIosNew } from 'react-icons/md';
import { useNavigate, useParams } from 'react-router-dom';
import styled, { ThemeContext } from 'styled-components';

import usePostJoinChallenge from 'hooks/api/usePostJoinChallenge';
import { useManageAccessToken } from 'hooks/useManageAccessToken';
import useSnackBar from 'hooks/useSnackBar';

import { ChallengeExplanationTextProps } from 'pages/ChallengeDetailPage/type';

import { FlexBox, Text, FixedButton, ThumbnailWrapper } from 'components';

import { CLIENT_PATH } from 'constants/path';

const makeCursorPointer = {
  cursor: 'pointer',
};

export const ChallengeDetailPage = () => {
  const renderSnackBar = useSnackBar();
  const checkLogout = useManageAccessToken();
  const navigate = useNavigate();
  const themeContext = useContext(ThemeContext);
  const { challengeId } = useParams();

  const { isLoading, data } = useGetChallengeById(
    { challengeId: Number(challengeId) },
    {
      refetchOnWindowFocus: false,
      onError: (error) => {
        renderSnackBar({
          message: '챌린지 조회 시 에러가 발생했습니다.',
          status: 'ERROR',
          linkText: '문의하기',
          linkTo: CLIENT_PATH.VOC,
        });

        checkLogout(error);
      },
    },
  );

  const { joinChallenge } = usePostJoinChallenge({
    challengeId: Number(challengeId),
  });

  if (isLoading || typeof data === 'undefined' || typeof data.data === 'undefined') {
    return <p>로딩중...</p>;
  }

  const { challengeName, challengerCount } = data.data;

  const backToPreviousPage = () => {
    navigate(CLIENT_PATH.SEARCH);
  };

  return (
    <Wrapper>
      <TitleWrapper style={makeCursorPointer} onClick={backToPreviousPage}>
        <MdArrowBackIosNew size={20} />
        <Text fontWeight="bold" size={20} color={themeContext.onBackground}>
          {challengeName}
        </Text>
        <div />
      </TitleWrapper>
      <ChallengeDetailWrapper>
        <ChallengeTextWrapper>
          <Text size={16} color={themeContext.primary}>
            현재 {challengerCount}명이 함께 도전 중이에요
          </Text>
          <ChallengeExplanationText color={themeContext.onBackground}>
            &quot;{challengeName}&quot; 챌린지를 {challengerCount}명의 사람들과 지금 바로
            함께하세요!
          </ChallengeExplanationText>
        </ChallengeTextWrapper>
        <ThumbnailWrapper size="medium" bgColor="#FED6D6">
          🎁
        </ThumbnailWrapper>
      </ChallengeDetailWrapper>
      <FixedButton size="large" onClick={() => joinChallenge(challengeName)}>
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
