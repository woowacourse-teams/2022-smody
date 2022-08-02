import { useGetChallengeById } from 'apis';
import { MdArrowBackIosNew } from 'react-icons/md';
import { useNavigate, useParams } from 'react-router-dom';
import styled from 'styled-components';
import { getEmoji } from 'utils/emoji';

import usePostJoinChallenge from 'hooks/usePostJoinChallenge';
import useThemeContext from 'hooks/useThemeContext';

import { ChallengeExplanationTextProps } from 'pages/ChallengeDetailPage/type';

import { FlexBox, Text, FixedButton, ThumbnailWrapper, LoadingSpinner } from 'components';

import { CLIENT_PATH } from 'constants/path';
import { makeCursorPointer } from 'constants/style';

const ChallengeDetailPage = () => {
  const navigate = useNavigate();
  const themeContext = useThemeContext();
  const { challengeId } = useParams();

  const { isFetching, data } = useGetChallengeById(
    { challengeId: Number(challengeId) },
    {
      refetchOnWindowFocus: false,
    },
  );

  const { joinChallenge } = usePostJoinChallenge({
    challengeId: Number(challengeId),
  });

  if (isFetching || typeof data === 'undefined' || typeof data.data === 'undefined') {
    return <LoadingSpinner />;
  }

  const { challengeName, challengerCount } = data.data;

  const backToPreviousPage = () => {
    navigate(CLIENT_PATH.SEARCH);
  };

  return (
    <Wrapper>
      <TitleWrapper flexDirection="row" justifyContent="space-between">
        <MdArrowBackIosNew
          size={20}
          style={makeCursorPointer}
          onClick={backToPreviousPage}
        />
        <Text fontWeight="bold" size={20} color={themeContext.onBackground}>
          {challengeName}
        </Text>
        <div />
      </TitleWrapper>
      <ChallengeDetailWrapper
        flexDirection="row"
        justifyContent="space-between"
        gap="1rem"
      >
        <FlexBox flexDirection="column" gap="1rem">
          <Text size={16} color={themeContext.primary}>
            현재 {challengerCount}명이 함께 도전 중이에요
          </Text>
          <ChallengeExplanationText color={themeContext.onBackground}>
            &quot;{challengeName}&quot; 챌린지를 {challengerCount}명의 사람들과 지금 바로
            함께하세요!
          </ChallengeExplanationText>
        </FlexBox>
        <ThumbnailWrapper size="medium" bgColor="#FED6D6">
          {getEmoji(Number(challengeId))}
        </ThumbnailWrapper>
      </ChallengeDetailWrapper>
      <FixedButton size="large" onClick={() => joinChallenge(challengeName)}>
        도전하기
      </FixedButton>
    </Wrapper>
  );
};

export default ChallengeDetailPage;

const Wrapper = styled.div`
  margin: 0 1.25rem;
`;

const TitleWrapper = styled(FlexBox)`
  margin-bottom: 2rem;
`;

const ChallengeDetailWrapper = styled(FlexBox)`
  line-height: 1rem;
`;

const ChallengeExplanationText = styled(Text).attrs<ChallengeExplanationTextProps>(
  ({ color }) => ({
    size: 20,
    color: color,
  }),
)<ChallengeExplanationTextProps>`
  line-height: 1.7rem;
`;
