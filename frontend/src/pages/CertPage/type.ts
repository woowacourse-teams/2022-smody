import { Challenge, UserChallengeStat } from 'types/challenge';
import { Cycle } from 'types/cycle';

export type CycleInfoItem = Pick<UserChallengeStat, 'successCount'> & Challenge & Cycle;
