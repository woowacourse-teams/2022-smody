import { HandleRankingPeriodIdFunc } from './type';
import { useState } from 'react';

import { INIT_RANKING_PERIOD_ID } from 'constants/domain';

const useRankingPage = () => {
  const [selectedRankingPeriodId, setSelectedRankingPeriodId] =
    useState(INIT_RANKING_PERIOD_ID);

  const handleRankingPeriodId: HandleRankingPeriodIdFunc = ({ rankingPeriodId }) => {
    setSelectedRankingPeriodId(rankingPeriodId);
  };

  return {
    selectedRankingPeriodId,
    handleRankingPeriodId,
  };
};

export default useRankingPage;
