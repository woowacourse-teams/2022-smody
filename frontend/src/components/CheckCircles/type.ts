import { Cycle } from 'commonType';

export type CheckCirclesProps = Pick<Cycle, 'progressCount'> & { gap?: string };

export interface CheckCircleProps extends CheckCirclesProps {
  checkCircleCount: number;
}
