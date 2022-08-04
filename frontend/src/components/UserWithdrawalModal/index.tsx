import { useDeleteMyInfo } from 'apis';
import { authApiClient } from 'apis/apiClient';
import { MouseEventHandler } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';
import styled, { css } from 'styled-components';

import useInput from 'hooks/useInput';
import useSnackBar from 'hooks/useSnackBar';
import useThemeContext from 'hooks/useThemeContext';

import { Button, FlexBox, ModalOverlay, Text, Input } from 'components';
import { UserWithdrawalModalProps } from 'components/UserWithdrawalModal/type';

import { CLIENT_PATH } from 'constants/path';

export const UserWithdrawalModal = ({
  email,
  handleCloseModal,
}: UserWithdrawalModalProps) => {
  const themeContext = useThemeContext();
  const navigate = useNavigate();
  const renderSnackBar = useSnackBar();
  const emailInput = useInput('');

  const setIsLogin = useSetRecoilState(isLoginState);

  const { isLoading: isLoadingDeleteMyInfo, mutate: deleteMyInfo } = useDeleteMyInfo({
    onSuccess: () => {
      renderSnackBar({
        message:
          '회원 정보를 성공적으로 삭제했습니다. 그동안 스모디를 이용해 주셔서 감사합니다.',
        status: 'SUCCESS',
      });

      authApiClient.deleteAuth();
      setIsLogin(false);

      navigate(CLIENT_PATH.HOME);
    },
  });

  const canWithdrawal = email === emailInput.value;

  const handleClickUserWithdrawal: MouseEventHandler<HTMLButtonElement> = () => {
    deleteMyInfo();
  };

  return (
    <ModalOverlay handleCloseModal={handleCloseModal}>
      <Wrapper flexDirection="column" alignItems="center" gap="1.5rem">
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
