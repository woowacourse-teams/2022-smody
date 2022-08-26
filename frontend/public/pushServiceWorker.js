const VERSION = 'version 1.0.1';

const broadcast = new BroadcastChannel('push-channel');

self.addEventListener('install', (event) => {
  console.log('SMODY service worker - install', VERSION);

  // 제어중인 서비스 워커가 존재해도 대기 상태를 건너뛴다.
  self.skipWaiting();
});

self.addEventListener('activate', (event) => {
  console.log('SMODY service worker - activate', VERSION);
  // 활성화 즉시 클라이언트를 제어한다.(새로고침 불필요)
  self.clients.claim();
});

/**
 * 알림 관련 코드
 */
self.addEventListener('push', (event) => {
  const data = event.data.json();

  const title = 'Three more days, 스모디';
  const pushCase = data.pushCase;
  const pathId = data.pathId;
  let path;
  if (pushCase === 'subscription') {
    path = '/profile';
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

  broadcast.postMessage({ message: data.message });
});

self.addEventListener('notificationclick', (event) => {
  event.notification.close();
  const fullPath = self.location.origin + event.notification.data.path;

  event.waitUntil(self.clients.openWindow(fullPath));
});
