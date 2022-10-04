import { UseNotificationMessageProps, NotificationHandlerProps } from './type';
import { queryKeys } from 'apis/constants';
import { useGetNotifications, useDeleteNotification } from 'apis/pushNotificationApi';
import { setBadge } from 'pwa/badge';
import { useEffect } from 'react';
import { useQueryClient } from 'react-query';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import useSnackBar from 'hooks/useSnackBar';

import { CLIENT_PATH } from 'constants/path';

const messageChannel = new MessageChannel();
// 우선 채널 설정 위해 서비스워커에 port2를 전달한다
if ('serviceWorker' in navigator && navigator.serviceWorker.controller) {
  navigator.serviceWorker.controller.postMessage(
    {
      type: 'INIT_PORT',
    },
    [messageChannel.port2],
  );
}

export const useNotificationMessage = ({
  updateNotificationCount,
}: UseNotificationMessageProps) => {
  const isLogin = useRecoilValue(isLoginState);
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const renderSnackBar = useSnackBar();

  const { data: notificationData, refetch } = useGetNotifications({
    enabled: isLogin,
    onSuccess: ({ data: notifications }) => {
      updateNotificationCount(notifications.length);
    },
  });

  const { mutate: deleteNotification } = useDeleteNotification({
    onSuccess: () => {
      queryClient.invalidateQueries(queryKeys.getNotifications);
    },
  });

  const notifications = notificationData?.data;
  const badgeNumber = notifications?.length;

  useEffect(() => {
    setBadge(badgeNumber);
  }, [setBadge, badgeNumber]);

  // 메시지 수신 - 클라이언트는 port1을 사용하고 있다
  // 상대 포트인 port2(서비스워커)에서 전달되는 메시지 수신한다
  messageChannel.port1.onmessage = (event) => {
    const message = event.data.message;
    refetch();
    renderSnackBar({
      status: 'SUCCESS',
      message: `[알림] ${message}`,
    });
  };

  const handleClickNotification = ({
    pushNotificationId,
    pathId,
    pushCase,
  }: NotificationHandlerProps) => {
    deleteNotification({ pushNotificationId });
    if (pushCase === 'subscription') {
      navigate(`${CLIENT_PATH.PROFILE}`);
    }
    if (pushCase === 'comment') {
      navigate(`${CLIENT_PATH.FEED_DETAIL}/${pathId}`);
    }
    if (pushCase === 'challenge') {
      navigate(`${CLIENT_PATH.CYCLE_DETAIL}/${pathId}`);
    }
  };

  return { notifications, handleClickNotification };
};
