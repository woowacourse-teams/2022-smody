import { useContext } from 'react';
import styled, { ThemeContext } from 'styled-components';

import usePostJoinChallenge from 'hooks/api/usePostJoinChallenge';

import { Button } from 'components/@shared/Button';
import { FlexBox } from 'components/@shared/FlexBox';
import ModalOverlay from 'components/@shared/ModalOverlay';
import { Text } from 'components/@shared/Text';

import { SuccessModalProps } from 'components/SuccessModal/type';

export const SuccessModal = ({
  handleCloseModal,
  challengeName,
  successCount,
  challengeId,
}: SuccessModalProps) => {
  const themeContext = useContext(ThemeContext);
  const { joinChallenge } = usePostJoinChallenge({ challengeId });

  const handleRetry = () => {
    handleCloseModal();
    joinChallenge();
  };

  return (
    <ModalOverlay handleCloseModal={handleCloseModal}>
      <Wrapper>
        <Text color={themeContext.onSurface} size={70} fontWeight="normal">
          🌞
        </Text>
        <Text color={themeContext.onSurface} size={24} fontWeight="bold">
          {challengeName}
        </Text>
        <Text color={themeContext.primary} size={24} fontWeight="bold">
          🎉 챌린지 성공 🎉
        </Text>
        <Text color={themeContext.blur} size={24} fontWeight="bold">
          한번 더 도전하기
        </Text>
        <Text color={themeContext.disabled} size={16} fontWeight="normal">
          해당 챌린지를 총 {successCount}회 성공하셨어요.
        </Text>

        <ButtonWrapper>
          <Button onClick={handleCloseModal} size="medium" isActive={false}>
            쉴래요
          </Button>
          <Button onClick={handleRetry} size="medium" isActive={true}>
            재도전
          </Button>
        </ButtonWrapper>
      </Wrapper>
    </ModalOverlay>
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
