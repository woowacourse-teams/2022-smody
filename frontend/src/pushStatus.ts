type PushStatus = {
  pushSupport: boolean;
  serviceWorkerRegistration: ServiceWorkerRegistration | null;
  pushSubscription: PushSubscription | null;
  notificationPermission: 'granted' | 'default' | 'denied';
};

export const pushStatus: PushStatus = {
  pushSupport: false,
  serviceWorkerRegistration: null,
  pushSubscription: null,
  notificationPermission: Notification.permission,
};
