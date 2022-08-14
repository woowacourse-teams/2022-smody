self.addEventListener('install', (event) => {
  console.log('Service Worker - install');
});

self.addEventListener('activate', (event) => {
  console.log('Service Worker - activate');
});

self.addEventListener('fetch', (event) => {
  console.log('[Service Worker - fetch]', event.request.url);
});

/**
 * 알림 관련 코드
 */

self.addEventListener('push', (event) => {
  const data = event.data.json();
  console.log('Service Worker - push:', data);

  const title = 'Smody';
  const options = {
    body: data.message,
  };

  event.waitUntil(self.registration.showNotification(title, options));
});

self.addEventListener('notificationclick', (event) => {
  console.log('Service Worker - Notification clicked!');
  event.notification.close();
  event.waitUntil(self.clients.openWindow('https://www.smody.com'));
});
