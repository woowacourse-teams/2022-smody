import { userData } from 'mocks/data';
import { rest } from 'msw';

import { BASE_URL } from 'constants/path';

export const auth = [
  // 1. 내 정보 조회(GET)
  rest.get(`${BASE_URL}/members/me`, (req, res, ctx) => {
    if (req.headers.headers.authorization === 'Bearer null') {
      return res(ctx.status(403));
    }
    return res(ctx.status(200), ctx.json(userData));
  }),

  // 3. 회원 정보 수정(PATCH)
  rest.patch(`${BASE_URL}/members/me`, (req, res, ctx) => {
    const { nickname, introduction } = req.body;

    userData.nickname = nickname;
    userData.introduction = introduction;

    return res(ctx.status(204), ctx.delay(1000));
  }),

  // 4. 회원 탈퇴(DELETE)
  rest.delete(`${BASE_URL}/members/me`, (req, res, ctx) => {
    return res(ctx.status(204), ctx.delay(1000));
  }),
];
