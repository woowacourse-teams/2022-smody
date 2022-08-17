import { OutletWrapperProps } from 'Layout/type';
import { Suspense } from 'react';
import { Outlet } from 'react-router-dom';
import styled, { css } from 'styled-components';

import useMatchPath from 'hooks/useMatchPath';
import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Header, LoadingSpinner, Navbar, SnackBar } from 'components';

import { CLIENT_PATH } from 'constants/path';

const PROFILE_PATH_PADDING = { pc: '0', tablet: '0', mobile: '0' };
const OTHER_PATH_PADDING = { pc: '10rem', tablet: '7rem', mobile: '1.25rem' };

export const Layout = () => {
  const themeContext = useThemeContext();

  const getPathMatchColor = useMatchPath(themeContext.secondary, themeContext.background);

  const getPathMatchHorizontalPadding = useMatchPath(
    PROFILE_PATH_PADDING,
    OTHER_PATH_PADDING,
  );

  const bgColor = getPathMatchColor([
    CLIENT_PATH.CERT,
    CLIENT_PATH.HOME,
    CLIENT_PATH.CYCLE_DETAIL,
  ]);
  const horizontalPadding = getPathMatchHorizontalPadding([CLIENT_PATH.PROFILE]);

  return (
    <Wrapper>
      <Header bgColor={bgColor} />

      <OutletWrapper
        flexDirection="column"
        bgColor={bgColor}
        horizontalPadding={horizontalPadding}
      >
        <Suspense fallback={<LoadingSpinner />}>
          <Outlet />
        </Suspense>
      </OutletWrapper>
      <Navbar />
      <SnackBar />
    </Wrapper>
  );
};

const Wrapper = styled.div`
  /* min-width: 400px; */
`;

const OutletWrapper = styled(FlexBox)<OutletWrapperProps>`
  ${({ bgColor, horizontalPadding }) => css`
    min-height: calc(100vh - 119px);
    background-color: ${bgColor};

    /* PC (해상도 1024px)*/
    @media all and (min-width: 1024px) {
      padding: 1rem ${horizontalPadding.pc} 4.625rem;
    }

    /* 테블릿 가로, 테블릿 세로 (해상도 768px ~ 1023px)*/
    @media all and (min-width: 768px) and (max-width: 1023px) {
      padding: 1rem ${horizontalPadding.tablet} 4.625rem;
    }

    /* 모바일 가로, 모바일 세로 (해상도 480px ~ 767px)*/
    @media all and (max-width: 767px) {
      padding: 1rem ${horizontalPadding.mobile} 4.625rem;
    }
  `}
`;
