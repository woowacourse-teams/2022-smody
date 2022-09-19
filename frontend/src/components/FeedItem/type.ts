import { Feed } from 'commonType';

export interface FeedItemProps extends Feed {
  isDetailPage?: boolean;
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

export interface WrapperProps extends Pick<FeedItemProps, 'isDetailPage'> {
  isSuccess: boolean;
}

export type CheckSuccessCycleProps = Pick<WrapperProps, 'isSuccess'>;
