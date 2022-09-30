import { PickType } from 'smody-library';
import { Feed, Comment } from 'types/feed';
import { Member } from 'types/member';

export type GetAllFeedsResponse = Feed[];

export type GetFeedByIdParams = Pick<Feed, 'cycleDetailId'>;

export type GetFeedByIdResponse = Feed;

export type UsePostCommentProps = Pick<Feed, 'cycleDetailId'>;

export type UsePostCommentMutationFunctionProps = Pick<Comment, 'content'>;

export type PostCommentPayload = UsePostCommentProps &
  UsePostCommentMutationFunctionProps;

export type UseGetCommentsByIdProps = Pick<Feed, 'cycleDetailId'> & {
  isLogin: boolean;
};

export type GetCommentsByIdParams = Pick<Feed, 'cycleDetailId'>;

export type GetCommentsByIdResponse = Comment[];

export type PatchCommentsPayload = Pick<Comment, 'commentId' | 'content'>;

export type DeleteCommentsParams = Pick<Comment, 'commentId'>;

export type GetMembersParams = {
  filter: string;
};

export type GetMembersResponse = Pick<Member, 'memberId' | 'nickname' | 'picture'>[];

export type PostMentionNotificationsPayload = {
  memberIds: PickType<Member, 'memberId'>[];
  pathId: PickType<Feed, 'cycleDetailId'>;
  pushCase: 'mention';
};
