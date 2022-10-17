import { UseSubscriptionButtonProps } from './type';
import { pushStatus } from 'pwa/pushStatus';
import { useEffect } from 'react';

import useSubscribe from 'hooks/useSubscribe';

export const useSubscriptionButton = ({
  updateIsSubscribed,
}: UseSubscriptionButtonProps) => {
  const { isSubscribed, subscribe, isLoadingSubscribe } = useSubscribe();

  useEffect(() => {
    updateIsSubscribed(isSubscribed);
  }, [isSubscribed]);

  const isAbleSubscribe =
    pushStatus.pushSupport && pushStatus.notificationPermission === 'granted';

  return { isSubscribed, subscribe, isLoadingSubscribe, isAbleSubscribe };
};
