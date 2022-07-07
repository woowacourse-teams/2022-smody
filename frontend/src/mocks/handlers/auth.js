import { rest } from 'msw';

import { DEV_BASE_URL } from 'constants/path';

export const auth = [
  rest.post(`${DEV_BASE_URL}/members`, (req, res, ctx) => {
    const { email, password, nickname } = req.body;
    return res(
      ctx.status(201),
      ctx.json({
        email: email,
      }),
    );
  }),
  rest.post(`${DEV_BASE_URL}/members/emails/checkDuplicate`, (req, res, ctx) => {
    const { email } = req.body;
    return res(ctx.status(200));
  }),

  rest.post(`${DEV_BASE_URL}/members/nicknames/checkDuplicate`, (req, res, ctx) => {
    const { nickname } = req.body;
    return res(ctx.status(200));
  }),

  rest.post(`${DEV_BASE_URL}/login`, (req, res, ctx) => {
    const { email, password } = req.body;

    return res(
      ctx.status(200),
      ctx.json({
        nickname: '스모디맨',
        accessToken: '${accessToken}',
      }),
    );
  }),
];
