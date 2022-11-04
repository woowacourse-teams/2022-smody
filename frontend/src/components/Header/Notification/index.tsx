import { useNotification } from './useNotification';

import {
  Dropdown,
  Bell,
  SubscriptionButton,
  NotificationMessage,
  ErrorBoundary,
  ErrorFallbackSubscriptionButton,
  ErrorFallbackNotificationMessage,
} from 'components';

export const Notification = () => {
  const {
    notificationCount,
    isSubscribed,
    updateNotificationCount,
    updateIsSubscribed,
    pathname,
  } = useNotification();

  return (
    <Dropdown
      button={<Bell count={notificationCount} isSubscribed={isSubscribed} />}
      nonLinkableElement={
        <ErrorBoundary
          pathname="pathname"
          renderFallback={(renderFallbackParams) => (
            <ErrorFallbackSubscriptionButton {...renderFallbackParams} />
          )}
        >
          <SubscriptionButton updateIsSubscribed={updateIsSubscribed} />
        </ErrorBoundary>
      }
      updateNotificationCount={updateNotificationCount}
      updateIsSubscribed={updateIsSubscribed}
    >
      <ErrorBoundary
        pathname={pathname}
        renderFallback={(renderFallbackParams) => (
          <ErrorFallbackNotificationMessage {...renderFallbackParams} />
        )}
      >
        <NotificationMessage updateNotificationCount={updateNotificationCount} />
      </ErrorBoundary>
    </Dropdown>
  );
};
