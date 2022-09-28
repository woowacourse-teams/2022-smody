import { Challenge, UserChallengeStat } from 'types/challenge';
import { Cycle } from 'types/cycle';

export type CertItemProps = Challenge &
  Pick<UserChallengeStat, 'successCount'> &
  Pick<Cycle, 'cycleId' | 'progressCount' | 'startTime'>;
