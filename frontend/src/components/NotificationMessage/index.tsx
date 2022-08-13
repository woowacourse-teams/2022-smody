import { queryKeys } from 'apis/constants';
import { useDeleteNotification } from 'apis/pushNotificationApi';
import { GetNotificationsResponse } from 'apis/pushNotificationApi/type';
import { useQueryClient } from 'react-query';
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
  const queryClient = useQueryClient();
  const { mutate: deleteNotification } = useDeleteNotification({
    onSuccess: () => {
      queryClient.invalidateQueries(queryKeys.getNotifications);
    },
  });
  const handleClickNotification = ({
    pushNotificationId,
    pathId,
    type,
  }: {
    pushNotificationId: number;
    pathId?: number;
    type?: string;
  }) => {
    deleteNotification({ pushNotificationId });
    if (type === 'comment') {
      navigate(`/feed/detail/${pathId}`);
    }
    if (type === 'challenge') {
      navigate(`/cycle/detail/${pathId}`);
    }
    // TODO : 네비게이트 시킬 라우터 패스, 백엔드로부터 받기
    // 인증 마감 임박 알릴 경우 -> cycleId 받기 '/cycle/detail/:cycleId',
    // 댓글 달릴 경우 -> cycleDetailId 받기 '/feed/detail/:cycleDetailId',
  };

  return (
    <div>
      {notifications?.map(({ pushNotificationId, message, pushTime, pathId, type }) => (
        <NotificationWrapper
          key={pushNotificationId}
          onClick={() => handleClickNotification({ pushNotificationId, pathId, type })}
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
