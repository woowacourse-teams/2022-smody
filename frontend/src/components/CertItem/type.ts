import { Cycle } from 'commonType';

export interface CertItemProps extends Omit<Cycle, 'challengerCount'> {
  emoji: string;
}
