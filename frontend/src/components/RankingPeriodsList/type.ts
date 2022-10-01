import { RankingPeriod } from 'types/ranking';

export type RankingPeriodsListProps = {
  handleRankingPeriodId: ({
    rankingPeriodId,
  }: Pick<RankingPeriod, 'rankingPeriodId'>) => void;
};

export type SelectListProps = {
  show: boolean;
};

export type UseRankingPeriodsListProps = RankingPeriodsListProps;

export type HandleChooseRankingPeriodFunc = Pick<RankingPeriod, 'rankingPeriodId'> & {
  index: number;
};
