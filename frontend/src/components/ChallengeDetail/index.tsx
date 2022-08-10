import { ChallengeDetailProps, ChallengeExplanationTextProps } from './type';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, ThumbnailWrapper, Title, Text } from 'components';

import { CLIENT_PATH } from 'constants/path';

export const ChallengeDetail = ({
  challengeName,
  challengerCount,
  emoji,
}: ChallengeDetailProps) => {
  const themeContext = useThemeContext();

  return (
    <FlexBox flexDirection="column">
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
          {emoji}
        </ThumbnailWrapper>
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
