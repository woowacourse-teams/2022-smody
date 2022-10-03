import { BASE_URL } from 'env';
import { allRanking, myRanking, rankingPeriodsData, userData } from 'mocks/data';
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
      const { rankingPeriodId } = req.params;
      const data = myRanking;
      data.ranking = rankingPeriodId;
      data.nickname = userData.nickname;
      data.introduction = userData.introduction;
      data.picture = userData.picture;
      if (rankingPeriodId === '1') {
        return res(ctx.delay(1000), ctx.status(404));
      }
      return res(ctx.delay(1000), ctx.status(200), ctx.json(data));
    },
  ),

  // 전체 랭킹 활동 조회
  rest.get(
    `${BASE_URL}/ranking-periods/:rankingPeriodId/ranking-activities`,
    (req, res, ctx) => {
      const { rankingPeriodId } = req.params;
      if (rankingPeriodId === '1') {
        return res(ctx.delay(1000), ctx.status(404));
      }
      return res(ctx.delay(1000), ctx.status(200), ctx.json(allRanking));
    },
  ),
];
