import App from 'App';
import { createRoot } from 'react-dom/client';
import { RecoilRoot } from 'recoil';

if (process.env.NODE_ENV === 'development') {
  const { worker } = require('./mocks/browser');
  worker.start();
}

if (process.env.NODE_ENV === 'production' && 'serviceWorker' in navigator) {
  navigator.serviceWorker.register('serviceWorker.js').then((registration) => {
    // 등록완료
    console.log('서비스워커 등록 완료');
  });
}

const rootElement = document.getElementById('root');
const root = createRoot(rootElement!);
root.render(
  <RecoilRoot>
    <App />
  </RecoilRoot>,
);
