import App from 'App';
import { isProd, isDev, isLocal } from 'env';
import registerPwaServiceWorker from 'pwa/registerPwaServiceWorker';
import { createRoot } from 'react-dom/client';
import { RecoilRoot } from 'recoil';

if (isProd || isLocal) {
  registerPwaServiceWorker();
}

if (isDev) {
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
