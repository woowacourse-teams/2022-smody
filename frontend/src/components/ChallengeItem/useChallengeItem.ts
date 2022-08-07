import { useChallengeItemProps } from './type';

import usePostJoinChallenge from 'hooks/usePostJoinChallenge';

const useChallengeItem = ({
  challengeId,
  challengeListRefetch,
}: useChallengeItemProps) => {
  const { joinChallenge } = usePostJoinChallenge({
    challengeId: Number(challengeId),
    successCallback: challengeListRefetch,
  });

  return joinChallenge;
};

export default useChallengeItem;
