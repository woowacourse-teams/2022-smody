import { useContext, useEffect } from 'react';
import { useReward } from 'react-rewards';
import styled, { ThemeContext } from 'styled-components';
import { getEmoji } from 'utils/emoji';

import usePostJoinChallenge from 'hooks/api/usePostJoinChallenge';

import { Button } from 'components/@shared/Button';
import { FlexBox } from 'components/@shared/FlexBox';
import ModalOverlay from 'components/@shared/ModalOverlay';
import { Text } from 'components/@shared/Text';

import { CheckCircles } from 'components/CheckCircles';
import { SuccessModalProps } from 'components/SuccessModal/type';

import { CYCLE_SUCCESS_CRITERIA } from 'constants/domain';

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
export const SuccessModal = ({
  handleCloseModal,
  challengeName,
  successCount,
  challengeId,
  progressCount,
}: SuccessModalProps) => {
  const themeContext = useContext(ThemeContext);
  const { reward: confettiReward } = useReward('confettiRewardId', 'confetti');
  const { reward: emojiReward } = useReward('emojiRewardId', 'emoji', {
    emoji: [getEmoji(Number(challengeId))],
  });

  const { joinChallenge } = usePostJoinChallenge({
    challengeId,
    successCallback: handleCloseModal,
  });

  const handleRest = () => {
    handleCloseModal();
  };

  const handleRetry = () => {
    joinChallenge(challengeName);
  };

  const isChallengeComplete = progressCount === CYCLE_SUCCESS_CRITERIA;

  useEffect(() => {
    if (isChallengeComplete) {
      confettiReward();
      emojiReward();
    }
  }, []);

  return (
    <>
      <ModalOverlay handleCloseModal={handleCloseModal}>
        <Wrapper>
          <Text color={themeContext.onSurface} size={70} fontWeight="normal">
            {getEmoji(Number(challengeId))}
            <span id="confettiRewardId" />
            <span id="emojiRewardId" />
          </Text>
          <Text color={themeContext.onSurface} size={20} fontWeight="bold">
            {challengeName}
          </Text>
          <Text color={themeContext.primary} size={20} fontWeight="bold">
            {isChallengeComplete ? '🎉 챌린지 성공 🎉' : '오늘의 인증 완료'}
          </Text>
          <Text color={themeContext.blur} size={20} fontWeight="bold">
            {getMessageByProgressCount(progressCount)}
          </Text>
          {isChallengeComplete ? (
            <Text color={themeContext.disabled} size={16} fontWeight="normal">
              해당 챌린지를 총 {successCount}회 성공하셨어요.
            </Text>
          ) : (
            <CheckCircles progressCount={progressCount} />
          )}

          <ButtonWrapper>
            {isChallengeComplete ? (
              <>
                <Button onClick={handleRest} size="medium" isActive={false}>
                  쉴래요
                </Button>
                <Button onClick={handleRetry} size="medium" isActive={true}>
                  재도전
                </Button>
              </>
            ) : (
              <Button onClick={handleCloseModal} size="medium" isActive={false}>
                확인
              </Button>
            )}
          </ButtonWrapper>
        </Wrapper>
      </ModalOverlay>
    </>
  );
};

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  alignItems: 'center',
  justifyContent: 'center',
  gap: '1rem',
})``;

const ButtonWrapper = styled(FlexBox).attrs({
  alignItems: 'center',
  gap: '1rem',
})``;
