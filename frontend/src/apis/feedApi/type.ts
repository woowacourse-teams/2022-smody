import { Feed, Comment } from 'commonType';

export type GetAllFeedsResponse = Feed[];

export type GetFeedByIdProps = Pick<Feed, 'cycleDetailId'>;

export type GetFeedByIdResponse = Feed;

export type GetCommentsByIdProps = Pick<Feed, 'cycleDetailId'>;

export type GetCommentsByIdResponse = Comment[];
