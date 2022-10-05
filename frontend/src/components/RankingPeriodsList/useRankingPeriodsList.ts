import { HandleChooseRankingPeriodFunc } from './type';
import { useGetRankingPeriods } from 'apis/rankingApi';
import { useState, useEffect } from 'react';
import { useSetRecoilState } from 'recoil';
import { selectedRankingPeriodIdState } from 'recoil/ranking/atom';
import {
  addDays,
  dateYMDFormatParsing,
  getCurrentStartDateString,
  getWeekNumber,
  isSameDate,
  parseTime,
} from 'utils';

import { EMPTY_RANKING_PERIOD_ID, RANKING_DURATION } from 'constants/domain';

const useRankingPeriodsList = () => {
  const setRankingPeriodId = useSetRecoilState(selectedRankingPeriodIdState);
  const [selectedPeriodIndex, setSelectedPeriodIndex] = useState(0);
  const [showSelectBox, setShowSelectBox] = useState(false);
  const { data: rankingPeriodsData } = useGetRankingPeriods();

  useEffect(() => {
    if (typeof rankingPeriodsData?.data === 'undefined') {
      return;
    }
    if (rankingPeriodsData.data.length === 0) {
      setRankingPeriodId(EMPTY_RANKING_PERIOD_ID);
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

  const currentStartDate = getCurrentStartDateString();
  const currentRankingPeriodData = {
    rankingPeriodId: EMPTY_RANKING_PERIOD_ID,
    startDate: currentStartDate,
    duration: 'week' as const,
  };

  if (
    rankingPeriodsData.data.length === 0 ||
    !isSameDate(rankingPeriodsData.data[0].startDate, currentStartDate)
  ) {
    rankingPeriodsData.data.unshift(currentRankingPeriodData);
  }

  const { startDate, duration } = rankingPeriodsData.data[selectedPeriodIndex];

  const startDateString = dateYMDFormatParsing(startDate);
  const endDateString = dateYMDFormatParsing(
    String(addDays(new Date(Date.parse(startDate)), RANKING_DURATION[duration] - 1)),
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
