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

  const title = 'Three more days, 스모디';
  const pushCase = data.pushCase;
  const pathId = data.pathId;
  let path;
  if (pushCase === 'subscription') {
    console.log('hi');
    path = `/profile`;
  }
  if (pushCase === 'comment') {
    path = `/feed/detail/${pathId}`;
  }
  if (pushCase === 'challenge') {
    path = `/cycle/detail/${pathId}`;
  }

  const options = {
    body: data.message,
    icon: './image/favicon-32x32.png',
    badge: './image/favicon-16x16.png',
    vibrate: [
      500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500,
    ],
    data: { path },
  };

  event.waitUntil(self.registration.showNotification(title, options));
});

self.addEventListener('notificationclick', (event) => {
  event.notification.close();
  const fullPath = self.location.origin + event.notification.data.path;

  event.waitUntil(self.clients.openWindow(fullPath));
});
