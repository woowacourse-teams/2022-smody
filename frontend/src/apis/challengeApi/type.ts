import { PickType } from 'smody-library';
import {
  AdditionalChallengeInfo,
  Challenge,
  ChallengeDetail,
  UserChallengeStat,
} from 'types/challenge';
import { Cycle, CycleDetail } from 'types/cycle';
import { Member } from 'types/member';

export type GetChallengeParams = {
  searchValue: string;
};

export type GetAllChallengesResponse = AdditionalChallengeInfo[];

export type GetMyChallengesParams = { cursorId: PickType<Challenge, 'challengeId'> };

export type GetMyChallengesResponse = (Challenge &
  Pick<UserChallengeStat, 'successCount'>)[];

export type GetChallengeByIdParams = Pick<Challenge, 'challengeId'>;

export type GetChallengeByIdResponse = AdditionalChallengeInfo &
  ChallengeDetail &
  Pick<UserChallengeStat, 'successCount'> & {
    cycleDetails: Omit<CycleDetail, 'cycleDetailId'>[];
  };

export type GetChallengersByIdParams = Pick<Challenge, 'challengeId'>;

export type GetChallengersByIdResponse = (Omit<Member, 'email'> &
  Pick<Cycle, 'progressCount'>)[];

export type PostChallengePayload = Omit<ChallengeDetail, 'challengeId'>;

export type GetMyChallengeByIdParams = Pick<Challenge, 'challengeId'>;

export type GetMyChallengeByIdResponse = Omit<ChallengeDetail, 'challengeId'> &
  Pick<UserChallengeStat, 'successCount'> & { cycleDetailCount: number };
