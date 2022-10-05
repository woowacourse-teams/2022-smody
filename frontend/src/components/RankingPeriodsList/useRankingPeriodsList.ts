import { HandleChooseRankingPeriodFunc } from './type';
import { useGetRankingPeriods } from 'apis/rankingApi';
import { useState, useEffect } from 'react';
import { useSetRecoilState } from 'recoil';
import { selectedRankingPeriodIdState } from 'recoil/ranking/atom';
import { addDays, dateYMDFormatParsing, getWeekNumber, parseTime } from 'utils';

import { RANKING_DURATION } from 'constants/domain';

const useRankingPeriodsList = () => {
  const setRankingPeriodId = useSetRecoilState(selectedRankingPeriodIdState);
  const [selectedPeriodIndex, setSelectedPeriodIndex] = useState(0);
  const [showSelectBox, setShowSelectBox] = useState(false);
  const { data: rankingPeriodsData } = useGetRankingPeriods();

  useEffect(() => {
    if (typeof rankingPeriodsData?.data === 'undefined') {
      return;
    }
    setRankingPeriodId(rankingPeriodsData.data[selectedPeriodIndex].rankingPeriodId);
  }, [selectedPeriodIndex]);

  const handleSelectBox = () => {
    setShowSelectBox((prev) => !prev);
  };

  const handleChooseRankingPeriod = ({
    index,
    rankingPeriodId,
  }: HandleChooseRankingPeriodFunc) => {
    setRankingPeriodId(rankingPeriodId);
    setSelectedPeriodIndex(index);
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

  const { year, month } = parseTime(startDate);
  const titleDate = `${year}년 ${month}월 ${getWeekNumber(startDate)}주차`;
  const detailDate = `${startDateString} ~ ${endDateString}`;

  return {
    selectedPeriodIndex,
    showSelectBox,
    rankingPeriodsData,
    handleSelectBox,
    handleChooseRankingPeriod,
    titleDate,
    detailDate,
  };
};

export default useRankingPeriodsList;
