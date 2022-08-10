import { apiClient } from 'apis/apiClient';
import { PAGE_SIZE } from 'apis/constants';
import { GetAllFeedsResponse } from 'apis/feedApi/type';

// 1. 피드 전체 조회(GET)
export const getAllFeeds = async (lastCycleDetailId: number) => {
  const params = { lastCycleDetailId, size: PAGE_SIZE.FEEDS };

  return apiClient.axios.get<GetAllFeedsResponse>('/feeds', {
    params,
  });
};
