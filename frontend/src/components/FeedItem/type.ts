import { Feed } from 'commonType';

export interface FeedItemProps extends Feed {
  isClickable?: boolean;
}

export type UseFeedProps = Pick<
  FeedItemProps,
  'challengeId' | 'cycleDetailId' | 'progressTime'
>;

export type WrapperProps = Pick<FeedItemProps, 'isClickable'>;
