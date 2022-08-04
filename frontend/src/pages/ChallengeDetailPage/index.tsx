import { useGetChallengeById } from 'apis';
import { queryKeys } from 'apis/constants';
import { useQueryClient } from 'react-query';
import { useParams } from 'react-router-dom';
import styled from 'styled-components';
import { getEmoji } from 'utils/emoji';

import usePostJoinChallenge from 'hooks/usePostJoinChallenge';
import useThemeContext from 'hooks/useThemeContext';

import { ChallengeExplanationTextProps } from 'pages/ChallengeDetailPage/type';

import { FlexBox, Text, FixedButton, ThumbnailWrapper, Title } from 'components';

import { CLIENT_PATH } from 'constants/path';

const ChallengeDetailPage = () => {
  const themeContext = useThemeContext();
  const queryClient = useQueryClient();
  const { challengeId } = useParams();

  const { data } = useGetChallengeById(
    { challengeId: Number(challengeId) },
    {
      refetchOnWindowFocus: false,
    },
  );

  const { joinChallenge } = usePostJoinChallenge({
    challengeId: Number(challengeId),
    successCallback: () => {
      queryClient.invalidateQueries(queryKeys.getChallengeById);
    },
  });

  if (typeof data === 'undefined' || typeof data.data === 'undefined') {
    return null;
  }

  const { challengeName, challengerCount, isInProgress } = data.data;

  return (
    <Wrapper>
      <Title text={challengeName} linkTo={CLIENT_PATH.SEARCH} />
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
      <FixedButton
        size="large"
        onClick={() => joinChallenge(challengeName)}
        disabled={isInProgress}
      >
        {isInProgress ? '도전중' : '도전하기'}
      </FixedButton>
    </Wrapper>
  );
};

export default ChallengeDetailPage;

const Wrapper = styled.div`
  margin: 0 1.25rem;
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
