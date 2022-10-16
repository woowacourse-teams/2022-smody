import { Challenge, UserChallengeStat } from 'types/challenge';
import { Cycle } from 'types/cycle';

export type SuccessModalProps = Pick<Cycle, 'cycleId' | 'progressCount'> &
  Pick<Challenge, 'challengeId' | 'challengeName'> &
  Pick<UserChallengeStat, 'successCount'> & {
    handleCloseModal: () => void;
    emojiIndex: number;
  };

export type UseSuccessModalProps = Pick<
  SuccessModalProps,
  | 'cycleId'
  | 'progressCount'
  | 'challengeId'
  | 'challengeName'
  | 'handleCloseModal'
  | 'emojiIndex'
>;
