import { usePopularChallengePage } from './usePopularChallengePage';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { ChallengeList, FlexBox, Text } from 'components';

const PopularChallengePage = () => {
  const themeContext = useThemeContext();
  const { challengeInfiniteData } = usePopularChallengePage();

  if (typeof challengeInfiniteData === 'undefined') {
    return null;
  }

  return (
    <FlexBox flexDirection="column">
      <TitleText color={themeContext.onBackground} size={20} fontWeight="bold">
        🔥 가장 많이 참여하고 있는 챌린지 🔥
      </TitleText>
      <ChallengeList challengeInfiniteData={challengeInfiniteData.pages} />;
    </FlexBox>
  );
};

export default PopularChallengePage;

const TitleText = styled(Text)`
  text-align: center;
  margin-bottom: 1.5rem;
`;
