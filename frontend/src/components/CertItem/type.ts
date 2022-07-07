import { Cycle } from 'commonType';

export interface CertItemProps extends Omit<Cycle, 'challengeId'> {
  handleClickCertification: (cycleId: number) => void;
}
