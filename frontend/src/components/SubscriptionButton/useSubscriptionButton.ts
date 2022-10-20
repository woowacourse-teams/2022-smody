import { UseSubscriptionButtonProps } from './type';
import { useDeleteAllNotification } from 'apis';
import { queryKeys } from 'apis/constants';
import { pushStatus } from 'pwa/pushStatus';
import { useEffect } from 'react';
import { useQueryClient } from 'react-query';

import useSubscribe from 'hooks/useSubscribe';

export const useSubscriptionButton = ({
  updateIsSubscribed,
}: UseSubscriptionButtonProps) => {
  const queryClient = useQueryClient();
  const { isSubscribed, subscribe, isLoadingSubscribe } = useSubscribe();
  const { mutate: deleteAllNotifications, isLoading: isLoadingDeleteAllNotifications } =
    useDeleteAllNotification({
      onSuccess: () => {
        queryClient.invalidateQueries(queryKeys.getNotifications);
      },
    });

  useEffect(() => {
    updateIsSubscribed(isSubscribed);
  }, [isSubscribed]);

  const handleClickDeleteAllNotifications = () => {
    if (window.confirm('알림을 모두 삭제하시겠습니까?')) {
      deleteAllNotifications();
    }
  };

  const isAbleSubscribe =
    pushStatus.pushSupport && pushStatus.notificationPermission === 'granted';

  return {
    isSubscribed,
    subscribe,
    isLoadingSubscribe,
    isAbleSubscribe,
    handleClickDeleteAllNotifications,
    isLoadingDeleteAllNotifications,
  };
};
