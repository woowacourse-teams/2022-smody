import { Cycle } from 'commonType';

export type PostCycleProps = Pick<Cycle, 'challengeId' | 'startTime'>;

export type PostCycleProgressProps = Pick<Cycle, 'cycleId'>;

export interface PostCycleProgressResponse {
  progressCount: number;
}

export interface GetChallengeResponse {
  challengeId: number;
  challengeName: string;
  challengerCount: number;
}
