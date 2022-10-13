import { ChallengeDetailProps, ChallengeExplanationTextProps } from './type';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, ChallengeIcon, Title, Text, ShareButton } from 'components';

import { CLIENT_PATH } from 'constants/path';
import { colorList } from 'constants/style';

export const ChallengeDetail = ({
  challengeId,
  challengeName,
  challengerCount,
  emojiIndex,
  colorIndex,
  description,
}: ChallengeDetailProps) => {
  const themeContext = useThemeContext();

  return (
    <FlexBox flexDirection="column">
      <Title text={challengeName} linkTo={CLIENT_PATH.SEARCH}>
        <ShareButton text={`${challengeName} 챌린지에 함께 도전해요!`} />
      </Title>
      <ChallengeDetailWrapper
        flexDirection="row"
        justifyContent="space-between"
        gap="1rem"
      >
        <FlexBox flexDirection="column" gap="1rem">
          <Text size={16} color={themeContext.primary}>
            {challengerCount
              ? `현재 ${challengerCount}명이 함께 도전 중이에요`
              : '첫 도전자가 되어보세요'}
          </Text>
          <ChallengeExplanationText color={themeContext.onBackground}>
            {description}
          </ChallengeExplanationText>
        </FlexBox>
        <ChallengeIcon
          emojiIndex={emojiIndex}
          challengeId={challengeId}
          size="medium"
          bgColor={colorList[colorIndex]}
        />
      </ChallengeDetailWrapper>
    </FlexBox>
  );
};

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
