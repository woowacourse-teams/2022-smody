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
    },
  });

  const handleClickNotification = ({
    pushNotificationId,
    pathId,
    type,
  }: NotificationHandlerProps) => {
    deleteNotification({ pushNotificationId });
    if (type === 'subscription') {
      navigate(`/search`);
    }
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

  return { handleClickNotification };
};
