import { feedData } from 'mocks/data';
import { rest } from 'msw';

import { BASE_URL } from 'constants/path';

export const feed = [
  rest.get(`${BASE_URL}/feeds`, (req, res, ctx) => {
    return res(ctx.delay(2000), ctx.status(200), ctx.json(feedData));
  }),
  rest.get(`${BASE_URL}/feeds/:cycleDetailId`, (req, res, ctx) => {
    const { cycleDetailId } = req.params;

    if (Number.isNaN(cycleDetailId) || cycleDetailId > feedData.length) {
      return res(
        ctx.status(404),
        ctx.json({
          code: 4002,
          message: '존재하지 않는 챌린지입니다.',
        }),
      );
    }

    return res(ctx.status(200), ctx.json(feedData[cycleDetailId - 1]));
  }),
];
