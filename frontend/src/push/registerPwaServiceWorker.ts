import { pushStatus } from 'push/pushStatus';

const isPwaServiceWorkerSupported =
  'serviceWorker' in navigator && 'Notification' in window && 'PushManager' in window;

const updatePushStatus = async (registration: ServiceWorkerRegistration) => {
  pushStatus.serviceWorkerRegistration = registration;
  pushStatus.pushSupport = !!registration?.pushManager;
  pushStatus.pushSubscription = await registration?.pushManager?.getSubscription();
  pushStatus.notificationPermission = Notification.permission;
};

const registerPwaServiceWorker = async () => {
  if (!isPwaServiceWorkerSupported) {
    return;
  }

  let registration = await navigator.serviceWorker.getRegistration();

  const newScriptPath = '/pwaServiceWorker.js';
  const oldScriptUrl = registration?.active?.scriptURL;
  const oldScriptPath = new URL(oldScriptUrl!).pathname;

  if (!registration || oldScriptPath !== newScriptPath) {
    registration = await navigator.serviceWorker.register(newScriptPath);
  }

  await updatePushStatus(registration);
};

export default registerPwaServiceWorker;
