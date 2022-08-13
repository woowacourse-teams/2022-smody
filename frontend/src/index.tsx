import App from 'App';
import { pushStatus } from 'pushStatus';
import { createRoot } from 'react-dom/client';
import { RecoilRoot } from 'recoil';

const registerPushServiceWorker = async () => {
  let registration = await navigator.serviceWorker.getRegistration();
  if (!registration) {
    registration = await navigator.serviceWorker.register('pushServiceWorker.js');
  }

  pushStatus.serviceWorkerRegistration = registration ?? null;
  pushStatus.pushSupport = !!registration?.pushManager;
  pushStatus.pushSubscription = await registration?.pushManager?.getSubscription();
};

if (process.env.NODE_ENV === 'production' && 'serviceWorker' in navigator) {
  registerPushServiceWorker();
}

if (process.env.NODE_ENV === 'development' && !process.env.IS_LOCAL) {
  const { mockServiceWorker } = require('./mocks/browser');
  mockServiceWorker.start();
}

const rootElement = document.getElementById('root');
const root = createRoot(rootElement!);
root.render(
  <RecoilRoot>
    <App />
  </RecoilRoot>,
);
