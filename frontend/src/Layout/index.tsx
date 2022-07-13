import { WrapperProps } from 'Layout/type';
import { useContext } from 'react';
import { Outlet } from 'react-router-dom';
import styled, { ThemeContext, css } from 'styled-components';

import useMatchPath from 'hooks/useMatchPath';

import { FlexBox, Header, Navbar } from 'components';

import { CLIENT_PATH } from 'constants/path';

export const Layout = () => {
  const themeContext = useContext(ThemeContext);

  const getPathMatchResult = useMatchPath(
    themeContext.secondary,
    themeContext.background,
  );

  const bgColor = getPathMatchResult([CLIENT_PATH.CERT]);

  return (
    <>
      <Header />
      <Wrapper bgColor={bgColor}>
        <Outlet />
      </Wrapper>
      <Navbar />
    </>
  );
};

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
})<WrapperProps>`
  ${({ bgColor }) => css`
    background-color: ${bgColor};

    /* PC (해상도 1024px)*/
    @media all and (min-width: 1024px) {
      padding: 6rem 10rem 7rem;
    }

    /* 테블릿 가로, 테블릿 세로 (해상도 768px ~ 1023px)*/
    @media all and (min-width: 768px) and (max-width: 1023px) {
      padding: 6rem 7rem 7rem;
    }

    /* 모바일 가로, 모바일 세로 (해상도 480px ~ 767px)*/
    @media all and (max-width: 767px) {
      padding: 6rem 1.25rem 7rem;
    }
  `}
`;
