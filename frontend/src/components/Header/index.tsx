import { useContext } from 'react';
import { FaBell } from 'react-icons/fa';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';
import styled, { ThemeContext, css } from 'styled-components';

import useMatchPath from 'hooks/useMatchPath';

import { Logo, FlexBox, LinkText } from 'components';
import { WrapperProps } from 'components/Header/type';

import { CLIENT_PATH } from 'constants/path';

export const Header = () => {
  const isLogin = useRecoilValue(isLoginState);
  const themeContext = useContext(ThemeContext);
  const getPathMatchResult = useMatchPath(
    themeContext.secondary,
    themeContext.background,
  );

  const bgColor = getPathMatchResult([CLIENT_PATH.CERT]);

  return (
    <Wrapper bgColor={bgColor}>
      <Logo width="100" color={themeContext.primary} />
      {isLogin ? (
        <FaBell size={20} color={themeContext.primary} />
      ) : (
        <LinkText to="/login" size={20} color={themeContext.primary}>
          로그인
        </LinkText>
      )}
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  justifyContent: 'space-between',
  alignItems: 'center',
})<WrapperProps>`
  ${({ bgColor }) => css`
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    background-color: ${bgColor};
    padding: 1.2rem 4rem;
    z-index: 1000;
  `}
`;
