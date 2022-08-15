import { useChallengeItemProps } from './type';
import { queryKeys } from 'apis/constants';
import { useQueryClient } from 'react-query';
import { useNavigate } from 'react-router-dom';

import usePostJoinChallenge from 'hooks/usePostJoinChallenge';

import { CLIENT_PATH } from 'constants/path';

const useChallengeItem = ({
  challengeId,
  isInProgress,
  challengeName,
}: useChallengeItemProps) => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const { joinChallenge } = usePostJoinChallenge({
    challengeId: Number(challengeId),
    successCallback: () => {
      queryClient.invalidateQueries(queryKeys.getAllChallenges);
    },
  });

  const handleClickProgressButton = () => {
    isInProgress
      ? navigate(`${CLIENT_PATH.CHALLENGE_DETAIL}/${challengeId}`)
      : joinChallenge({ challengeName });
  };

  return handleClickProgressButton;
};

export default useChallengeItem;
