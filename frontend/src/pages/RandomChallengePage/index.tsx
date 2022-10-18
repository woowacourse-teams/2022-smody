import { useRandomChallengePage } from './useRandomChallengePage';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { ChallengeList, FlexBox, Text } from 'components';

const RandomChallengePage = () => {
  const themeContext = useThemeContext();
  const { challengeInfiniteData } = useRandomChallengePage();

  if (typeof challengeInfiniteData === 'undefined') {
    return null;
  }

  return (
    <FlexBox flexDirection="column">
      <TitleText color={themeContext.onBackground} size={20} fontWeight="bold">
        ❔ 이런 챌린지는 어때요 ❔
      </TitleText>
      <ChallengeList challengeInfiniteData={challengeInfiniteData.pages} />;
    </FlexBox>
  );
};

export default RandomChallengePage;

const TitleText = styled(Text)`
  text-align: center;
  margin-bottom: 1.5rem;
`;
