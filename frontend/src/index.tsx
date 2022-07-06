import App from 'App';
import { createRoot } from 'react-dom/client';

if (process.env.NODE_ENV === 'development') {
  const { worker } = require('./mocks/browser');
  worker.start();
}

const rootElement = document.getElementById('root');
const root = createRoot(rootElement!);
root.render(<App />);
