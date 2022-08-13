import App from 'App';
import registerPushServiceWorker from 'push/registerPushServiceWorker';
import { createRoot } from 'react-dom/client';
import { RecoilRoot } from 'recoil';

if (process.env.NODE_ENV === 'production') {
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
