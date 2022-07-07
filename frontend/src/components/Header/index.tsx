import { useContext } from 'react';
import { useRecoilValue } from 'recoil';
import { nicknameState, isLoginState } from 'recoil/auth/atoms';
import styled, { ThemeContext, css } from 'styled-components';

import { Logo, FlexBox, LinkText, Text } from 'components';

export const Header = () => {
  const isLogin = useRecoilValue(isLoginState);

  const nickname = useRecoilValue(nicknameState);
  const themeContext = useContext(ThemeContext);
  return (
    <Wrapper>
      <Logo width="100" color={themeContext.onPrimary} />
      {isLogin ? (
        <Text size={20} color={themeContext.onPrimary}>
          {nickname}
        </Text>
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
