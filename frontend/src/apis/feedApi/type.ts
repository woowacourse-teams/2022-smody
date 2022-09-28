import { PickType } from 'smody-library';
import { Feed, Comment } from 'types/feed';

export type GetAllFeedsResponse = Feed[];

export type GetFeedByIdParams = Pick<Feed, 'cycleDetailId'>;

export type GetFeedByIdResponse = Feed;

export type UsePostCommentProps = Pick<Feed, 'cycleDetailId'>;

export type UsePostCommentMutationFunctionProps = Pick<Comment, 'content'>;

export type PostCommentPayload = UsePostCommentProps &
  UsePostCommentMutationFunctionProps;

export interface UseGetCommentsByIdProps extends Pick<Feed, 'cycleDetailId'> {
  isLogin: boolean;
}

export type GetCommentsByIdParams = Pick<Feed, 'cycleDetailId'>;

export type GetCommentsByIdResponse = Comment[];

export type PatchCommentsPayload = Pick<Comment, 'commentId' | 'content'>;

export type DeleteCommentsParams = Pick<Comment, 'commentId'>;
