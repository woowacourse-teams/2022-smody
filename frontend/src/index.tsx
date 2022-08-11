import App from 'App';
import { pushStatus } from 'pushStatus';
import { createRoot } from 'react-dom/client';
import { RecoilRoot } from 'recoil';

const registerServiceWorker = async () => {
  let registration = await navigator.serviceWorker.getRegistration();
  if (!registration) {
    registration = await navigator.serviceWorker.register('serviceWorker.js');
  }

  pushStatus.serviceWorkerRegistration = registration ?? null;
  pushStatus.pushSupport = !!registration?.pushManager;
  pushStatus.pushSubscription = await registration?.pushManager?.getSubscription();
};

if (process.env.NODE_ENV !== 'production' && 'serviceWorker' in navigator) {
  registerServiceWorker();
}

if (process.env.NODE_ENV === 'development') {
  const { worker } = require('./mocks/browser');
  worker.start();
}

const rootElement = document.getElementById('root');
const root = createRoot(rootElement!);
root.render(
  <RecoilRoot>
    <App />
  </RecoilRoot>,
);
