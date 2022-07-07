// src/mocks/handlers.js
import { challengeData, cycleData } from 'mocks/data';
import { rest } from 'msw';

import { DEV_BASE_URL, API_PATH } from 'constants/path';

export const challenge = [
  //1. 챌린지 사이클 생성(POST)
  rest.post(`${DEV_BASE_URL}${API_PATH.CYCLE}`, (req, res, ctx) => {
    const { email, password, nickname } = req.body;
    return res(
      ctx.status(201),
      ctx.json({
        email: email,
      }),
    );
  }),
  // 2. 나의 모든 진행 중인 챌린지 사이클 조회(GET)
  rest.get(`${DEV_BASE_URL}/cycles/me?status=inProgress`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(cycleData));
  }),
  // 3. 챌린지 사이클의 진척도 증가(POST)
  rest.get(`${DEV_BASE_URL}/cycles/:cycleId/progress`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json({ progressCount: 2 }));
  }),
  // 4. 모든 챌린지 조회(GET)
  rest.get(`${DEV_BASE_URL}/challenges`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(challengeData));
  }),
  // 5. 아이디로 사이클 조회(GET)
  rest.post(`${DEV_BASE_URL}${API_PATH.CYCLES_ID}`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(cycleData[0]));
  }),
];
