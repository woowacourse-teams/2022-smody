import { Cycle } from 'commonType';

export type PostCycleProps = Pick<Cycle, 'challengeId'>;

export type PostCycleProgressProps = Pick<Cycle, 'cycleId'> & { formData: FormData };

export type GetCycleByIdProps = Pick<Cycle, 'cycleId'>;

export interface PostCycleProgressResponse {
  progressCount: number;
}

export interface GetMyCyclesStatResponse {
  totalCount: number;
  successCount: number;
}

interface CycleDetail {
  progressImage: string;
  progressTime: string;
  description: string;
}

export type GetCycleByIdResponse = Cycle & {
  cycleDetails: CycleDetail[];
};
