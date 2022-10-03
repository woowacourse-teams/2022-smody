import { useGetAllRanking } from 'apis';
import { useState, useEffect } from 'react';
import { useRecoilValue } from 'recoil';
import { selectedRankingPeriodIdState } from 'recoil/ranking/atom';

import { INIT_RANKING_PERIOD_ID } from 'constants/domain';

const useRankingList = () => {
  const rankingPeriodId = useRecoilValue(selectedRankingPeriodIdState);
  const [emptyRanking, setEmptyRanking] = useState(false);
  const {
    isSuccess: isSuccessAllRanking,
    data: allRankingData,
    refetch: refetchAllRanking,
  } = useGetAllRanking(
    { rankingPeriodId },
    {
      enabled: false,
      suspense: false,
      useErrorBoundary: (error) => error.response?.status !== 404,
      onError: (error) => {
        if (typeof error.response === 'undefined') {
          return;
        }

        if (error.response.status === 404 && emptyRanking === false) {
          setEmptyRanking(true);
        }
      },
    },
  );
  let needSkeleton = !isSuccessAllRanking || rankingPeriodId === INIT_RANKING_PERIOD_ID;

  useEffect(() => {
    if (rankingPeriodId !== INIT_RANKING_PERIOD_ID) {
      if (emptyRanking) {
        setEmptyRanking(false);
      }
      refetchAllRanking();
      needSkeleton = !isSuccessAllRanking || rankingPeriodId === INIT_RANKING_PERIOD_ID;
    }
  }, [rankingPeriodId]);

  if (rankingPeriodId !== INIT_RANKING_PERIOD_ID) {
    return { allRankingData, needSkeleton, emptyRanking };
  }
  return { allRankingData, needSkeleton: true, emptyRanking: false };
};

export default useRankingList;
