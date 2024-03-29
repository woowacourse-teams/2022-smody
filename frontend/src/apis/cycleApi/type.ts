import { PickType } from 'smody-library';
import { Challenge, UserChallengeStat } from 'types/challenge';
import { Cycle, CycleDetail } from 'types/cycle';

export type PostCyclePayload = Pick<Cycle, 'startTime' | 'challengeId'>;

export type GetMyCyclesInProgressParams = {
  cursorId: PickType<Cycle, 'cycleId'>;
};

type MyProgressCycle = Pick<UserChallengeStat, 'successCount'> & Challenge & Cycle;

export type GetMyCyclesInProgressResponse = MyProgressCycle[];

export type PostCycleProgressPayload = Pick<Cycle, 'cycleId'> & { formData: FormData };

export type GetCycleByIdParams = Pick<Cycle, 'cycleId'>;

export type PostCycleProgressResponse = Pick<Cycle, 'progressCount'> &
  Pick<CycleDetail, 'cycleDetailId'>;

export type GetMyCyclesStatResponse = UserChallengeStat;

export type GetCycleByIdResponse = Challenge &
  Cycle &
  Pick<UserChallengeStat, 'successCount'> & {
    cycleDetails: Omit<CycleDetail, 'cycleDetailId'>[];
  };

export type GetMyCyclesByChallengeIdParams = Pick<Challenge, 'challengeId'> & {
  filter: string;
};

export type GetMyCyclesByChallengeIdAPIParams = GetMyCyclesByChallengeIdParams & {
  cursorId: PickType<Challenge, 'challengeId'>;
};

export type GetMyCyclesByChallengeIdResponse = (Pick<Cycle, 'cycleId' | 'startTime'> &
  Pick<Challenge, 'emojiIndex' | 'colorIndex'> & {
    cycleDetails: Pick<CycleDetail, 'cycleDetailId' | 'progressImage'>[];
  })[];
