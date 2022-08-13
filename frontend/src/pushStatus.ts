type PushStatus = {
  pushSupport: boolean;
  serviceWorkerRegistration: ServiceWorkerRegistration | null;
  pushSubscription: PushSubscription | null;
  notificationPermission: 'granted' | 'default' | 'denied' | false;
};

export const isSupported = () =>
  'serviceWorker' in navigator && 'Notification' in window && 'PushManager' in window;

export const pushStatus: PushStatus = {
  pushSupport: isSupported(),
  serviceWorkerRegistration: null,
  pushSubscription: null,
  notificationPermission: isSupported() && Notification.permission,
};
