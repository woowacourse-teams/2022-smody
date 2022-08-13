import { GetNotificationsResponse } from 'apis/pushNotificationApi/type';
import { useNavigate } from 'react-router-dom';
import styled, { css } from 'styled-components';
import { parseTime } from 'utils';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text } from 'components';

export const NotificationMessage = ({
  notifications,
}: {
  notifications?: GetNotificationsResponse;
}) => {
  const navigate = useNavigate();
  const themeContext = useThemeContext();

  const handleClickNotification = () => {
    navigate('/feed');
  };

  return (
    <div>
      {notifications?.map(({ pushNotificationId, message, pushTime }) => (
        <NotificationWrapper
          key={pushNotificationId}
          onClick={handleClickNotification}
          flexDirection="column"
          gap="4px"
          alignItems="flex-start"
        >
          <Message color={themeContext.onSurface} size={14}>
            {message}
          </Message>
          <PushTimeText color={themeContext.mainText} size={12}>
            {parseTime(pushTime).year}년 {parseTime(pushTime).month}월{' '}
            {parseTime(pushTime).date}일{'  '}
            {parseTime(pushTime).hours}시 {parseTime(pushTime).minutes}분
          </PushTimeText>
        </NotificationWrapper>
      ))}
    </div>
  );
};

const NotificationWrapper = styled(FlexBox)`
  ${({ theme }) => css`
    padding: 0.4rem 0.8rem;
    width: 100%;
    &:hover {
      background-color: ${theme.primary};
      & p:first-child {
        color: #fff;
      }
      & p:last-child {
        color: rgb(246, 221, 255);
      }
    }
  `}
`;

const Message = styled(Text)`
  /* height: 2rem; */
`;

const PushTimeText = styled(Text)``;
