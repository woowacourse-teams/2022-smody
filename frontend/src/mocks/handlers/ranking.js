import { BASE_URL } from 'env';
import { rankingPeriodsData } from 'mocks/data';
import { rest } from 'msw';

export const ranking = [
  //전체 랭킹 기간 조회
  rest.post(`${BASE_URL}/ranking-periods?sort=startDate desc`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(rankingPeriodsData));
  }),
];
