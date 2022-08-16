import { Challenge, Cycle, CycleDetail } from 'commonType';

export type PostCycleProps = Pick<Cycle, 'challengeId' | 'startTime'>;

export interface GetMyCyclesInProgressResponse extends Cycle {
  emojiIndex: number;
  colorIndex: number;
}

export type PostCycleProgressProps = Pick<Cycle, 'cycleId'> & { formData: FormData };

export type GetCycleByIdProps = Pick<Cycle, 'cycleId'>;

export interface PostCycleProgressResponse {
  progressCount: number;
}

export interface GetMyCyclesStatResponse {
  totalCount: number;
  successCount: number;
}

export interface GetCycleByIdResponse extends Cycle {
  emojiIndex: number;
  colorIndex: number;
  cycleDetails: CycleDetail[];
}

export type GetMyCyclesByChallengeIdProps = Pick<Challenge, 'challengeId'>;

export interface getMyCyclesByChallengeIdAPIProps extends GetMyCyclesByChallengeIdProps {
  lastCycleId: number;
}

export interface GetMyCyclesByChallengeIdResponse {
  cycleId: number;
  emojiIndex: number;
  colorIndex: number;
  cycleDetails: CycleDetail[];
}
