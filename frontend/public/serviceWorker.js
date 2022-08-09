self.addEventListener('install', (event) => {
  console.log('Service Worker - install');
});

self.addEventListener('activate', (event) => {
  console.log('Service Worker - activate');
});

self.addEventListener('fetch', (event) => {
  console.log('[Service Worker - fetch]', event.request.url);
});
