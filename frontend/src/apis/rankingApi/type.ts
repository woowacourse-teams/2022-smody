import { Ranking, RankingPeriod } from 'types/ranking';

export type GetRankingPeriodsResponse = RankingPeriod[];

export type GetMyRankingParams = Pick<RankingPeriod, 'rankingPeriodId'>;

export type GetMyRankingResponse = Ranking;

export type GetAllRankingParams = Pick<RankingPeriod, 'rankingPeriodId'>;

export type GetAllRankingResponse = Ranking[];
