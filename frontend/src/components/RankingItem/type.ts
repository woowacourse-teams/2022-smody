import { Ranking } from 'types/ranking';

export type RankingItemProps = Ranking;

export type MedalProps = Pick<RankingItemProps, 'ranking'>;
