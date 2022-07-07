import { Cycle } from 'commonType';

export type PostCycleProps = Pick<Cycle, 'challengeId' | 'startTime'>;

export type PostCycleProgressProps = Pick<Cycle, 'cycleId'>;

export interface PostCycleProgressResponse {
  progressCount: 0 | 1 | 2 | 3;
}

export interface GetChallengeResponse {
  challengeId: number;
  challengeName: string;
  challengerCount: number;
}
