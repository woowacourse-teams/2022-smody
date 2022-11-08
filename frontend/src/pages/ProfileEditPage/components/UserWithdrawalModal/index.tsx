import useUserWithdrawalModal from './useUserWithdrawalModal';
import Close from 'assets/close.svg';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { UserWithdrawalModalProps } from 'pages/ProfileEditPage/components/UserWithdrawalModal/type';

import { Button, FlexBox, ModalOverlay, Text, Input } from 'components';

export const UserWithdrawalModal = ({
  email,
  handleCloseModal,
}: UserWithdrawalModalProps) => {
  const themeContext = useThemeContext();
  const { canWithdrawal, handleClickUserWithdrawal, emailInput } = useUserWithdrawalModal(
    { email },
  );

  return (
    <ModalOverlay handleCloseModal={handleCloseModal}>
      <Wrapper flexDirection="column" alignItems="center" gap="1.5rem">
        <CloseButton onClick={handleCloseModal} aria-label="닫기">
          <Close />
        </CloseButton>
        <Text color={themeContext.primary} size={20} fontWeight="bold">
          정말로 떠나시겠습니까?
        </Text>
        <Text color={themeContext.onBackground} size={16}>
          회원 탈퇴는 취소할 수 없으며 현재까지 도전한 정보는 사라집니다.
        </Text>
        <FlexBox alignItems="center" flexWrap="wrap">
          <Text color={themeContext.onBackground} size={16}>
            탈퇴를 원하신다면 입력창에&nbsp;
          </Text>
          <Text color={themeContext.primary} size={16} fontWeight="bold">
            {email}
          </Text>
          <Text color={themeContext.onBackground} size={16}>
            을 입력하고 제출해주세요.
          </Text>
        </FlexBox>
        <Input
          type="text"
          placeholder="삭제를 원하신다면 이메일을 입력해주세요."
          {...emailInput}
        />
        <WithdrawalButton
          size="large"
          onClick={handleClickUserWithdrawal}
          disabled={!canWithdrawal}
        >
          회원 탈퇴
        </WithdrawalButton>
      </Wrapper>
    </ModalOverlay>
  );
};

const Wrapper = styled(FlexBox)`
  width: 100%;
  padding: 2rem 1rem;
  padding: 1rem 1rem 2rem;
`;

const CloseButton = styled.button`
  align-self: flex-end;
`;

const WithdrawalButton = styled(Button)`
  ${({ theme }) => css`
    margin-top: auto;
    background-color: ${theme.error};
    color: ${theme.onError};

    &:disabled {
      background-color: ${theme.disabled};
      color: ${theme.onError};
    }
  `}
`;
