// src/mocks/handlers.js
import { rest } from 'msw';

export const handlers = [
  rest.post('/members', (req, res, ctx) => {
    const { email, password, nickname } = req.body;
    return res(
      ctx.status(201),
      ctx.json({
        email: email,
      }),
    );
  }),
  rest.post('/members/emails/checkDuplicate', (req, res, ctx) => {
    const { email } = req.body;
    return res(ctx.status(200));
  }),

  rest.post('/members/nicknames/checkDuplicate', (req, res, ctx) => {
    const { nickname } = req.body;
    return res(ctx.status(200));
  }),

  rest.post('/login', (req, res, ctx) => {
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
