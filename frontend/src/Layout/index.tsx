import { OutletWrapperProps } from 'Layout/type';
import { Suspense } from 'react';
import { Outlet } from 'react-router-dom';
import { useLocation } from 'react-router-dom';
import styled, { css } from 'styled-components';

import useMatchPath from 'hooks/useMatchPath';
import useThemeContext from 'hooks/useThemeContext';

import {
  ErrorBoundary,
  ErrorFallbackMain,
  FlexBox,
  Header,
  LoadingSpinner,
  Navbar,
  SnackBar,
  ScrollToTop,
} from 'components';

import { CLIENT_PATH } from 'constants/path';
import { TITLE_HEIGHT } from 'constants/style';

const PROFILE_PATH_PADDING = { pc: '0', tablet: '0', mobile: '0' };
const OTHER_PATH_PADDING = { pc: '10rem', tablet: '7rem', mobile: '1.25rem' };

const TITLE_USING_PATH_PADDING_TOP = TITLE_HEIGHT;
const TITLE_NOT_USING_PATH_PADDING_TOP = '0px';

export const Layout = () => {
  const themeContext = useThemeContext();
  const { pathname } = useLocation();

  const getPathMatchColor = useMatchPath(themeContext.secondary, themeContext.background);

  const getPathMatchHorizontalPadding = useMatchPath(
    PROFILE_PATH_PADDING,
    OTHER_PATH_PADDING,
  );

  const getPathMatchTopPadding = useMatchPath(
    TITLE_USING_PATH_PADDING_TOP,
    TITLE_NOT_USING_PATH_PADDING_TOP,
  );

  const bgColor = getPathMatchColor([
    CLIENT_PATH.CERT,
    CLIENT_PATH.HOME,
    CLIENT_PATH.CYCLE_DETAIL,
    CLIENT_PATH.RANK,
  ]);

  const horizontalPadding = getPathMatchHorizontalPadding(
    [CLIENT_PATH.PROFILE],
    [CLIENT_PATH.PROFILE_CHALLENGE_DETAIL],
  );

  const topPadding = getPathMatchTopPadding([
    CLIENT_PATH.CHALLENGE_DETAIL,
    CLIENT_PATH.PROFILE_CHALLENGE_DETAIL,
    CLIENT_PATH.CHALLENGE_CREATE,
    CLIENT_PATH.CYCLE_DETAIL,
    CLIENT_PATH.CYCLE_DETAIL_SHARE,
    CLIENT_PATH.FEED_DETAIL,
    CLIENT_PATH.PROFILE_EDIT,
  ]);

  return (
    <Wrapper>
      <Header bgColor={bgColor} />

      <OutletWrapper
        flexDirection="column"
        bgColor={bgColor}
        horizontalPadding={horizontalPadding}
        topPadding={topPadding}
      >
        <ErrorBoundary
          pathname={pathname}
          renderFallback={(renderFallbackParams) => (
            <ErrorFallbackMain {...renderFallbackParams} />
          )}
        >
          <Suspense fallback={<LoadingSpinner />}>
            <ScrollToTop pathname={pathname}>
              <Outlet />
            </ScrollToTop>
          </Suspense>
        </ErrorBoundary>
      </OutletWrapper>
      <Navbar />
      <SnackBar />
    </Wrapper>
  );
};

const Wrapper = styled.div``;

const OutletWrapper = styled(FlexBox)<OutletWrapperProps>`
  ${({ bgColor, horizontalPadding, topPadding }) => css`
    min-height: 100vh;
    background-color: ${bgColor};
    padding-top: ${topPadding};

    /* PC (해상도 1024px)*/
    @media all and (min-width: 1024px) {
      padding: calc(4.5rem + ${topPadding}) ${horizontalPadding.pc} 4.625rem;
    }

    /* 테블릿 가로, 테블릿 세로 (해상도 768px ~ 1023px)*/
    @media all and (min-width: 768px) and (max-width: 1023px) {
      padding: calc(4.5rem + ${topPadding}) ${horizontalPadding.tablet} 4.625rem;
    }

    /* 모바일 가로, 모바일 세로 (해상도 480px ~ 767px)*/
    @media all and (max-width: 767px) {
      padding: calc(4.5rem + ${topPadding}) ${horizontalPadding.mobile} 4.625rem;
    }
  `}
`;
