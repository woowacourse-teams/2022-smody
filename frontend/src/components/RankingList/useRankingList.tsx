import { useGetAllRanking } from 'apis';
import { useEffect } from 'react';
import { useRecoilValue } from 'recoil';
import { myMemberIdState, selectedRankingPeriodIdState } from 'recoil/ranking/atom';

import { INIT_RANKING_PERIOD_ID } from 'constants/domain';

const useRankingList = () => {
  const rankingPeriodId = useRecoilValue(selectedRankingPeriodIdState);
  const myMemberId = useRecoilValue(myMemberIdState);

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

  useEffect(() => {
    if (rankingPeriodId !== INIT_RANKING_PERIOD_ID) {
      refetchAllRanking();
      needSkeleton = !isSuccessAllRanking || rankingPeriodId === INIT_RANKING_PERIOD_ID;
    }
  }, [rankingPeriodId]);

  if (rankingPeriodId !== INIT_RANKING_PERIOD_ID) {
    return { allRankingData, needSkeleton, myMemberId };
  }
  return { allRankingData, needSkeleton: true, myMemberId };
};

export default useRankingList;
