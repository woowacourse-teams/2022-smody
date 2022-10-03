import { Ranking } from 'types/ranking';
import { AvailablePickedColor } from 'types/style';

export type RankingItemProps = Ranking & {
  myRanking?: boolean;
};

export type UseRankingItemProps = {
  myRanking: boolean;
};

export type MedalProps = Pick<RankingItemProps, 'ranking'>;

export type WrapperProps = {
  surfaceColor: AvailablePickedColor;
};
