import { rest } from 'msw';

import { BASE_URL } from 'constants/path';

export const auth = [
  // 1. 내 정보 조회(GET)
  rest.get(`${BASE_URL}/members/me`, (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({
        nickname: '빅터짱짱맨',
        picture: 'https://emoji-copy.com/wp-content/uploads/1f64a.png',
        email: 'victor@gmail.com',
      }),
    );
  }),
];
