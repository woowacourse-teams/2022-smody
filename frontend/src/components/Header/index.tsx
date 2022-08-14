import { HeaderProps } from './type';
import { useHeader } from './useHeader';
import { isDev, isLocal } from 'env';
import { setBadge } from 'push/badge';
import { useEffect } from 'react';
import { Link } from 'react-router-dom';
import styled, { css } from 'styled-components';

import useSubscribe from 'hooks/useSubscribe';
import useThemeContext from 'hooks/useThemeContext';

import {
  Logo,
  FlexBox,
  Button,
  DarkModeButton,
  Dropdown,
  Bell,
  SubscriptionButton,
} from 'components';

import { Z_INDEX } from 'constants/css';
import { CLIENT_PATH } from 'constants/path';

const itemList = [
  {
    text: '운동 챌린지를 성공하셨습니다',
    linkTo: '/cert',
  },
  {
    text: '빅터님이 댓글을 달았습니다',
    linkTo: '/feed',
  },
  {
    text: '더즈님이 댓글을 달았습니다',
    linkTo: '/profile',
  },
  {
    text: '미라클 모닝 인증 마감까지 2시간 남았습니다',
    linkTo: '/search',
  },
];

export const Header = ({ bgColor }: HeaderProps) => {
  const themeContext = useThemeContext();
  const { isDark, isLogin, handleDarkToggle, handleLoginButton } = useHeader();
  const { isSubscribed, subscribe, isLoadingSubscribe } = useSubscribe();
  {
    /* TODO badgeNumber에 백엔드에서 받은 알림 count 넣기 */
  }
  const badgeNumber = 4;
  useEffect(() => {
    setBadge(badgeNumber);
  }, []);

  return (
    <Wrapper bgColor={bgColor} justifyContent="space-between" alignItems="center">
      <Link to={CLIENT_PATH.HOME}>
        <Logo isAnimated={false} width="100" color={themeContext.primary} />
        {isDev && 'DEV'}
        {isLocal && 'LOCAL'}
      </Link>
      <FlexBox gap="1rem">
        <DarkModeButton checked={isDark} handleChange={handleDarkToggle} />
        {isLogin ? (
          <Dropdown button={<Bell count={4} isSubscribed={isSubscribed} />}>
            <SubscriptionButton
              isSubscribed={isSubscribed}
              subscribe={subscribe}
              isLoadingSubscribe={isLoadingSubscribe}
            />

            {itemList.map(({ text, linkTo }) => (
              <Item key={text} to={linkTo}>
                {text}
              </Item>
            ))}
          </Dropdown>
        ) : (
          <Button size="small" onClick={handleLoginButton}>
            로그인
          </Button>
        )}
      </FlexBox>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox)<HeaderProps>`
  ${({ bgColor }) => css`
    position: sticky;
    top: 0;
    left: 0;
    right: 0;
    background-color: ${bgColor};
    z-index: ${Z_INDEX.HEADER};

    /* PC (해상도 1024px)*/
    @media all and (min-width: 1024px) {
      padding: 1rem 10rem;
    }

    /* 테블릿 가로, 테블릿 세로 (해상도 768px ~ 1023px)*/
    @media all and (min-width: 768px) and (max-width: 1023px) {
      padding: 1rem 7rem;
    }

    /* 모바일 가로, 모바일 세로 (해상도 480px ~ 767px)*/
    @media all and (max-width: 767px) {
      padding: 1rem 1.25rem;
    }
  `}
`;

const Item = styled(Link)`
  ${({ theme }) => css`
    height: 2rem;
    display: flex;
    align-items: center;
    padding: 0 0.8rem;
    width: 100%;
    &:hover {
      background-color: ${theme.primary};
      color: ${theme.onPrimary};
    }
  `}
`;
