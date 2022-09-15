import App from 'App';
import registerPwaServiceWorker from 'pwa/registerPwaServiceWorker';
import { createRoot } from 'react-dom/client';
import { RecoilRoot } from 'recoil';

if (process.env.IS_PROD || process.env.IS_LOCAL) {
  registerPwaServiceWorker();
}

if (process.env.IS_DEV) {
  import('./mocks/browser')
    .then(({ mockServiceWorker }) => {
      mockServiceWorker.start();
    })
    .catch((err) => {
      console.log(err);
    });
}

const rootElement = document.getElementById('root');
const root = createRoot(rootElement!);
root.render(
  <RecoilRoot>
    <App />
  </RecoilRoot>,
);
