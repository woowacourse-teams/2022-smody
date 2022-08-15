import { usePostCycle } from 'apis';
import { Challenge } from 'commonType';
import { useRef } from 'react';
import { parseDateToISOString } from 'utils';

import useSnackBar from 'hooks/useSnackBar';

import { CLIENT_PATH } from 'constants/path';

interface PostJoinChallengeProps extends Pick<Challenge, 'challengeId'> {
  successCallback?: () => void;
}

export interface JoinChallengeProps {
  challengeName: string;
  startTime?: Date;
}

const usePostJoinChallenge = ({
  challengeId,
  successCallback,
}: PostJoinChallengeProps) => {
  const challengeNameRef = useRef('');

  const renderSnackBar = useSnackBar();

  const { mutate, isSuccess: isSuccessJoinChallenge } = usePostCycle({
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

  const joinChallenge = ({ challengeName, startTime }: JoinChallengeProps) => {
    challengeNameRef.current = challengeName;
    if (!startTime) {
      mutate({ challengeId, startTime: parseDateToISOString(new Date()) });
      return;
    }
    mutate({ challengeId, startTime: parseDateToISOString(startTime) });
  };

  return { joinChallenge, isSuccessJoinChallenge };
};

export default usePostJoinChallenge;
