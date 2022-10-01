import { HandleRankingPeriodIdFunc } from './type';
import { useState } from 'react';

const useRankingPage = () => {
  const [selectedRankingPeriodId, setSelectedRankingPeriodId] = useState(-1);

  const handleRankingPeriodId: HandleRankingPeriodIdFunc = ({ rankingPeriodId }) => {
    setSelectedRankingPeriodId(rankingPeriodId);
  };

  return {
    selectedRankingPeriodId,
    handleRankingPeriodId,
  };
};

export default useRankingPage;
