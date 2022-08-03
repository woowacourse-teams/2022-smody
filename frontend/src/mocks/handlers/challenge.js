// src/mocks/handlers.js
import {
  challengeData,
  cycleData,
  cycleDetailData,
  cycleNonDetailData,
  mySuccessChallengeData,
} from 'mocks/data';
import { rest } from 'msw';

import { API_PATH } from 'constants/path';
import { BASE_URL } from 'constants/path';

export const challenge = [
  //1. 챌린지 사이클 생성(POST)
  rest.post(`${BASE_URL}${API_PATH.CYCLE}`, (req, res, ctx) => {
    const { challengeId } = req.body;
    return res(ctx.status(403), ctx.json({ code: 3003 }));
  }),
  // 2. 나의 모든 진행 중인 챌린지 사이클 조회(GET)
  rest.get(`${BASE_URL}/cycles/me`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(cycleData));
    // return res(ctx.status(200), ctx.json([]));
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
    // return res(ctx.status(200), ctx.json(cycleNonDetailData));
  }),

  // 4. 챌린지 사이클의 진척도 증가(POST)
  rest.post(`${BASE_URL}/cycles/:cycleId/progress`, (req, res, ctx) => {
    const { cycleId } = req.params;
    cycleData[cycleId - 1].progressCount++;
    return res(ctx.status(200), ctx.json({ progressCount: 2 }));
  }),
  // 5. 모든 챌린지 조회(GET) - 비회원
  rest.get(`${BASE_URL}/challenges`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(challengeData));

    // return res(ctx.delay(1000), ctx.status(200), ctx.json(challengeData));
  }),
  // 5. 모든 챌린지 조회(GET) - 회원
  rest.get(`${BASE_URL}/challenges/auth`, (req, res, ctx) => {
    return res(ctx.delay(1000), ctx.status(200), ctx.json(challengeData));
  }),
  // 6. 나의 성공한 챌린지 조회(GET)
  rest.get(`${BASE_URL}/challenges/me`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(mySuccessChallengeData));
    // return res(ctx.status(200), ctx.json([]));
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

    return res(
      ctx.delay(1000),
      ctx.status(200),
      ctx.json(challengeData[challengeId - 1]),
    );
  }),

  // 8. 챌린지 하나 상세 조회(GET) - 회원
  rest.get(`${BASE_URL}/challenges/:challengeId/auth`, (req, res, ctx) => {
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

    return res(
      ctx.delay(1000),
      ctx.status(200),
      ctx.json(challengeData[challengeId - 1]),
    );
  }),
];
