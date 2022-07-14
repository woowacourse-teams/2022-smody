import { Cycle } from 'commonType';

export interface CertItemProps extends Cycle {
  handleClickCertification: (cycleId: number) => void;
}
