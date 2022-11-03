import { useGetAllRanking } from 'apis';
import { useEffect } from 'react';
import { useRecoilValue } from 'recoil';
import { selectedRankingPeriodIdState } from 'recoil/ranking/atom';

import { EMPTY_RANKING_PERIOD_ID, INIT_RANKING_PERIOD_ID } from 'constants/domain';

const useRankingList = () => {
  const rankingPeriodId = useRecoilValue(selectedRankingPeriodIdState);

  const {
    isSuccess: isSuccessAllRanking,
    data: allRankingData,
    refetch: refetchAllRanking,
  } = useGetAllRanking(
    { rankingPeriodId },
    {
      enabled: false,
      suspense: false,
    },
  );
  let needSkeleton = !isSuccessAllRanking || rankingPeriodId === INIT_RANKING_PERIOD_ID;
  let isEmpty =
    (isSuccessAllRanking && allRankingData.data.length === 0) ||
    rankingPeriodId === EMPTY_RANKING_PERIOD_ID;

  useEffect(() => {
    if (rankingPeriodId > INIT_RANKING_PERIOD_ID) {
      refetchAllRanking();
      needSkeleton = !isSuccessAllRanking || rankingPeriodId === INIT_RANKING_PERIOD_ID;
      return;
    }
    isEmpty =
      (isSuccessAllRanking && allRankingData.data.length === 0) ||
      rankingPeriodId === EMPTY_RANKING_PERIOD_ID;
  }, [rankingPeriodId]);

  if (rankingPeriodId === INIT_RANKING_PERIOD_ID) {
    return { allRankingData, isEmpty, needSkeleton: true };
  }
  if (rankingPeriodId === EMPTY_RANKING_PERIOD_ID) {
    return { allRankingData, isEmpty, needSkeleton };
  }
  return { allRankingData, isEmpty, needSkeleton };
};

export default useRankingList;
