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
    position: sticky;
    top: 0;
    left: 0;
    right: 0;
    background-color: ${bgColor};

    /* PC (해상도 1024px)*/
    @media all and (min-width: 1024px) {
      padding: 1.5rem 10rem 1rem;
    }

    /* 테블릿 가로, 테블릿 세로 (해상도 768px ~ 1023px)*/
    @media all and (min-width: 768px) and (max-width: 1023px) {
      padding: 1.5rem 7rem 1rem;
    }

    /* 모바일 가로, 모바일 세로 (해상도 480px ~ 767px)*/
    @media all and (max-width: 767px) {
      padding: 1.5rem 1.25rem 1rem;
    }
  `}
`;
