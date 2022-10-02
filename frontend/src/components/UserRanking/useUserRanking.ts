import { UseUserRankingProps } from './type';
import { useGetMyRanking } from 'apis';
import { useEffect } from 'react';

import { INIT_RANKING_PERIOD_ID } from 'constants/domain';

const MockData = {
  data: {
    ranking: -1,
    memberId: -1,
    nickname: '',
    introduction: '',
    picture: '',
    point: -1,
  },
};

const useUserRanking = ({ rankingPeriodId }: UseUserRankingProps) => {
  const {
    isSuccess: isSuccessMyRanking,
    data: myRankingData,
    refetch: refetchMyRanking,
  } = useGetMyRanking({ rankingPeriodId }, { enabled: false, suspense: false });
  let needSkeleton = !isSuccessMyRanking || rankingPeriodId === INIT_RANKING_PERIOD_ID;

  useEffect(() => {
    if (rankingPeriodId !== INIT_RANKING_PERIOD_ID) {
      refetchMyRanking();
      needSkeleton = !isSuccessMyRanking || rankingPeriodId === INIT_RANKING_PERIOD_ID;
    }
  }, [rankingPeriodId]);

  if (rankingPeriodId !== INIT_RANKING_PERIOD_ID) {
    return { myRankingData, needSkeleton };
  }
  return { myRankingData: MockData, needSkeleton: true };
};

export default useUserRanking;
