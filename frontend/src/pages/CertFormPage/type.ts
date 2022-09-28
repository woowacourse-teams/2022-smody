import { Challenge, UserChallengeStat } from 'types/challenge';
import { Cycle } from 'types/cycle';

export type CertImageWrapperProps = {
  isSelectImage: boolean;
};

export type TextAreaProps = {
  isDark: boolean;
};

export type CertFormPageLocationState = Challenge &
  Pick<UserChallengeStat, 'successCount'> &
  Pick<Cycle, 'cycleId' | 'progressCount'>;
