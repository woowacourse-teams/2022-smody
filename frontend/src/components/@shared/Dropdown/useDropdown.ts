import { UseDropdownProps } from './type';
import { useGetNotifications } from 'apis/pushNotificationApi';
import { setBadge } from 'pwa/badge';
import { pushStatus } from 'pwa/pushStatus';
import { MouseEventHandler, useState, useEffect } from 'react';

import useDetectPageChange from 'hooks/useDetectPageChange';

export const useDropdown = ({
  updateNotificationCount,
  updateIsSubscribed,
}: UseDropdownProps) => {
  const [isDropdownToggled, setDropdownToggled] = useState(false);

  const resetDropdown = () => {
    if (isDropdownToggled) {
      setDropdownToggled(false);
    }
  };

  useDetectPageChange(resetDropdown);

  useGetNotifications({
    useErrorBoundary: false,
    onSuccess: ({ data: notifications }) => {
      const notificationCount = notifications.length;

      updateNotificationCount(notifications.length);
      setBadge(notificationCount);
    },
  });

  const isAlreadySubscribed = !!pushStatus.pushSubscription;

  useEffect(() => {
    updateIsSubscribed(isAlreadySubscribed);
  }, [isAlreadySubscribed]);

  const showDropdownMenu = () => {
    setDropdownToggled(true);
  };

  const hideDropdownMenu: MouseEventHandler<HTMLDivElement> = (event) => {
    if (event.currentTarget === event.target) {
      setDropdownToggled(false);
    }
  };

  const onSelectMenu = () => {
    setDropdownToggled(false);
  };

  return { isDropdownToggled, showDropdownMenu, hideDropdownMenu, onSelectMenu };
};
