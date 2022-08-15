import { useGetChallengeById } from 'apis';
import { queryKeys } from 'apis/constants';
import { useEffect } from 'react';
import { useQueryClient } from 'react-query';
import { useParams } from 'react-router-dom';

import useJoinChallenge from 'hooks/useJoinChallenge';

const useChallengeDetailPage = () => {
  const queryClient = useQueryClient();
  const { challengeId } = useParams();
  const {
    joinChallenge,
    isSuccessJoinChallenge,
    isCustomCycleTimeOpen,
    handleOpenBottomSheet,
    handleCloseBottomSheet,
  } = useJoinChallenge({ challengeId: Number(challengeId) });

  useEffect(() => {
    if (isSuccessJoinChallenge) {
      queryClient.invalidateQueries(queryKeys.getChallengeById);
      queryClient.invalidateQueries(queryKeys.getChallengersById);
    }
  }, [isSuccessJoinChallenge]);

  const { data: challengeData } = useGetChallengeById(
    { challengeId: Number(challengeId) },
    {
      refetchOnWindowFocus: false,
    },
  );

  return {
    challengeData,
    joinChallenge,
    isCustomCycleTimeOpen,
    handleOpenBottomSheet,
    handleCloseBottomSheet,
  };
};

export default useChallengeDetailPage;
