import { useChallengeItemProps } from './type';
import { queryKeys } from 'apis/constants';
import { useState } from 'react';
import { useQueryClient } from 'react-query';
import { useNavigate } from 'react-router-dom';

import usePostJoinChallenge from 'hooks/usePostJoinChallenge';

import { CLIENT_PATH } from 'constants/path';

const useChallengeItem = ({ challengeId, isInProgress }: useChallengeItemProps) => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [isCustomCycleTimeOpen, setIsCustomCycleTimeOpen] = useState(false);

  const { joinChallenge } = usePostJoinChallenge({
    challengeId: Number(challengeId),
    successCallback: () => {
      queryClient.invalidateQueries(queryKeys.getAllChallenges);
    },
  });

  const handleClickProgressButton = () => {
    isInProgress
      ? navigate(`${CLIENT_PATH.CHALLENGE_DETAIL}/${challengeId}`)
      : setIsCustomCycleTimeOpen(true);
  };

  const handleCloseBottomSheet = () => {
    setIsCustomCycleTimeOpen(false);
  };

  return {
    joinChallenge,
    handleClickProgressButton,
    isCustomCycleTimeOpen,
    handleCloseBottomSheet,
  };
};

export default useChallengeItem;
