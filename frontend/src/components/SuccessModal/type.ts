import { Cycle } from 'commonType';

export interface SuccessModalProps
  extends Pick<
    Cycle,
    'cycleId' | 'challengeName' | 'successCount' | 'challengeId' | 'progressCount'
  > {
  handleCloseModal: () => void;
}

export type UseSuccessModalProps = Omit<SuccessModalProps, 'successCount'>;
