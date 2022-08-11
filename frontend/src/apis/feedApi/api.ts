import { apiClient } from 'apis/apiClient';
import { PAGE_SIZE } from 'apis/constants';
import {
  GetAllFeedsResponse,
  GetFeedByIdProps,
  GetFeedByIdResponse,
  GetCommentsByIdProps,
  GetCommentsByIdResponse,
} from 'apis/feedApi/type';

// 1. 피드 전체 조회(GET)
export const getAllFeeds = async (lastCycleDetailId: number) => {
  const params = { lastCycleDetailId, size: PAGE_SIZE.FEEDS };

  return apiClient.axios.get<GetAllFeedsResponse>('/feeds', {
    params,
  });
};

// 2. id로 피드 조회(GET)
export const getFeedById = async ({ cycleDetailId }: GetFeedByIdProps) => {
  return apiClient.axios.get<GetFeedByIdResponse>(`/feeds/${cycleDetailId}`);
};

// 4. 댓글 조회(GET)
export const getCommentsById = ({ cycleDetailId }: GetCommentsByIdProps) => {
  return apiClient.axios.get<GetCommentsByIdResponse>(`/feeds/${cycleDetailId}/comments`);
};
