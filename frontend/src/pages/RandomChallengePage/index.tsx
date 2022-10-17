import { useRandomChallengePage } from './useRandomChallengePage';

import { ChallengeList } from 'components';

const RandomChallengePage = () => {
  const { challengeInfiniteData } = useRandomChallengePage();

  if (typeof challengeInfiniteData === 'undefined') {
    return null;
  }

  return <ChallengeList challengeInfiniteData={challengeInfiniteData.pages} />;
};

export default RandomChallengePage;
