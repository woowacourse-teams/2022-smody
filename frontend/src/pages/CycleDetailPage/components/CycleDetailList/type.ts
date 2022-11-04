import { CycleDetail } from 'types/cycle';

export type CycleDetailListProps = {
  cycleDetails: Pick<CycleDetail, 'progressImage' | 'progressTime' | 'description'>[];
};
