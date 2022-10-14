import useSuccessModal from './useSuccessModal';
import Close from 'assets/close.svg';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import {
  Button,
  FlexBox,
  ModalOverlay,
  Text,
  CheckCircles,
  ChallengeIcon,
} from 'components';
import { SuccessModalProps } from 'components/SuccessModal/type';

export const SuccessModal = ({
  handleCloseModal,
  cycleId,
  challengeName,
  successCount,
  challengeId,
  progressCount,
  emojiIndex,
}: SuccessModalProps) => {
  const themeContext = useThemeContext();
  const {
    successMessage,
    isChallengeComplete,
    handleClickClose,
    handleClickCheck,
    handleClickShare,
    handleClickRetry,
  } = useSuccessModal({
    handleCloseModal,
    cycleId,
    challengeName,
    challengeId,
    progressCount,
    emojiIndex,
  });

  return (
    <ModalOverlay handleCloseModal={handleClickClose}>
      <Wrapper
        flexDirection="column"
        alignItems="center"
        justifyContent="center"
        gap="1rem"
      >
        <CloseWrapper onClick={handleClickClose}>
          <Close />
        </CloseWrapper>

        <ChallengeIcon emojiIndex={emojiIndex} challengeId={challengeId} size="large" />
        <span id="confettiRewardId" />
        <span id="emojiRewardId" />
        <Text color={themeContext.onSurface} size={20} fontWeight="bold">
          {challengeName}
        </Text>
        <Text color={themeContext.primary} size={20} fontWeight="bold">
          {isChallengeComplete ? '🎉 챌린지 성공 🎉' : '오늘의 인증 완료'}
        </Text>
        <Text color={themeContext.onSurface} size={20} fontWeight="bold">
          {successMessage}
        </Text>
        {isChallengeComplete ? (
          <Text color={themeContext.disabled} size={16} fontWeight="normal">
            해당 챌린지를 총 {successCount}회 성공하셨어요.
          </Text>
        ) : (
          <CheckCircles progressCount={progressCount} />
        )}
        {isChallengeComplete ? (
          <FlexBox flexDirection="column" gap="1rem">
            <FlexBox gap="0.5rem">
              <Button onClick={handleClickCheck} size="medium" isActive={false}>
                기록 확인
              </Button>
              <Button onClick={handleClickShare} size="medium" isActive={false}>
                공유하기
              </Button>
            </FlexBox>
            <RetryButton onClick={handleClickRetry} size="medium" isActive={true}>
              재도전
            </RetryButton>
          </FlexBox>
        ) : (
          <Button autoFocus onClick={handleClickCheck} size="medium" isActive={false}>
            기록 확인
          </Button>
        )}
      </Wrapper>
    </ModalOverlay>
  );
};

const Wrapper = styled(FlexBox)`
  width: 100%;
  padding: 1rem 1.25rem 1.438rem;
`;

const CloseWrapper = styled.div`
  align-self: flex-end;
  cursor: pointer;
`;

const RetryButton = styled(Button)`
  width: 100%;
`;
