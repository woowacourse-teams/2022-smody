// src/mocks/browser.js
import { handlers } from 'mocks/handlers';
import { setupWorker } from 'msw';

// This configures a Service Worker with the given request handlers.
export const mockServiceWorker = setupWorker(...handlers);
