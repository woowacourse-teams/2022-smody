import { Member } from './member';

export type RankingPeriod = {
  rankingPeriodId: number;
  startDate: string;
  duration: 'week';
};

export type Ranking = Pick<
  Member,
  'memberId' | 'nickname' | 'introduction' | 'picture'
> & {
  ranking: number;
  point: number;
};
