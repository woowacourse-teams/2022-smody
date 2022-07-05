import Home from 'assets/home.svg';
import Plus from 'assets/plus.svg';
import Profile from 'assets/profile.svg';
import Search from 'assets/search.svg';
import { useContext } from 'react';
import { Link, useLocation } from 'react-router-dom';
import styled, { css } from 'styled-components';
import { ThemeContext } from 'styled-components';

import { Text, FlexBox } from 'components';
import { NavLinkProps } from 'components/Navbar/type';

export const Navbar = () => {
  const themeContext = useContext(ThemeContext);
  const { pathname } = useLocation();
  const pathMatchRoute = (route: string) => {
    if (route === pathname) {
      return {
        fill: themeContext.primary,
        stroke: themeContext.primary,
      };
    }
    return { fill: themeContext.background, stroke: themeContext.blur };
  };

  const homeColor = pathMatchRoute('/home');
  const searchColor = pathMatchRoute('/search');
  const certColor = pathMatchRoute('/cert');
  const profileColor = pathMatchRoute('/login');

  return (
    <Footer>
      <nav>
        <NavItemsContainer as="ul">
          <li>
            <NavLink as={Link} to="/home" {...homeColor}>
              <Home />
              <Text size={14} color={homeColor.stroke}>
                홈
              </Text>
            </NavLink>
          </li>
          <li>
            <NavLink as={Link} to="/search" {...searchColor}>
              <Search />
              <Text size={14} color={searchColor.stroke}>
                검색
              </Text>
            </NavLink>
          </li>
          <li>
            <NavLink as={Link} to="/cert" {...certColor}>
              <Plus />
              <Text size={14} color={certColor.stroke}>
                인증
              </Text>
            </NavLink>
          </li>
          <li>
            <NavLink as={Link} to="/login" {...profileColor}>
              <Profile />
              <Text size={14} color={profileColor.stroke}>
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
  ${({ fill, stroke }: NavLinkProps) => css`
    cursor: pointer;

    & svg path {
      fill: ${fill};
      stroke: ${stroke};
    }
  `}
`;
