import { RankingPeriod } from 'types/ranking';

export type RankingPeriodItemProps = RankingPeriod & {
  selected: boolean;
  handleChooseRankingPeriod: () => void;
};

export type SelectListProps = {
  show: boolean;
};

export type SelectListItemProps = Pick<RankingPeriodItemProps, 'selected'>;
