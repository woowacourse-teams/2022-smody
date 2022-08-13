import { pushStatus } from 'push/pushStatus';

const isPushServiceWorkerSupported = () =>
  'serviceWorker' in navigator && 'Notification' in window && 'PushManager' in window;

const registerPushServiceWorker = async () => {
  if (!isPushServiceWorkerSupported()) {
    return;
  }

  let registration = await navigator.serviceWorker.getRegistration();

  if (!registration) {
    registration = await navigator.serviceWorker.register('pushServiceWorker.js');
  }

  pushStatus.serviceWorkerRegistration = registration;
  pushStatus.pushSupport = !!registration?.pushManager;
  pushStatus.pushSubscription = await registration?.pushManager?.getSubscription();
  pushStatus.notificationPermission = Notification.permission;
};

export default registerPushServiceWorker;
