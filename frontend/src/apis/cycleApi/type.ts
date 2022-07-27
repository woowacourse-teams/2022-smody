import { Cycle } from 'commonType';

export type PostCycleProps = Pick<Cycle, 'challengeId'>;

export type PostCycleProgressProps = Pick<Cycle, 'cycleId'>;

export interface PostCycleProgressResponse {
  progressCount: number;
}

export interface GetMyCyclesStatResponse {
  totalCount: number;
  successCount: number;
}
