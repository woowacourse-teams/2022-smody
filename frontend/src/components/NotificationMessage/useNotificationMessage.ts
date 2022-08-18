import { UseNotificationMessageProps, NotificationHandlerProps } from './type';
import { queryKeys } from 'apis/constants';
import { useGetNotifications, useDeleteNotification } from 'apis/pushNotificationApi';
import { setBadge } from 'push/badge';
import { useEffect } from 'react';
import { useQueryClient } from 'react-query';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import useSnackBar from 'hooks/useSnackBar';

import { CLIENT_PATH } from 'constants/path';

const broadcast = new BroadcastChannel('push-channel');

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

  broadcast.onmessage = (event) => {
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
