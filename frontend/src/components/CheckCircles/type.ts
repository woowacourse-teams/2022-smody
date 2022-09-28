import { Cycle } from 'types/cycle';

export type CheckCirclesProps = Pick<Cycle, 'progressCount'>;

export type CheckCircleProps = Pick<Cycle, 'progressCount'> & {
  checkCircleCount: number;
};
