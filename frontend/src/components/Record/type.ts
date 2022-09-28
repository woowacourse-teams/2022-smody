import { CycleDetail } from 'types/cycle';

export type RecordProps = {
  cycleId: number;
  emojiIndex: number;
  startTime: string;
  cycleDetails: Pick<CycleDetail, 'cycleDetailId' | 'progressImage'>[];
};

export type UseRecordProps = Pick<RecordProps, 'cycleId' | 'startTime' | 'cycleDetails'>;

export type RecordItemProps = Pick<RecordProps, 'emojiIndex' | 'cycleDetails'> & {
  isBlank: boolean;
  index: number;
};

export type HandleNavigateFeedDetailProps = Pick<CycleDetail, 'cycleDetailId'>;

export type RecordWrapperProps = {
  isSuccess: boolean;
};

export type RecordItemWrapperProps = {
  recordImg?: string;
};
