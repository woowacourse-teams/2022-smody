import { useEffect } from 'react';
import { useReward } from 'react-rewards';
import { useNavigate } from 'react-router-dom';
import { getEmoji } from 'utils/emoji';

import usePostJoinChallenge from 'hooks/usePostJoinChallenge';

import { SuccessModalProps } from 'components/SuccessModal/type';

import { CYCLE_SUCCESS_CRITERIA } from 'constants/domain';
import { CLIENT_PATH } from 'constants/path';

const getMessageByProgressCount = (progressCount: number) => {
  switch (progressCount) {
    case 1:
      return '시작이 좋네요!';
    case 2:
      return '다 왔어요. 한 번만 더!';
    case 3:
      return '한 번 더 도전하기';

    default:
      break;
  }
};

const useSuccessModal = ({
  handleCloseModal,
  cycleId,
  challengeName,
  challengeId,
  progressCount,
}: Omit<SuccessModalProps, 'successCount'>) => {
  const navigate = useNavigate();
  const { reward: confettiReward } = useReward('confettiRewardId', 'confetti');
  const { reward: emojiReward } = useReward('emojiRewardId', 'emoji', {
    emoji: [getEmoji(Number(challengeId))],
  });

  const postJoinChallengeSuccessCallback = () => {
    navigate(CLIENT_PATH.CERT);
    handleCloseModal();
  };

  const { joinChallenge } = usePostJoinChallenge({
    challengeId,
    successCallback: postJoinChallengeSuccessCallback,
  });

  const handleClickCheck = () => {
    handleCloseModal();
    navigate(`${CLIENT_PATH.CYCLE_DETAIL}/${cycleId}`);
  };

  const handleClickClose = () => {
    handleCloseModal();
    navigate(CLIENT_PATH.CERT);
  };

  const handleClickRetry = () => {
    joinChallenge(challengeName);
  };

  const isChallengeComplete = progressCount === CYCLE_SUCCESS_CRITERIA;

  const successMessage = getMessageByProgressCount(progressCount);

  useEffect(() => {
    confettiReward();
    emojiReward();
  }, []);
  return {
    successMessage,
    isChallengeComplete,
    handleClickClose,
    handleClickCheck,
    handleClickRetry,
  };
};

export default useSuccessModal;
