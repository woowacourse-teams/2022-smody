import { db, notifications } from 'mocks/data';
import { rest } from 'msw';

import { BASE_URL } from 'constants/path';

const publicKey = {
  publicKey:
    'BCyawrgMy1I1meZj7wqeS8skspdDyNF8d2bEqTWQdA4ma7yickBKsCrXVFcN9Awyf3tGcjw-hJ_DUYLY7nJi24I',
};

export const push = [
  // VAPID 공개키 조회(GET)
  rest.get(`${BASE_URL}/web-push/public-key`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(publicKey));
  }),

  // 구독 정보 저장(POST)
  rest.post(`${BASE_URL}/web-push/subscribe`, (req, res, ctx) => {
    if (req.headers.headers.authorization === 'Bearer null') {
      return res(ctx.status(403), ctx.json({ code: 2002 }));
    }
    const subscription = req.body;
    db.find((user) => user.nickname === 'marco').subscription = subscription;

    return res(ctx.status(200), ctx.json(db));
  }),

  // 구독 정보 삭제(POST)
  rest.post(`${BASE_URL}/web-push/unsubscribe`, (req, res, ctx) => {
    if (req.headers.headers.authorization === 'Bearer null') {
      return res(ctx.status(403), ctx.json({ code: 2002 }));
    }
    const { endpoint } = req.body;

    db.forEach((user) => {
      if (user.subscription.endpoint === endpoint) {
        user.subscription = null;
      }
    });

    return res(ctx.status(200), ctx.json(db));
  }),

  // 알림 조회(GET)
  rest.get(`${BASE_URL}/push-notifications`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(notifications));
  }),

  // 읽은 알림 삭제(DELETE)
  rest.delete(`${BASE_URL}/push-notifications/:pushNotificationId`, (req, res, ctx) => {
    const { pushNotificationId } = req.params;
    const index = notifications.findIndex(
      (notification) => notification.pushNotificationId === Number(pushNotificationId),
    );

    notifications.splice(index, 1);
    return res(ctx.status(200));
  }),
];
