import { Cycle } from 'commonType';

export interface CertItemProps extends Omit<Cycle, 'challengerCount'> {
  emojiIndex: number;
  colorIndex: number;
}
