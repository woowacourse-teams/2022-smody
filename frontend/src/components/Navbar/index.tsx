import Feed from 'assets/feed.svg';
import Plus from 'assets/plus.svg';
import Profile from 'assets/profile.svg';
import Search from 'assets/search.svg';
import { Link } from 'react-router-dom';
import styled, { css } from 'styled-components';

import useMatchPath from 'hooks/useMatchPath';
import useThemeContext from 'hooks/useThemeContext';

import { Text, FlexBox } from 'components';
import { NavLinkProps } from 'components/Navbar/type';

import { CLIENT_PATH } from 'constants/path';

export const Navbar = () => {
  const themeContext = useThemeContext();
  const getPathMatchResult = useMatchPath(themeContext.primary, themeContext.disabled);

  const certColor = getPathMatchResult([CLIENT_PATH.CERT]);
  const searchColor = getPathMatchResult([
    CLIENT_PATH.SEARCH,
    CLIENT_PATH.CHALLENGE_DETAIL,
  ]);
  const feedColor = getPathMatchResult([CLIENT_PATH.FEED]);
  const profileColor = getPathMatchResult([
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
              <Text size={11} color={certColor}>
                인증
              </Text>
            </NavLink>
          </li>
          <li>
            <NavLink as={Link} to={CLIENT_PATH.SEARCH} fill={searchColor}>
              <Search />
              <Text size={11} color={searchColor}>
                검색
              </Text>
            </NavLink>
          </li>
          <li>
            <NavLink as={Link} to={CLIENT_PATH.FEED} fill={feedColor}>
              <Feed />
              <Text size={11} color={feedColor}>
                피드
              </Text>
            </NavLink>
          </li>
          <li>
            <NavLink as={Link} to={CLIENT_PATH.PROFILE} fill={profileColor}>
              <Profile />
              <Text size={11} color={profileColor}>
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
    position: sticky;
    left: 0;
    bottom: 0;
    right: 0;
    height: 3.625rem;
    background-color: ${theme.background};
    border-top: 1px solid ${theme.border};
  `}
`;

const NavItemsContainer = styled(FlexBox).attrs({
  justifyContent: 'space-around',
  alignItems: 'center',
})`
  width: 100%;
  height: 100%;
  margin: 1rem 0;
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
