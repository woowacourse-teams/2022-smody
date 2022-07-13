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
    padding: 6rem 20px 7rem;
  `}
`;
