import { Cycle } from 'commonType';

export interface SuccessModalProps
  extends Pick<Cycle, 'challengeName' | 'successCount' | 'challengeId'> {
  handleCloseModal: () => void;
}
