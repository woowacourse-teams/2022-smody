import { usePopularChallengePage } from './usePopularChallengePage';

import { ChallengeList } from 'components';

const PopularChallengePage = () => {
  const { challengeInfiniteData } = usePopularChallengePage();

  if (typeof challengeInfiniteData === 'undefined') {
    return null;
  }

  return <ChallengeList challengeInfiniteData={challengeInfiniteData.pages} />;
};

export default PopularChallengePage;
