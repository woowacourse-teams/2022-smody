import { userData, accessTokenData } from 'mocks/data';
import { rest } from 'msw';

import { BASE_URL } from 'constants/path';

export const auth = [
  // 1. 내 정보 조회(GET)
  rest.get(`${BASE_URL}/members/me`, (req, res, ctx) => {
    if (req.headers.headers.authorization === 'Bearer null') {
      return res(ctx.status(403), ctx.json({ code: 2002 }));
    }
    return res(ctx.status(200), ctx.json(userData));
  }),

  // 2-1. 구글 링크 조회(GET)
  rest.get(`${BASE_URL}/oauth/link/google`, (req, res, ctx) => {
    const CLIENT_ID = process.env.CLIENT_ID;
    const REDIRECT_URI = 'http://localhost:3000/cert';

    return res(
      ctx.status(200),
      ctx.json(
        `https://accounts.google.com/o/oauth2/v2/auth/oauthchooseaccount?client_id=${CLIENT_ID}&response_type=code&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile%20https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&redirect_uri=${REDIRECT_URI}&flowName=GeneralOAuthFlow`,
      ),
    );
  }),

  // 2-2. 토큰 조회(GET)
  rest.get(`${BASE_URL}/oauth/login/google`, (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({
        accessToken: accessTokenData,
      }),
    );
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

  rest.post('/images', (req, res, ctx) => {
    console.dir('@@@프로필 이미지 post 성공', JSON.stringify(req.body));
    return res(ctx.status(201));
  }),
];
