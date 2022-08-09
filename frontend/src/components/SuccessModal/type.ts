import { Cycle } from 'commonType';

export interface SuccessModalProps
  extends Pick<
    Cycle,
    'cycleId' | 'challengeName' | 'successCount' | 'challengeId' | 'progressCount'
  > {
  handleCloseModal: () => void;
  emoji: string;
}

export interface UseSuccessModalProps extends Omit<SuccessModalProps, 'successCount'> {
  emoji: string;
}
