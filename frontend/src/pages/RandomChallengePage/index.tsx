import { useRandomChallengePage } from './useRandomChallengePage';
import styled from 'styled-components';

import useSnackBar from 'hooks/useSnackBar';
import useThemeContext from 'hooks/useThemeContext';

import { ChallengeItem, ChallengeList, FlexBox, Text } from 'components';

const RandomChallengePage = () => {
  const renderSnackBar = useSnackBar();
  const themeContext = useThemeContext();
  const { challengeInfiniteData, isError, savedChallenges } = useRandomChallengePage();

  if (isError) {
    const errorMessage = !navigator.onLine
      ? '네트워크가 오프라인입니다. 이전에 캐싱된 데이터가 표시됩니다.'
      : '서버 에러가 발생했습니다. 이전에 캐싱된 데이터가 표시됩니다';
    renderSnackBar({
      status: 'ERROR',
      message: errorMessage,
    });

    return (
      <FlexBox as="ul" flexDirection="column" gap="27px">
        {savedChallenges.map((challengeInfo) => (
          <li key={challengeInfo.challengeId}>
            <ChallengeItem {...challengeInfo} />
          </li>
        ))}
      </FlexBox>
    );
  }

  if (typeof challengeInfiniteData === 'undefined') {
    return null;
  }

  return (
    <FlexBox flexDirection="column">
      <TitleText color={themeContext.onBackground} size={20} fontWeight="bold">
        ❔ 이런 챌린지는 어때요 ❔
      </TitleText>
      <ChallengeList challengeInfiniteData={challengeInfiniteData.pages} />
    </FlexBox>
  );
};

export default RandomChallengePage;

const TitleText = styled(Text)`
  text-align: center;
  margin-bottom: 1.5rem;
`;
