import { useGetMyRanking } from 'apis';
import { useEffect, useState } from 'react';
import { useRecoilValue } from 'recoil';
import { selectedRankingPeriodIdState } from 'recoil/ranking/atom';

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

const useUserRanking = () => {
  const rankingPeriodId = useRecoilValue(selectedRankingPeriodIdState);
  const [notFoundInRanking, setNotFoundInRanking] = useState(false);
  const {
    isSuccess: isSuccessMyRanking,
    data: myRankingData,
    refetch: refetchMyRanking,
  } = useGetMyRanking(
    { rankingPeriodId },
    {
      enabled: false,
      suspense: false,
      useErrorBoundary: (error) => error.response?.status !== 404,
      onError: (error) => {
        if (typeof error.response === 'undefined') {
          return;
        }

        if (error.response.status === 404 && notFoundInRanking === false) {
          setNotFoundInRanking(true);
        }
      },
    },
  );
  let needSkeleton = !isSuccessMyRanking || rankingPeriodId === INIT_RANKING_PERIOD_ID;

  useEffect(() => {
    if (rankingPeriodId !== INIT_RANKING_PERIOD_ID) {
      if (notFoundInRanking) {
        setNotFoundInRanking(false);
      }
      refetchMyRanking();
      needSkeleton = !isSuccessMyRanking || rankingPeriodId === INIT_RANKING_PERIOD_ID;
    }
  }, [rankingPeriodId]);

  if (rankingPeriodId !== INIT_RANKING_PERIOD_ID) {
    return { myRankingData, needSkeleton, notFoundInRanking };
  }
  return { myRankingData: MockData, needSkeleton: true, notFoundInRanking: false };
};

export default useUserRanking;