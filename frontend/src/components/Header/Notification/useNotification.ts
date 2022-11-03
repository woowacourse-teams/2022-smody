import { useState } from 'react';
import { useLocation } from 'react-router-dom';

export const useNotification = () => {
  const [notificationCount, setNotificationCount] = useState<number | undefined>(
    undefined,
  );
  const [isSubscribed, setIsSubscribed] = useState(false);
  const { pathname } = useLocation();

  const updateNotificationCount = (updatedNotificationCount: number) => {
    setNotificationCount(updatedNotificationCount);
  };

  const updateIsSubscribed = (updatedIsSubscribed: boolean) => {
    setIsSubscribed(updatedIsSubscribed);
  };

  return {
    notificationCount,
    isSubscribed,
    updateNotificationCount,
    updateIsSubscribed,
    pathname,
  };
};
