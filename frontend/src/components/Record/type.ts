import { CycleDetailWithId } from 'commonType';

export interface RecordProps {
  cycleId: number;
  emojiIndex: number;
  startTime: string;
  cycleDetails: Pick<CycleDetailWithId, 'cycleDetailId' | 'progressImage'>[];
}

export type UseRecordProps = Pick<RecordProps, 'cycleId' | 'startTime' | 'cycleDetails'>;

export interface RecordItemProps
  extends Pick<RecordProps, 'emojiIndex' | 'cycleDetails'> {
  isBlank: boolean;
  index: number;
}

export type HandleNavigateFeedDetailProps = Pick<CycleDetailWithId, 'cycleDetailId'>;

export interface RecordWrapperProps {
  isSuccess: boolean;
}

export interface RecordItemWrapperProps {
  recordImg?: string;
}
