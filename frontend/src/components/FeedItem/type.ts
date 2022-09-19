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

export type WrapperProps = Pick<FeedItemProps, 'isDetailPage'>;

export interface ProgressImgProps extends Pick<FeedItemProps, 'isDetailPage'> {
  isSuccess: boolean;
}

export type CheckSuccessCycleProps = Pick<ProgressImgProps, 'isSuccess'>;
