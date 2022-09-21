import { GetMyCyclesInProgressResponse } from 'apis/cycleApi/type';

export interface CertTimelineItemProps {
  cycleInfo: GetMyCyclesInProgressResponse;
  time: 'past' | 'now' | 'future';
}

export type ItemWrapperProps = Pick<CertTimelineItemProps, 'time'>;
