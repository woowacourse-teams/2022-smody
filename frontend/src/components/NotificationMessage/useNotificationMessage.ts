import { NotificationHandlerProps } from './type';
import { queryKeys } from 'apis/constants';
import { useDeleteNotification } from 'apis/pushNotificationApi';
import { useQueryClient } from 'react-query';
import { useNavigate } from 'react-router-dom';

import { CLIENT_PATH } from 'constants/path';

export const useNotificationMessage = () => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const { mutate: deleteNotification } = useDeleteNotification({
    onSuccess: () => {
      queryClient.invalidateQueries(queryKeys.getNotifications);
    },
  });

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

  return { handleClickNotification };
};
