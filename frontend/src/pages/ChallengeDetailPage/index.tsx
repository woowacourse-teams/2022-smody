import useChallengeDetailPage from './useChallengeDetailPage';
import styled from 'styled-components';

import { FlexBox, FixedButton, ChallengerList, ChallengeDetail } from 'components';

const ChallengeDetailPage = () => {
  const { challengeData, joinChallenge } = useChallengeDetailPage();

  if (typeof challengeData?.data === 'undefined') {
    return null;
  }

  const { challengeId, challengeName, isInProgress } = challengeData.data;

  return (
    <Wrapper flexDirection="column" gap="2rem">
      <ChallengeDetail {...challengeData.data} />
      <ChallengerList challengeId={challengeId} />
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

const Wrapper = styled(FlexBox)`
  margin: 0 1.25rem;
  padding-bottom: 90px;
`;
