import { Feed } from 'commonType';

export type GetAllFeedsResponse = Feed[];

export type GetFeedByIdProps = Pick<Feed, 'cycleDetailId'>;

export type GetFeedByIdResponse = Feed;
