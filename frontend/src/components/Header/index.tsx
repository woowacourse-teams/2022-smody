import { authApiClient } from 'apis/apiClient';
import { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue, useRecoilState } from 'recoil';
import { nicknameState, isLoginState } from 'recoil/auth/atoms';
import styled, { ThemeContext, css } from 'styled-components';

import { Logo, FlexBox, LinkText, Text } from 'components';

import { CLIENT_PATH } from 'constants/path';

export const Header = () => {
  const [isLogin, setIsLogin] = useRecoilState(isLoginState);
  const nickname = useRecoilValue(nicknameState);
  const themeContext = useContext(ThemeContext);
  const navigate = useNavigate();
  const handleLogout = () => {
    authApiClient.deleteAuth();
    setIsLogin(false);
    navigate(CLIENT_PATH.HOME);
  };

  return (
    <Wrapper>
      <Logo width="100" color={themeContext.onPrimary} />
      {isLogin ? (
        <UserWrapper>
          <Text size={20} color={themeContext.onPrimary}>
            {nickname}
          </Text>
          <button onClick={handleLogout}>
            <Text size={20} color={themeContext.primary}>
              로그아웃
            </Text>
          </button>
        </UserWrapper>
      ) : (
        <LinkText to="/login" size={20} color={themeContext.onPrimary}>
          로그인
        </LinkText>
      )}
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  justifyContent: 'space-between',
  alignItems: 'center',
})`
  ${({ theme }) => css`
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    background-color: ${theme.primary};
    padding: 1.2rem 4rem;
    z-index: 1000;
  `}
`;

const UserWrapper = styled(FlexBox).attrs({
  alignItems: 'center',
  gap: '1rem',
})`
  width: fit-content;
`;
