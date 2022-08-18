import { UseDropdownProps } from './type';
import { useGetNotifications } from 'apis/pushNotificationApi';
import { pushStatus } from 'push/pushStatus';
import { MouseEventHandler, useState, useEffect } from 'react';

export const useDropdown = ({
  updateNotificationCount,
  updateIsSubscribed,
}: UseDropdownProps) => {
  const [isDropdownToggled, setDropdownToggled] = useState(false);

  useGetNotifications({
    useErrorBoundary: false,
    onSuccess: ({ data: notifications }) => {
      updateNotificationCount(notifications.length);
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
