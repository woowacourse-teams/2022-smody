import { RankingPeriod } from 'types/ranking';

export type RankingPeriodItemProps = RankingPeriod & {
  selected: boolean;
  handleChooseRankingPeriod: () => void;
};

export type SelectListItemProps = Pick<RankingPeriodItemProps, 'selected'>;

export type UseRankingPeriodItemProps = Pick<
  RankingPeriodItemProps,
  'startDate' | 'duration'
>;
