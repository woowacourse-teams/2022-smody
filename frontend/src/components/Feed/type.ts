import { Challenge, CycleDetail } from 'commonType';

export interface FeedProps
  extends Pick<Challenge, 'challengeId'>,
    Pick<CycleDetail, 'progressTime'> {}

export type UseFeedProps = Pick<FeedProps, 'challengeId' | 'progressTime'>;
