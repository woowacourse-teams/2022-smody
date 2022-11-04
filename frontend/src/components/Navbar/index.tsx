import Feed from 'assets/feed.svg';
import Plus from 'assets/plus.svg';
import Profile from 'assets/profile.svg';
import { BsTrophyFill } from 'react-icons/bs';
import { ImFire } from 'react-icons/im';
import { Link } from 'react-router-dom';
import styled, { css } from 'styled-components';

import { Text, FlexBox } from 'components';
import { NavLinkProps } from 'components/Navbar/type';
import { useNavBar } from 'components/Navbar/useNavBar';

import { Z_INDEX } from 'constants/css';
import { CLIENT_PATH } from 'constants/path';

export const Navbar = () => {
  const { certColor, challengeColor, feedColor, profileColor, rankColor } = useNavBar();

  return (
    <Footer aria-label="하단 내브바">
      <nav aria-label="하단 내브바 링크 목록">
        <NavItemsContainer justifyContent="space-around" alignItems="center" as="ul">
          <li>
            <NavLink
              flexDirection="column"
              alignItems="center"
              gap="6px"
              as={Link}
              to={CLIENT_PATH.CHALLENGE_RANDOM}
            >
              <ImFire size={23} color={challengeColor} />
              <Text size={11} color={challengeColor} aria-label="챌린지 탭">
                챌린지
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
              <Text size={11} color={feedColor} aria-label="피드 탭">
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
              to={CLIENT_PATH.CERT}
              fill={certColor}
            >
              <Plus />
              <Text size={11} color={certColor} aria-label="인증 탭">
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
              to={CLIENT_PATH.RANK}
            >
              <BsTrophyFill size={23} color={rankColor} />
              <Text size={11} color={rankColor} aria-label="랭킹 탭">
                랭킹
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
              <Text size={11} color={profileColor} aria-label="프로필 탭">
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
    height: 4rem;
    background-color: ${theme.background};
    border-top: 1px solid ${theme.border};
    z-index: ${Z_INDEX.NAVBAR};
  `}
`;

const NavItemsContainer = styled(FlexBox)`
  width: 100%;
  height: 100%;
  margin: 0.8rem 0;
`;

const NavLink = styled(FlexBox)`
  ${({ fill }: NavLinkProps) => css`
    cursor: pointer;

    & svg {
      path {
        fill: ${fill};
        stroke: none;
      }
    }
  `}
`;
