import { useState } from 'react';

import usePostJoinChallenge from 'hooks/usePostJoinChallenge';

import { ChallengeItemProps } from 'components/ChallengeItem/type';

type UseJoinChallengeProps = Pick<ChallengeItemProps, 'challengeId'>;

const useJoinChallenge = ({ challengeId }: UseJoinChallengeProps) => {
  const [isCustomCycleTimeOpen, setIsCustomCycleTimeOpen] = useState(false);

  const { joinChallenge, isSuccessJoinChallenge } = usePostJoinChallenge({
    challengeId: Number(challengeId),
  });

  const handleOpenBottomSheet = () => {
    setIsCustomCycleTimeOpen(true);
  };

  const handleCloseBottomSheet = () => {
    setIsCustomCycleTimeOpen(false);
  };

  return {
    joinChallenge,
    isSuccessJoinChallenge,
    isCustomCycleTimeOpen,
    handleOpenBottomSheet,
    handleCloseBottomSheet,
  };
};

export default useJoinChallenge;
