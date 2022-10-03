import { useGetMyRanking } from 'apis';
import { useEffect, useState } from 'react';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';
import { selectedRankingPeriodIdState } from 'recoil/ranking/atom';

import { INIT_RANKING_PERIOD_ID } from 'constants/domain';

const useUserRanking = () => {
  const rankingPeriodId = useRecoilValue(selectedRankingPeriodIdState);
  const isLogin = useRecoilValue(isLoginState);
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
    if (rankingPeriodId !== INIT_RANKING_PERIOD_ID && isLogin) {
      if (notFoundInRanking) {
        setNotFoundInRanking(false);
      }
      refetchMyRanking();
      needSkeleton = !isSuccessMyRanking || rankingPeriodId === INIT_RANKING_PERIOD_ID;
    }
  }, [rankingPeriodId]);

  if (rankingPeriodId !== INIT_RANKING_PERIOD_ID) {
    return { myRankingData, needSkeleton, notFoundInRanking, isLogin };
  }
  return { myRankingData, needSkeleton: true, notFoundInRanking: false, isLogin };
};

export default useUserRanking;
