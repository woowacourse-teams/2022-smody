import Feed from 'assets/feed.svg';
import Plus from 'assets/plus.svg';
import Profile from 'assets/profile.svg';
import Search from 'assets/search.svg';
import { Link } from 'react-router-dom';
import styled, { css } from 'styled-components';

import { Text, FlexBox } from 'components';
import { NavLinkProps } from 'components/Navbar/type';
import { useNavBar } from 'components/Navbar/useNavBar';

import { Z_INDEX } from 'constants/css';
import { CLIENT_PATH } from 'constants/path';

export const Navbar = () => {
  const { certColor, searchColor, feedColor, profileColor } = useNavBar();

  return (
    <Footer>
      <nav>
        <NavItemsContainer justifyContent="space-around" alignItems="center" as="ul">
          <li>
            <NavLink
              flexDirection="column"
              alignItems="center"
              gap="6px"
              as={Link}
              to={CLIENT_PATH.CERT}
              fill={certColor}
            >
              <Plus />
              <Text size={11} color={certColor}>
                인증
              </Text>
            </NavLink>
          </li>
          <li>
            <NavLink
              flexDirection="column"
              alignItems="center"
              gap="6px"
              as={Link}
              to={CLIENT_PATH.SEARCH}
              fill={searchColor}
            >
              <Search />
              <Text size={11} color={searchColor}>
                검색
              </Text>
            </NavLink>
          </li>
          <li>
            <NavLink
              flexDirection="column"
              alignItems="center"
              gap="6px"
              as={Link}
              to={CLIENT_PATH.FEED}
              fill={feedColor}
            >
              <Feed />
              <Text size={11} color={feedColor}>
                피드
              </Text>
            </NavLink>
          </li>
          <li>
            <NavLink
              flexDirection="column"
              alignItems="center"
              gap="6px"
              as={Link}
              to={CLIENT_PATH.PROFILE}
              fill={profileColor}
            >
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
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    height: 3.625rem;
    background-color: ${theme.background};
    border-top: 1px solid ${theme.border};
    z-index: ${Z_INDEX.NAVBAR};
  `}
`;

const NavItemsContainer = styled(FlexBox)`
  width: 100%;
  height: 100%;
  margin: 1rem 0;
  overflow-y: hidden;
`;

const NavLink = styled(FlexBox)`
  ${({ fill }: NavLinkProps) => css`
    cursor: pointer;

    & svg path {
      fill: ${fill};
      stroke: none;
    }
  `}
`;
