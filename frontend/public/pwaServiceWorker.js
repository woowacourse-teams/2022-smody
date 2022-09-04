const VERSION = 'v4';
const CACHE_NAME = 'smody-cache_' + VERSION;
const IMAGE_CACHE_NAME = 'smody-image_' + VERSION;

const CLIENT_PATH = {
  HOME: '/home',
  LOGIN: '/login',
  SIGN_UP: '/sign_up',
  CERT: '/cert',
  SEARCH: '/search',
  FEED: '/feed',
  FEED_DETAIL: '/feed/detail',
  FEED_DETAIL_ID: '/feed/detail/:cycleDetailId',
  PROFILE: '/profile',
  PROFILE_EDIT: '/profile/edit',
  PROFILE_CHALLENGE_DETAIL: '/profile/challenges/detail',
  PROFILE_CHALLENGE_DETAIL_ID: '/profile/challenges/detail/:challengeId',
  CYCLE_DETAIL: '/cycle/detail',
  CYCLE_DETAIL_ID: '/cycle/detail/:cycleId',
  CHALLENGE_CREATE: '/challenge/create',
  CHALLENGE_DETAIL: '/challenge/detail',
  CHALLENGE_DETAIL_ID: '/challenge/detail/:challengeId',
  VOC: '/voc',
  NOT_FOUND: '/not_found',
  WILD_CARD: '*',
};

const routerList = Object.values(CLIENT_PATH);

const IMMUTABLE_APPSHELL = [
  '/image/android-chrome-192x192.png',
  '/image/android-chrome-512x512.png',
  '/image/apple-touch-icon.png',
  '/image/favicon-16x16.png',
  '/image/favicon-32x32.png',
  '/image/favicon.ico',
  '/assets/service_example.png',
  '/manifest.json',
];

const MUTABLE_APPSHELL = ['/', '/bundle', '/index.html'];

const CACHE_LIST = IMMUTABLE_APPSHELL.concat(MUTABLE_APPSHELL);

self.addEventListener('install', (event) => {
  console.log('SMODY service worker - install', VERSION);

  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => {
      return cache.addAll(CACHE_LIST);
    }),
  );

  // 제어중인 서비스 워커가 존재해도 대기 상태를 건너뛴다.
  self.skipWaiting();
});

self.addEventListener('activate', (event) => {
  console.log('SMODY service worker - activate', VERSION);

  event.waitUntil(
    caches.keys().then((keyList) => {
      return Promise.all(
        keyList.map((key) => {
          if (key !== CACHE_NAME && key !== IMAGE_CACHE_NAME) {
            return caches.delete(key);
          }
        }),
      );
    }),
  );
  // 활성화 즉시 클라이언트를 제어한다.(새로고침 불필요)
  self.clients.claim();
});

self.addEventListener('fetch', (event) => {
  const url = new URL(event.request.url);
  const urlPath = url.pathname.split('?')[0];

  if (IMMUTABLE_APPSHELL.includes(urlPath)) {
    // 자주 변경되지 않는 리소스인 경우
    // 선 캐시, 후 네트워크 응답
    event.respondWith(
      caches.match(urlPath).then((response) => {
        return response || fetch(event.request);
      }),
    );
  }

  // bundle js 파일 요청할 경우
  // 선 네트워크, 후 캐시 응답
  if (event.request.url.includes('bundle')) {
    event.respondWith(
      caches.open(CACHE_NAME).then((cache) => {
        return fetch(event.request)
          .then((networkResponse) => {
            // 최신화된 데이터를 캐시에 업데이트한다
            cache.put(event.request, networkResponse.clone());
            return networkResponse;
          })
          .catch(() => {
            // 네트워크 문제가 발생한 경우 캐시에서 응답
            return cache.match(event.request);
          });
      }),
    );
  }

  // route path를 요청할 경우 index.html 응답
  if (routerList.includes(urlPath)) {
    // 선 캐시, 후 네트워크
    event.respondWith(
      caches.match('/index.html').then((response) => response || fetch(event.request)),
    );
  }

  // 게시물 이미지 캐싱
  // 요청 url의 pathname이 /images로 시작하면 게시물 이미지 요청이다.
  if (url.pathname.startsWith('/images')) {
    event.respondWith(
      caches.open(IMAGE_CACHE_NAME).then((cache) => {
        return cache.match(event.request).then((cacheResponse) => {
          // 캐시가 존재하는 경우 캐시 응답
          if (cacheResponse) {
            return cacheResponse;
          } else {
            // 존재하지 않는 경우 최초 1회만 캐싱
            return fetch(event.request).then((networkResponse) => {
              // 캐싱하고 네트워크 리소스 응답
              cache.put(event.request, networkResponse.clone());
              return networkResponse;
            });
          }
        });
      }),
    );
  }
});

// 알림 관련 코드
const broadcast = new BroadcastChannel('push-channel');

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
