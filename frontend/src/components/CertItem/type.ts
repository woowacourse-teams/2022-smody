import { Cycle } from 'commonType';

export interface CertItemProps extends Cycle {
  refetch: () => void;
}
