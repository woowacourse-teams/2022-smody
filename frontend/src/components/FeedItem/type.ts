import { Feed } from 'commonType';

export interface FeedItemProps extends Feed {
  isClickable?: boolean;
  isShowBriefChallengeName?: boolean;
}

export type UseFeedProps = Pick<
  FeedItemProps,
  | 'challengeId'
  | 'cycleDetailId'
  | 'progressTime'
  | 'progressCount'
  | 'challengeName'
  | 'isShowBriefChallengeName'
>;

export interface WrapperProps extends Pick<FeedItemProps, 'isClickable'> {
  isSuccess: boolean;
}

export type CheckSuccessCycleProps = Pick<WrapperProps, 'isSuccess'>;
