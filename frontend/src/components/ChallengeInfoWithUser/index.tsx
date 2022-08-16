import { useGetMyChallengeById } from 'apis';
import { useParams } from 'react-router-dom';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Title, Text, ShareButton, ThumbnailWrapper } from 'components';

import { CLIENT_PATH } from 'constants/path';
import { colorList, emojiList } from 'constants/style';

import { AvailablePickedColor } from 'styles/type';

export const ChallengeInfoWithUser = () => {
  const themeContext = useThemeContext();
  const { challengeId } = useParams();

  const { data: challengeData } = useGetMyChallengeById(
    { challengeId: Number(challengeId) },
    {
      refetchOnWindowFocus: false,
    },
  );

  if (typeof challengeData === 'undefined') {
    return null;
  }

  const {
    challengeName,
    description,
    emojiIndex,
    colorIndex,
    successCount,
    cycleDetailCount,
  } = challengeData.data;

  return (
    <FlexBox flexDirection="column">
      <Title text={challengeName} linkTo={CLIENT_PATH.FEED}>
        <ShareButton text={`${challengeName} 챌린지에 함께 도전해요!`} />
      </Title>
      <ChallengeDetailWrapper
        flexDirection="row"
        justifyContent="space-between"
        gap="1rem"
      >
        <FlexBox flexDirection="column" gap="1rem">
          <ChallengeExplanationText color={themeContext.onBackground}>
            {description}
          </ChallengeExplanationText>
          <FlexBox gap="2.625rem">
            <FlexBox gap="0.4rem">
              <Text size={16} color={themeContext.onBackground}>
                인증
              </Text>
              <Text size={16} fontWeight="bold" color={themeContext.onBackground}>
                {cycleDetailCount}
              </Text>
            </FlexBox>
            <FlexBox gap="0.4rem">
              <Text size={16} color={themeContext.onBackground}>
                성공
              </Text>
              <Text size={16} fontWeight="bold" color={themeContext.onBackground}>
                {successCount}
              </Text>
            </FlexBox>
          </FlexBox>
        </FlexBox>
        <ThumbnailWrapper size="medium" bgColor={colorList[colorIndex]}>
          {emojiList[emojiIndex]}
        </ThumbnailWrapper>
      </ChallengeDetailWrapper>
    </FlexBox>
  );
};

const ChallengeDetailWrapper = styled(FlexBox)`
  line-height: 1rem;
`;

export interface ChallengeExplanationTextProps {
  color: AvailablePickedColor;
}

const ChallengeExplanationText = styled(Text).attrs<ChallengeExplanationTextProps>(
  ({ color }) => ({
    size: 20,
    color: color,
  }),
)<ChallengeExplanationTextProps>`
  line-height: 1.7rem;
`;
