import Feed from 'assets/feed.svg';
import Plus from 'assets/plus.svg';
import Profile from 'assets/profile.svg';
import Search from 'assets/search.svg';
import { useContext } from 'react';
import { Link, useLocation } from 'react-router-dom';
import styled, { css } from 'styled-components';
import { ThemeContext } from 'styled-components';

import { Text, FlexBox } from 'components';
import { NavLinkProps } from 'components/Navbar/type';

import { CLIENT_PATH } from 'constants/path';

export const Navbar = () => {
  const themeContext = useContext(ThemeContext);
  const { pathname } = useLocation();
  const pathMatchRoute = (routes: string[]) => {
    let matchResultColor = themeContext.disabled;
    routes.forEach((route) => {
      if (pathname.includes(route)) {
        matchResultColor = themeContext.primary;
        return;
      }
    });
    return matchResultColor;
  };

  const certColor = pathMatchRoute([CLIENT_PATH.CERT]);
  const searchColor = pathMatchRoute([CLIENT_PATH.SEARCH, CLIENT_PATH.CHALLENGE_DETAIL]);
  const feedColor = pathMatchRoute([CLIENT_PATH.FEED]);
  const profileColor = pathMatchRoute([
    CLIENT_PATH.LOGIN,
    CLIENT_PATH.SIGN_UP,
    CLIENT_PATH.PROFILE,
  ]);

  return (
    <Footer>
      <nav>
        <NavItemsContainer as="ul">
          <li>
            <NavLink as={Link} to={CLIENT_PATH.CERT} fill={certColor}>
              <Plus />
              <Text size={14} color={certColor}>
                인증
              </Text>
            </NavLink>
          </li>
          <li>
            <NavLink as={Link} to={CLIENT_PATH.SEARCH} fill={searchColor}>
              <Search />
              <Text size={14} color={searchColor}>
                검색
              </Text>
            </NavLink>
          </li>
          <li>
            <NavLink as={Link} to={CLIENT_PATH.FEED} fill={feedColor}>
              <Feed />
              <Text size={14} color={feedColor}>
                피드
              </Text>
            </NavLink>
          </li>
          <li>
            <NavLink as={Link} to={CLIENT_PATH.LOGIN} fill={profileColor}>
              <Profile />
              <Text size={14} color={profileColor}>
                프로필
              </Text>
            </NavLink>
          </li>
        </NavItemsContainer>
      </nav>
    </Footer>
  );
};

const Footer = styled.footer`
  ${({ theme }) => css`
    position: fixed;
    left: 0;
    bottom: 0;
    right: 0;
    height: 4.5rem;
    background-color: ${theme.background};
    border-top: 1px solid ${theme.border};
    z-index: 1000;
  `}
`;

const NavItemsContainer = styled(FlexBox).attrs({
  justifyContent: 'space-around',
  alignItems: 'center',
})`
  width: 100%;
  height: 100%;
  margin-top: 1rem;
  overflow-y: hidden;
`;

const NavLink = styled(FlexBox).attrs({
  flexDirection: 'column',
  alignItems: 'center',
  gap: '6px',
})`
  ${({ fill }: NavLinkProps) => css`
    cursor: pointer;

    & svg path {
      fill: ${fill};
      stroke: none;
    }
  `}
`;
