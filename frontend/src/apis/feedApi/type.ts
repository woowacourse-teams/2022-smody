import { Feed, Comment } from 'commonType';

export type GetAllFeedsResponse = Feed[];

export type GetFeedByIdProps = Pick<Feed, 'cycleDetailId'>;

export type GetFeedByIdResponse = Feed;

export type UsePostCommentProps = Pick<Feed, 'cycleDetailId'>;

export type UsePostCommentMutationFunctionProps = Pick<Comment, 'content'>;

export type PostCommentProps = Pick<Feed, 'cycleDetailId'> & Pick<Comment, 'content'>;

export interface UseGetCommentsByIdProps extends Pick<Feed, 'cycleDetailId'> {
  isLogin: boolean;
}

export type GetCommentsByIdProps = Pick<Feed, 'cycleDetailId'>;

export type GetCommentsByIdResponse = Comment[];

export type PatchCommentsProps = Pick<Comment, 'commentId' | 'content'>;

export type DeleteCommentsProps = Pick<Comment, 'commentId'>;
