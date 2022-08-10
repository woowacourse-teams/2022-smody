import {
  challengeData,
  cycleData,
  cycleDetailData,
  mySuccessChallengeData,
  accessTokenData,
  challengers,
} from 'mocks/data';
import { rest } from 'msw';

import { API_PATH } from 'constants/path';
import { BASE_URL } from 'constants/path';

export const challenge = [
  //1. 챌린지 사이클 생성(POST)
  rest.post(`${BASE_URL}${API_PATH.CYCLE}`, (req, res, ctx) => {
    const { challengeId } = req.body;

    challengeData[challengeId - 1].isInProgress = true;

    return res(ctx.json(201));
  }),
  // 2. 나의 모든 진행 중인 챌린지 사이클 조회(GET)
  rest.get(`${BASE_URL}/cycles/me`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(cycleData));
  }),
  // 3.나의 사이클 통계 정보 조회
  rest.get(`${BASE_URL}/cycles/me/stat`, (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({
        totalCount: 35,
        successCount: 5,
      }),
    );
  }),

  // 7. 아이디로 사이클 조회(GET)
  rest.get(`${BASE_URL}/cycles/:cycleId`, (req, res, ctx) => {
    const { cycleId } = req.params;

    if (Number.isNaN(cycleId)) {
      return res(
        ctx.status(404),
        ctx.json({
          code: 4002,
          message: '존재하지 않는 사이클입니다.',
        }),
      );
    }
    return res(ctx.status(200), ctx.json(cycleDetailData));
  }),

  // 4. 챌린지 사이클의 진척도 증가(POST)
  rest.post(`${BASE_URL}/cycles/:cycleId/progress`, (req, res, ctx) => {
    const { cycleId } = req.params;
    cycleData[cycleId - 1].progressCount++;

    return res(ctx.delay(2000), ctx.status(200), ctx.json({ progressCount: 2 }));
  }),

  // 5. 모든 챌린지 조회(GET) - 비회원
  rest.get(`${BASE_URL}/challenges`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(challengeData));
  }),
  // 5. 모든 챌린지 조회(GET) - 회원
  rest.get(`${BASE_URL}/challenges/auth`, (req, res, ctx) => {
    const { authorization } = req.headers.headers;

    const accessToken = authorization.split(' ')[1];

    if (accessToken !== accessTokenData) {
      return res(
        ctx.status(403),
        ctx.json({
          code: 2002,
          message: '유효하지 않은 토큰입니다.',
        }),
      );
    }

    return res(ctx.status(200), ctx.json(challengeData));
  }),
  // 6. 나의 성공한 챌린지 조회(GET)
  rest.get(`${BASE_URL}/challenges/me`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(mySuccessChallengeData));
  }),
  // 8. 챌린지 하나 상세 조회(GET) - 비회원
  rest.get(`${BASE_URL}/challenges/:challengeId`, (req, res, ctx) => {
    const { challengeId } = req.params;

    if (Number.isNaN(challengeId) || challengeId > challengeData.length) {
      return res(
        ctx.status(404),
        ctx.json({
          code: 4002,
          message: '존재하지 않는 챌린지입니다.',
        }),
      );
    }

    return res(ctx.status(200), ctx.json(challengeData[challengeId - 1]));
  }),

  // 8. 챌린지 하나 상세 조회(GET) - 회원
  rest.get(`${BASE_URL}/challenges/:challengeId/auth`, (req, res, ctx) => {
    const { challengeId } = req.params;
    const { authorization } = req.headers.headers;

    const accessToken = authorization.split(' ')[1];

    if (Number.isNaN(challengeId) || challengeId > challengeData.length) {
      return res(
        ctx.status(404),
        ctx.json({
          code: 4002,
          message: '존재하지 않는 챌린지입니다.',
        }),
      );
    }

    if (accessToken !== accessTokenData) {
      return res(
        ctx.status(403),
        ctx.json({
          code: 2002,
          message: '유효하지 않은 토큰입니다.',
        }),
      );
    }

    return res(ctx.status(200), ctx.json(challengeData[challengeId - 1]));
  }),

  rest.get(`${BASE_URL}/challenges/:challengeId/challengers`, (req, res, ctx) => {
    const { challengeId } = req.params;

    if (Number.isNaN(challengeId) || challengeId > challengeData.length) {
      return res(
        ctx.status(404),
        ctx.json({
          code: 4002,
          message: '존재하지 않는 챌린지입니다.',
        }),
      );
    }

    // return res(ctx.status(200), ctx.json([]));
    return res(ctx.status(200), ctx.json(challengers));
  }),

  //10. 챌린지 생성(POST)
  rest.post(`${BASE_URL}/challenges`, (req, res, ctx) => {
    challengeData.push(req.body);
    console.log(req.body);
    return res(ctx.delay(1000), ctx.json(201));
  }),
];
