type PushStatus = {
  pushSupport: boolean;
  pushSubscription: PushSubscription | null;
  serviceWorkerRegistration?: ServiceWorkerRegistration;
  notificationPermission?: 'granted' | 'default' | 'denied';
};

export const isSupported = () =>
  'serviceWorker' in navigator && 'Notification' in window && 'PushManager' in window;

export const pushStatus: PushStatus = {
  pushSupport: false,
  pushSubscription: null,
  serviceWorkerRegistration: undefined,
  notificationPermission: undefined,
};
