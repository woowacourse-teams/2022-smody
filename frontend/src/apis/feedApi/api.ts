import { authApiClient, apiClient } from 'apis/apiClient';
import { PAGE_SIZE, SORT } from 'apis/constants';
import {
  GetAllFeedsResponse,
  GetFeedByIdProps,
  GetFeedByIdResponse,
  GetCommentsByIdProps,
  GetCommentsByIdResponse,
  PostCommentProps,
  PatchCommentsProps,
  DeleteCommentsProps,
} from 'apis/feedApi/type';

// 1. 피드 전체 조회(GET)
export const getAllFeeds = async (lastCycleDetailId: number) => {
  const params = {
    lastCycleDetailId,
    size: PAGE_SIZE.FEEDS,
    sort: `${SORT.LATEST}`,
  };

  return apiClient.axios.get<GetAllFeedsResponse>('/feeds', {
    params,
  });
};

// 2. id로 피드 조회(GET)
export const getFeedById = async ({ cycleDetailId }: GetFeedByIdProps) => {
  return apiClient.axios.get<GetFeedByIdResponse>(`/feeds/${cycleDetailId}`);
};

// 3. 댓글 생성(POST)
export const postComments = async ({ cycleDetailId, content }: PostCommentProps) => {
  return authApiClient.axios.post(`/feeds/${cycleDetailId}/comments`, {
    content,
  });
};

// 4. 댓글 조회(GET) - 비회원용
export const getCommentsById = ({ cycleDetailId }: GetCommentsByIdProps) => {
  return apiClient.axios.get<GetCommentsByIdResponse>(`/feeds/${cycleDetailId}/comments`);
};

// 4. 댓글 조회(GET) - 회원용
export const getCommentsByIdAuth = ({ cycleDetailId }: GetCommentsByIdProps) => {
  return authApiClient.axios.get<GetCommentsByIdResponse>(
    `/feeds/${cycleDetailId}/comments/auth`,
  );
};

// 5. 댓글 수정(PATCH)
export const patchComments = async ({ commentId, content }: PatchCommentsProps) => {
  return authApiClient.axios.patch(`comments/${commentId}`, {
    content,
  });
};

// 6. 댓글 삭제(DELETE)
export const deleteComments = async ({ commentId }: DeleteCommentsProps) => {
  return authApiClient.axios.delete(`comments/${commentId}`);
};
