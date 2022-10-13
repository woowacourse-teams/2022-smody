import useChallengeInfoWithUser from './useChallengeInfoWithUser';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Title, Text, ShareButton, ChallengeIcon } from 'components';

import { CLIENT_PATH } from 'constants/path';
import { colorList, emojiList } from 'constants/style';

export const ChallengeInfoWithUser = () => {
  const themeContext = useThemeContext();
  const challengeData = useChallengeInfoWithUser();

  if (typeof challengeData?.data === 'undefined') {
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
      <Title text={challengeName} linkTo={CLIENT_PATH.PROFILE}>
        <ShareButton text={`${challengeName} 챌린지에 함께 도전해요!`} />
      </Title>
      <ChallengeDetailWrapper
        flexDirection="row"
        justifyContent="space-between"
        alignItems="center"
        gap="1rem"
      >
        <ChallengeDescriptionWrapper
          flexDirection="column"
          gap="1rem"
          justifyContent="center"
        >
          <FlexBox gap="2.625rem" justifyContent="center">
            <FlexBox gap="0.4rem">
              <Text size={16} color={themeContext.onBackground}>
                인증
              </Text>
              <Text size={16} fontWeight="bold" color={themeContext.primary}>
                {cycleDetailCount}
              </Text>
            </FlexBox>
            <FlexBox gap="0.4rem">
              <Text size={16} color={themeContext.onBackground}>
                성공
              </Text>
              <Text size={16} fontWeight="bold" color={themeContext.primary}>
                {successCount}
              </Text>
            </FlexBox>
          </FlexBox>
          <ChallengeExplanationWrapper>
            <Text color={themeContext.onBackground}>{description}</Text>
          </ChallengeExplanationWrapper>
        </ChallengeDescriptionWrapper>
        <ChallengeIcon size="medium" bgColor={colorList[colorIndex]}>
          {emojiList[emojiIndex]}
        </ChallengeIcon>
      </ChallengeDetailWrapper>
    </FlexBox>
  );
};

const ChallengeDetailWrapper = styled(FlexBox)`
  max-width: 1000px;
  line-height: 1rem;
  margin: 0 auto;
`;

const ChallengeDescriptionWrapper = styled(FlexBox)`
  flex-grow: 1;
  flex-wrap: wrap;
  text-align: center;
`;

const ChallengeExplanationWrapper = styled.div`
  word-wrap: break-word;
  word-break: break-all;
  line-height: 1.7rem;
`;
