import { NotificationHandlerProps } from './type';
import { queryKeys } from 'apis/constants';
import { useDeleteNotification } from 'apis/pushNotificationApi';
import { useQueryClient } from 'react-query';
import { useNavigate } from 'react-router-dom';

export const useNotificationMessage = () => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const { mutate: deleteNotification } = useDeleteNotification({
    onSuccess: () => {
      queryClient.invalidateQueries(queryKeys.getNotifications);
      console.log('딜리트');
    },
  });

  const handleClickNotification = ({
    pushNotificationId,
    pathId,
    pushCase,
  }: NotificationHandlerProps) => {
    deleteNotification({ pushNotificationId });
    if (pushCase === 'subscription') {
      navigate(`/search`);
    }
    if (pushCase === 'comment') {
      navigate(`/feed/detail/${pathId}`);
    }
    if (pushCase === 'challenge') {
      navigate(`/cycle/detail/${pathId}`);
    }
  };

  return { handleClickNotification };
};
