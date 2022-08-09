import { Cycle } from 'commonType';

export type CheckCirclesProps = Pick<Cycle, 'progressCount'>;

export interface CheckCircleProps extends CheckCirclesProps {
  checkCircleCount: number;
}
