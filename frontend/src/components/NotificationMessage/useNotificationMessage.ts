import { UseNotificationMessageProps, NotificationHandlerProps } from './type';
import { queryKeys } from 'apis/constants';
import { useGetNotifications, useDeleteNotification } from 'apis/pushNotificationApi';
import { useQueryClient } from 'react-query';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import { CLIENT_PATH } from 'constants/path';

export const useNotificationMessage = ({
  updateNotificationCount,
}: UseNotificationMessageProps) => {
  const isLogin = useRecoilValue(isLoginState);
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const { data: notificationData } = useGetNotifications({
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
