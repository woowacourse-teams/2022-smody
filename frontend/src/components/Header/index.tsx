import { HeaderProps } from './type';
import { useHeader } from './useHeader';
import { useGetNotifications } from 'apis/pushNotificationApi';
import { isDev, isLocal } from 'env';
import { setBadge } from 'push/badge';
import { useEffect } from 'react';
import { Link } from 'react-router-dom';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import {
  Logo,
  FlexBox,
  Button,
  DarkModeButton,
  Dropdown,
  Bell,
  SubscriptionButton,
  NotificationMessage,
} from 'components';

import { Z_INDEX } from 'constants/css';
import { CLIENT_PATH } from 'constants/path';

export const Header = ({ bgColor }: HeaderProps) => {
  const themeContext = useThemeContext();
  const {
    isDark,
    isLogin,
    handleDarkToggle,
    handleLoginButton,
    notifications,
    isSubscribed,
    subscribe,
    isLoadingSubscribe,
  } = useHeader();

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
          <Dropdown
            button={<Bell count={notifications?.length} isSubscribed={isSubscribed} />}
            nonLinkableElement={
              <SubscriptionButton
                isSubscribed={isSubscribed}
                subscribe={subscribe}
                isLoadingSubscribe={isLoadingSubscribe}
              />
            }
          >
            <NotificationMessage notifications={notifications} />
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
