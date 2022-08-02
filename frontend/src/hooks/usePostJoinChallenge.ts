import { usePostCycle } from 'apis';
import { Challenge } from 'commonType';
import { useRef } from 'react';

import useSnackBar from 'hooks/useSnackBar';

import { CLIENT_PATH } from 'constants/path';

interface PostJoinChallengeProps extends Pick<Challenge, 'challengeId'> {
  successCallback?: () => void;
}

const usePostJoinChallenge = ({
  challengeId,
  successCallback,
}: PostJoinChallengeProps) => {
  const challengeNameRef = useRef('');

  const renderSnackBar = useSnackBar();

  const { mutate, isSuccess } = usePostCycle({
    onSuccess: () => {
      successCallback && successCallback();

      renderSnackBar({
        message: `${challengeNameRef.current} 챌린지에 성공적으로 참여하였습니다`,
        status: 'SUCCESS',
        linkText: '인증하러 가기',
        linkTo: CLIENT_PATH.CERT,
      });
    },
  });

  const joinChallenge = (challengeName: string) => {
    challengeNameRef.current = challengeName;
    mutate({ challengeId });
  };

  return { joinChallenge, isSuccess };
};

export default usePostJoinChallenge;
