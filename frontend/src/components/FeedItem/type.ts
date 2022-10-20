import { Feed } from 'types/feed';

export type FeedItemProps = Feed & {
  isDetailPage?: boolean;
  isShowBriefChallengeName?: boolean;
};

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

export type CheckSuccessProps = {
  isSuccess: boolean;
};

export type ImgCloseButtonProps = { handleCloseImgModal: () => void };
