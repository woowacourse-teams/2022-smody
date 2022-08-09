import { Cycle, CycleDetail } from 'commonType';

export type PostCycleProps = Pick<Cycle, 'challengeId'>;

export interface GetMyCyclesInProgressResponse extends Cycle {
  emoji: string;
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
  description: string;
  emoji: string;
  cycleDetails: CycleDetail[];
}
