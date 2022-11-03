import { Ranking } from 'types/ranking';
import { AvailablePickedColor } from 'types/style';

export type RankingItemProps = Ranking;

export type UseRankingItemProps = Pick<Ranking, 'memberId'>;

export type MedalProps = Pick<RankingItemProps, 'ranking'>;

export type WrapperProps = {
  surfaceColor: AvailablePickedColor;
};
