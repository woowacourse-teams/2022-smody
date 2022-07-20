import { WrapperProps } from 'Layout/type';
import { useContext } from 'react';
import { Outlet } from 'react-router-dom';
import styled, { ThemeContext, css } from 'styled-components';

import useMatchPath from 'hooks/useMatchPath';

import { FlexBox, Header, Navbar, SnackBar } from 'components';

import { CLIENT_PATH } from 'constants/path';

const PROFILE_PATH_PADDING = { pc: '0', tablet: '0', mobile: '0' };
const OTHER_PATH_PADDING = { pc: '10rem', tablet: '7rem', mobile: '1.25rem' };

export const Layout = () => {
  const themeContext = useContext(ThemeContext);

  const getPathMatchColor = useMatchPath(themeContext.secondary, themeContext.background);

  const getPathMatchHorizontalPadding = useMatchPath(
    PROFILE_PATH_PADDING,
    OTHER_PATH_PADDING,
  );

  const bgColor = getPathMatchColor([CLIENT_PATH.CERT]);
  const horizontalPadding = getPathMatchHorizontalPadding([CLIENT_PATH.PROFILE]);
  return (
    <>
      <Header />
      <Wrapper bgColor={bgColor} horizontalPadding={horizontalPadding}>
        <Outlet />
      </Wrapper>
      <Navbar />
      <SnackBar />
    </>
  );
};

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
})<WrapperProps>`
  ${({ bgColor, horizontalPadding }) => css`
    min-height: calc(100vh - 140px);
    background-color: ${bgColor};

    /* PC (해상도 1024px)*/
    @media all and (min-width: 1024px) {
      padding: 1rem ${horizontalPadding.pc};
    }

    /* 테블릿 가로, 테블릿 세로 (해상도 768px ~ 1023px)*/
    @media all and (min-width: 768px) and (max-width: 1023px) {
      padding: 1rem ${horizontalPadding.tablet};
    }

    /* 모바일 가로, 모바일 세로 (해상도 480px ~ 767px)*/
    @media all and (max-width: 767px) {
      padding: 1rem ${horizontalPadding.mobile};
    }
  `}
`;
