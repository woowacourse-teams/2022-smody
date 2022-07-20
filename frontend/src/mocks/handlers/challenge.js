// src/mocks/handlers.js
import { challengeData, cycleData, mySuccessChallengeData } from 'mocks/data';
import { rest } from 'msw';

import { API_PATH } from 'constants/path';
import { BASE_URL } from 'constants/path';

export const challenge = [
  //1. 챌린지 사이클 생성(POST)
  rest.post(`${BASE_URL}${API_PATH.CYCLE}`, (req, res, ctx) => {
    const { challengeId } = req.body;
    return res(ctx.status(201));
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
  // 4. 챌린지 사이클의 진척도 증가(POST)
  rest.post(`${BASE_URL}/cycles/:cycleId/progress`, (req, res, ctx) => {
    const { cycleId } = req.params;
    cycleData[cycleId - 1].progressCount++;
    return res(ctx.status(200), ctx.json({ progressCount: 2 }));
  }),
  // 5. 모든 챌린지 조회(GET)
  rest.get(`${BASE_URL}/challenges`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(challengeData));
  }),
  // 6. 나의 성공한 챌린지 조회(GET)
  rest.get(`${BASE_URL}/challenges/me`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(mySuccessChallengeData));
    // return res(ctx.status(200), ctx.json([]));
  }),
  // 7. 아이디로 사이클 조회(GET)
  rest.post(`${BASE_URL}${API_PATH.CYCLES_ID}`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(cycleData[0]));
  }),
];
