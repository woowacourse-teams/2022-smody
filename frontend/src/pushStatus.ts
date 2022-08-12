type PushStatus = {
  pushSupport: boolean;
  serviceWorkerRegistration: ServiceWorkerRegistration | null;
  pushSubscription: PushSubscription | null;
  notificationPermission: 'granted' | 'default' | 'denied' | false;
};

const isSupported = () =>
  'Notification' in window && 'serviceWorker' in navigator && 'PushManager' in window;

export const pushStatus: PushStatus = {
  pushSupport: false,
  serviceWorkerRegistration: null,
  pushSubscription: null,
  notificationPermission: isSupported() && Notification.permission,
};

// if (isSupported()) {
//   const pushStatus.notificationPermission = Notification.permission;
// }

// console.log('isSupported', isSupported);
