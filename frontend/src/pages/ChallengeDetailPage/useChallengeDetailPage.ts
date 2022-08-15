import { useGetChallengeById } from 'apis';
import { queryKeys } from 'apis/constants';
import { useState } from 'react';
import { useQueryClient } from 'react-query';
import { useParams } from 'react-router-dom';

import usePostJoinChallenge from 'hooks/usePostJoinChallenge';

const useChallengeDetailPage = () => {
  const queryClient = useQueryClient();
  const { challengeId } = useParams();
  const [isCustomCycleTimeOpen, setIsCustomCycleTimeOpen] = useState(false);
  const currentHour = new Date().getHours();

  const { data: challengeData } = useGetChallengeById(
    { challengeId: Number(challengeId) },
    {
      refetchOnWindowFocus: false,
    },
  );

  const { joinChallenge } = usePostJoinChallenge({
    challengeId: Number(challengeId),
    successCallback: () => {
      queryClient.invalidateQueries(queryKeys.getChallengeById);
      queryClient.invalidateQueries(queryKeys.getChallengersById);
    },
  });

  const handleOpenBottomSheet = () => {
    setIsCustomCycleTimeOpen(true);
  };

  const handleCloseBottomSheet = () => {
    setIsCustomCycleTimeOpen(false);
  };

  return {
    challengeData,
    joinChallenge,
    currentHour,
    isCustomCycleTimeOpen,
    handleOpenBottomSheet,
    handleCloseBottomSheet,
  };
};

export default useChallengeDetailPage;
