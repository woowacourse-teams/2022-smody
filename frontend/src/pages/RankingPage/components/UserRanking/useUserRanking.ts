import { useGetMyRanking } from 'apis';
import { useEffect, useState } from 'react';
import { useRecoilState, useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';
import { myMemberIdState, selectedRankingPeriodIdState } from 'recoil/ranking/atom';

import {
  EMPTY_RANKING_PERIOD_ID,
  INIT_MY_MEMBER_ID,
  INIT_RANKING_PERIOD_ID,
} from 'constants/domain';

const useUserRanking = () => {
  const [myMemberId, setMyMemberId] = useRecoilState(myMemberIdState);
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
      onSuccess: ({ data }) => {
        if (myMemberId !== data.memberId) {
          setMyMemberId(data.memberId);
        }
      },
    },
  );
  let needSkeleton = !isSuccessMyRanking || rankingPeriodId === INIT_RANKING_PERIOD_ID;

  useEffect(() => {
    if (!isLogin && myMemberId !== INIT_MY_MEMBER_ID) {
      setMyMemberId(INIT_MY_MEMBER_ID);
    }
  }, [isLogin]);

  useEffect(() => {
    if (rankingPeriodId > INIT_RANKING_PERIOD_ID && isLogin) {
      if (notFoundInRanking) {
        setNotFoundInRanking(false);
      }
      refetchMyRanking();
      needSkeleton = !isSuccessMyRanking || rankingPeriodId == INIT_RANKING_PERIOD_ID;
    }
  }, [rankingPeriodId]);

  if (rankingPeriodId === INIT_RANKING_PERIOD_ID) {
    return { myRankingData, needSkeleton: true, notFoundInRanking: false, isLogin };
  }
  if (rankingPeriodId === EMPTY_RANKING_PERIOD_ID) {
    return { myRankingData, needSkeleton: false, notFoundInRanking: true, isLogin };
  }
  return { myRankingData, needSkeleton, notFoundInRanking, isLogin };
};

export default useUserRanking;
