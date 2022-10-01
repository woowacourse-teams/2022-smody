import { BASE_URL } from 'env';
import { myRanking, rankingPeriodsData } from 'mocks/data';
import { rest } from 'msw';

export const ranking = [
  //전체 랭킹 기간 조회
  rest.get(`${BASE_URL}/ranking-periods`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(rankingPeriodsData));
  }),

  //나의 랭킹
  rest.get(
    `${BASE_URL}/ranking-periods/:rankingPeriodId/ranking-activities/me`,
    (req, res, ctx) => {
      return res(ctx.status(200), ctx.json(myRanking));
    },
  ),
];
