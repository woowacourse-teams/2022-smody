import useRankingPage from './useRankingPage';

import { FlexBox, RankingPeriodsList, UserRanking } from 'components';

const RankingPage = () => {
  const { selectedRankingPeriodId, handleRankingPeriodId } = useRankingPage();

  return (
    <FlexBox flexDirection="column" gap="1rem">
      <UserRanking rankingPeriodId={selectedRankingPeriodId} />
      <RankingPeriodsList handleRankingPeriodId={handleRankingPeriodId} />
    </FlexBox>
  );
};

export default RankingPage;
