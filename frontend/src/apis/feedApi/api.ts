import { authApiClient, apiClient } from 'apis/apiClient';
import { PAGE_SIZE, SORT } from 'apis/constants';
import {
  GetAllFeedsResponse,
  GetFeedByIdParams,
  GetFeedByIdResponse,
  GetCommentsByIdParams,
  GetCommentsByIdResponse,
  PostCommentPayload,
  PatchCommentsPayload,
  DeleteCommentsParams,
  GetMembersResponse,
} from 'apis/feedApi/type';

// 1. 피드 전체 조회(GET)
export const getAllFeeds = async (cursorId: number) => {
  const params = {
    cursorId,
    size: PAGE_SIZE.FEEDS,
    sort: `${SORT.LATEST}`,
  };

  return apiClient.axios.get<GetAllFeedsResponse>('/feeds', {
    params,
  });
};

// 2. id로 피드 조회(GET)
export const getFeedById = async ({ cycleDetailId }: GetFeedByIdParams) => {
  return apiClient.axios.get<GetFeedByIdResponse>(`/feeds/${cycleDetailId}`);
};

// 3. 댓글 생성(POST)
export const postComments = async ({ cycleDetailId, content }: PostCommentPayload) => {
  return authApiClient.axios.post(`/feeds/${cycleDetailId}/comments`, {
    content,
  });
};

// 4. 댓글 조회(GET) - 비회원용
export const getCommentsById = ({ cycleDetailId }: GetCommentsByIdParams) => {
  return apiClient.axios.get<GetCommentsByIdResponse>(`/feeds/${cycleDetailId}/comments`);
};

// 4. 댓글 조회(GET) - 회원용
export const getCommentsByIdAuth = ({ cycleDetailId }: GetCommentsByIdParams) => {
  return authApiClient.axios.get<GetCommentsByIdResponse>(
    `/feeds/${cycleDetailId}/comments/auth`,
  );
};

// 5. 댓글 수정(PATCH)
export const patchComments = async ({ commentId, content }: PatchCommentsPayload) => {
  return authApiClient.axios.patch(`comments/${commentId}`, {
    content,
  });
};

// 6. 댓글 삭제(DELETE)
export const deleteComments = async ({ commentId }: DeleteCommentsParams) => {
  return authApiClient.axios.delete(`comments/${commentId}`);
};

// 7. 댓글에서 @를 눌렀을 때
export const getMembers = async (filter: string, cursorId: number) => {
  const params = filter !== '' && {
    cursorId,
    filter: filter,
  };
  return authApiClient.axios.get<GetMembersResponse>(`members`, {
    params,
  });
};
