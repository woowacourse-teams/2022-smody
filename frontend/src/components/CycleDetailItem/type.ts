import { CycleDetail } from 'types/cycle';

export type CycleDetailItemProps = Pick<
  CycleDetail,
  'progressTime' | 'description' | 'progressImage'
>;

export type CycleImgProps = {
  src: string;
};
