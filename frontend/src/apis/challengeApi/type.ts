import { CursorParam } from 'apis/type';
import {
  AdditionalChallengeInfo,
  Challenge,
  ChallengeDetail,
  UserChallengeStat,
} from 'commonTypes/challenge';
import { Cycle, CycleDetail } from 'commonTypes/cycle';
import { Member } from 'commonTypes/member';

export type GetChallengeParams = {
  searchValue: string;
};

export type GetChallengeResponse = AdditionalChallengeInfo;

export type GetMyChallengesParams = CursorParam;

export type GetMyChallengesResponse = Challenge & Pick<UserChallengeStat, 'successCount'>;

export type GetChallengeByIdParams = Pick<Challenge, 'challengeId'>;

export type GetChallengeByIdResponse = Challenge &
  Cycle &
  Pick<UserChallengeStat, 'successCount'> & {
    cycleDetails: Omit<CycleDetail, 'cycleDetailId'>[];
  };

export type GetChallengersByIdParams = Pick<Challenge, 'challengeId'>;

export type GetChallengersByIdResponse = Omit<Member, 'email'> &
  Pick<Cycle, 'progressCount'>;

export type PostChallengePayload = Omit<ChallengeDetail, 'challengeId'>;

export type GetMyChallengeByIdParams = Pick<Challenge, 'challengeId'>;

export type GetMyChallengeByIdResponse = Omit<ChallengeDetail, 'challengeId'> &
  Pick<UserChallengeStat, 'successCount'> & { cycleDetailCount: number };
