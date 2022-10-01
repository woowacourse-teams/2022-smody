import { RankingPeriod } from 'types/ranking';

export type HandleRankingPeriodIdFunc = ({
  rankingPeriodId,
}: Pick<RankingPeriod, 'rankingPeriodId'>) => void;
