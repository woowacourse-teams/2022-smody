import { NotificationMessageProps } from './type';
import { useNotificationMessage } from './useNotificationMessage';
import styled, { css } from 'styled-components';
import { parseTime } from 'utils';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text } from 'components';

export const NotificationMessage = ({
  updateNotificationCount,
}: NotificationMessageProps) => {
  const themeContext = useThemeContext();
  const { notifications, handleClickNotification } = useNotificationMessage({
    updateNotificationCount,
  });

  const formatTime = (time: string) => {
    const { year, month, date, hours, minutes } = parseTime(time);
    return `${year}년 ${month}월 ${date}일 ${hours}:${minutes}`;
  };

  return (
    <div>
      {notifications?.map(
        ({ pushNotificationId, message, pushTime, pathId, pushCase }) => (
          <NotificationWrapper
            key={pushNotificationId}
            onClick={() =>
              handleClickNotification({ pushNotificationId, pathId, pushCase })
            }
            flexDirection="column"
            gap="4px"
            alignItems="flex-start"
          >
            <Message color={themeContext.onSurface} size={14}>
              {message}
            </Message>
            <PushTimeText color={themeContext.mainText} size={12}>
              {formatTime(pushTime)}
            </PushTimeText>
          </NotificationWrapper>
        ),
      )}
    </div>
  );
};

const NotificationWrapper = styled(FlexBox)`
  ${({ theme }) => css`
    padding: 0.4rem 0.8rem;
    overflow: auto;
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
  display: flex;
  flex-wrap: wrap;
  width: 100%;
  word-wrap: break-word;
  word-break: break-all;
  white-space: normal;
`;

const PushTimeText = styled(Text)``;
