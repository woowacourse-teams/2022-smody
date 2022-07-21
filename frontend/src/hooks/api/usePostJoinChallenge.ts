import { usePostCycle } from 'apis';
import { useRef } from 'react';
import { validateAccessToken } from 'utils/validator';

import { PostJoinChallengeProps } from 'hooks/api/type';
import useSnackBar from 'hooks/useSnackBar';

import { CLIENT_PATH } from 'constants/path';

const usePostJoinChallenge = ({
  challengeId,
  challengeListRefetch,
}: PostJoinChallengeProps) => {
  const challengeNameRef = useRef('');

  const renderSnackBar = useSnackBar();

  const { mutate, isSuccess } = usePostCycle({
    onSuccess: () => {
      challengeListRefetch && challengeListRefetch();

      renderSnackBar({
        message: `${challengeNameRef.current} 챌린지에 성공적으로 참여하였습니다`,
        status: 'SUCCESS',
        linkText: '인증하러 가기',
        linkTo: CLIENT_PATH.CERT,
      });
    },
    onError: (error) => {
      renderSnackBar({
        message: `${challengeNameRef.current} 챌린지 참여 시 에러가 발생했습니다`,
        status: 'ERROR',
        linkText: '문의하기',
        linkTo: CLIENT_PATH.VOC,
      });

      validateAccessToken(error);
    },
  });

  const joinChallenge = (challengeName: string) => {
    challengeNameRef.current = challengeName;
    mutate({ challengeId });
  };

  return { joinChallenge, isSuccess };
};

export default usePostJoinChallenge;
