import { RankingPeriod } from 'types/ranking';

export type SelectListProps = {
  show: boolean;
};

export type HandleChooseRankingPeriodFunc = Pick<RankingPeriod, 'rankingPeriodId'> & {
  index: number;
};
