import { usePostCycle } from 'apis';
import { useNavigate } from 'react-router-dom';

import { PostJoinChallengeProps } from 'hooks/api/type';

import { TIMEZONE_OFFSET } from 'constants/domain';
import { CLIENT_PATH } from 'constants/path';

const usePostJoinChallenge = ({
  challengeId,
  isNavigator = true,
  handleSuccessFunction,
}: PostJoinChallengeProps) => {
  const navigate = useNavigate();

  const { mutate, isSuccess } = usePostCycle({
    onSuccess: () => {
      console.log('챌린지 참여 성공!!');
      isNavigator && navigate(CLIENT_PATH.CERT);
      handleSuccessFunction && handleSuccessFunction();
    },
    onError: () => {
      console.log('챌린지 참여 실패...');
    },
  });

  const joinChallenge = () => {
    mutate({ challengeId });
  };

  return { joinChallenge, isSuccess };
};

export default usePostJoinChallenge;
