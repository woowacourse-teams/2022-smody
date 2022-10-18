import { UseSubscriptionButtonProps } from './type';
import { useDeleteAllNotification } from 'apis';
import { pushStatus } from 'pwa/pushStatus';
import { useEffect } from 'react';

import useSubscribe from 'hooks/useSubscribe';

export const useSubscriptionButton = ({
  updateIsSubscribed,
}: UseSubscriptionButtonProps) => {
  const { isSubscribed, subscribe, isLoadingSubscribe } = useSubscribe();
  const { mutate: deleteAllNotifications, isLoading: isLoadingDeleteAllNotifications } =
    useDeleteAllNotification();

  useEffect(() => {
    updateIsSubscribed(isSubscribed);
  }, [isSubscribed]);

  const handleClickDeleteAllNotifications = () => {
    deleteAllNotifications();
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
