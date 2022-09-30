import { useGetRankingPeriods } from 'apis/rankingApi';
import { useState } from 'react';
import { addDays, dateYMDFormatParsing } from 'utils';

import { RANKING_DURATION } from 'constants/domain';

const useRankingPeriodsList = () => {
  const [selectedPeriodIndex, setSelectedPeriodIndex] = useState(0);
  const [showSelectBox, setShowSelectBox] = useState(false);
  const { data: rankingPeriodsData } = useGetRankingPeriods();

  const handleSelectBox = () => {
    setShowSelectBox((prev) => !prev);
  };

  const handleChooseRankingPeriod = (id: number) => {
    setSelectedPeriodIndex(id);
    handleSelectBox();
  };

  if (typeof rankingPeriodsData?.data === 'undefined') {
    return {
      selectedPeriodIndex,
      showSelectBox,
      rankingPeriodsData,
      handleSelectBox,
      handleChooseRankingPeriod,
    };
  }

  const { startDate, duration } = rankingPeriodsData.data[selectedPeriodIndex];

  const startDateString = dateYMDFormatParsing(startDate);
  const endDateString = dateYMDFormatParsing(
    String(addDays(new Date(Date.parse(startDate)), RANKING_DURATION[duration])),
  );

  return {
    selectedPeriodIndex,
    showSelectBox,
    rankingPeriodsData,
    handleSelectBox,
    handleChooseRankingPeriod,
    startDateString,
    endDateString,
  };
};

export default useRankingPeriodsList;
