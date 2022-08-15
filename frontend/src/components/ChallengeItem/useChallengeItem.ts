import { useChallengeItemProps } from './type';
import { queryKeys } from 'apis/constants';
import { useEffect } from 'react';
import { useQueryClient } from 'react-query';

import useJoinChallenge from 'hooks/useJoinChallenge';

const useChallengeItem = ({ challengeId }: useChallengeItemProps) => {
  const queryClient = useQueryClient();

  const {
    joinChallenge,
    isSuccessJoinChallenge,
    isCustomCycleTimeOpen,
    handleOpenBottomSheet,
    handleCloseBottomSheet,
  } = useJoinChallenge({ challengeId });

  useEffect(() => {
    if (isSuccessJoinChallenge) {
      queryClient.invalidateQueries(queryKeys.getAllChallenges);
    }
  }, [isSuccessJoinChallenge]);

  return {
    joinChallenge,
    isCustomCycleTimeOpen,
    handleOpenBottomSheet,
    handleCloseBottomSheet,
  };
};

export default useChallengeItem;
