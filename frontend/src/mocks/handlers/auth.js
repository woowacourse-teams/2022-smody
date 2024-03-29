import { BASE_URL } from 'env';
import { userData, accessTokenData } from 'mocks/data';
import { checkValidAccessToken } from 'mocks/utils';
import { rest } from 'msw';

export const auth = [
  // 1. 내 정보 조회(GET)
  rest.get(`${BASE_URL}/members/me`, (req, res, ctx) => {
    if (!checkValidAccessToken(req)) {
      return res(ctx.status(403), ctx.json({ code: 2002 }));
    }

    return res(ctx.status(200), ctx.json(userData));
  }),

  // 2-1. 구글 링크 조회(GET)
  rest.get(`${BASE_URL}/oauth/link/google`, (req, res, ctx) => {
    const CLIENT_ID = process.env.CLIENT_ID;
    const REDIRECT_URI = 'http://localhost:3000/feed';

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

    if (!checkValidAccessToken(req)) {
      return res(ctx.status(403), ctx.json({ code: 2002 }));
    }

    userData.nickname = nickname;
    userData.introduction = introduction;

    return res(ctx.status(204));
  }),

  // 4. 회원 탈퇴(DELETE)
  rest.delete(`${BASE_URL}/members/me`, (req, res, ctx) => {
    if (!checkValidAccessToken(req)) {
      return res(ctx.status(403), ctx.json({ code: 2002 }));
    }

    return res(ctx.status(204));
  }),

  // 프로필 이미지 업로드(POST)
  rest.post(`${BASE_URL}/members/me/profile-image`, (req, res, ctx) => {
    if (!checkValidAccessToken(req)) {
      return res(ctx.status(403), ctx.json({ code: 2002 }));
    }

    return res(ctx.delay(5000), ctx.status(201));
  }),

  // AccessToken 유효성 판단(GET)
  rest.get(`${BASE_URL}/oauth/check`, (req, res, ctx) => {
    return res(
      ctx.delay(2000),
      ctx.status(200),
      ctx.json({
        isValid: checkValidAccessToken(req),
      }),
    );
  }),
];
