import { CycleDetailWithId } from 'commonType';

export interface RecordProps {
  emojiIndex: number;
  startTime: string;
  cycleDetails: Pick<CycleDetailWithId, 'cycleDetailId' | 'progressImage'>[];
}

export type UseRecordProps = Pick<RecordProps, 'startTime' | 'cycleDetails'>;

export interface RecordItemProps
  extends Pick<RecordProps, 'emojiIndex' | 'cycleDetails'> {
  isBlank: boolean;
  index: number;
}

export interface RecordWrapperProps {
  isSuccess: boolean;
}

export interface RecordItemWrapperProps {
  recordImg?: string;
}
