import { PAGE_SIZE, queryKeys } from 'apis/constants';
import {
  getAllFeeds,
  getCommentsById,
  getCommentsByIdAuth,
  getFeedById,
  postComments,
  patchComments,
  deleteComments,
  getMembers,
  postMentionNotifications,
} from 'apis/feedApi/api';
import {
  GetAllFeedsResponse,
  GetFeedByIdParams,
  GetFeedByIdResponse,
  UseGetCommentsByIdProps,
  GetCommentsByIdResponse,
  UsePostCommentProps,
  UsePostCommentMutationFunctionProps,
  PatchCommentsPayload,
  DeleteCommentsParams,
  GetMembersParams,
  GetMembersResponse,
  PostMentionNotificationsPayload,
} from 'apis/feedApi/type';
import { AxiosResponse, AxiosError } from 'axios';
import {
  useQuery,
  useMutation,
  useInfiniteQuery,
  UseQueryOptions,
  UseMutationOptions,
  UseInfiniteQueryOptions,
} from 'react-query';
import { ErrorResponse } from 'types/internal';

// TODO 3. 커서페이징 처리
// 1. 피드 전체 조회(GET)
export const useGetAllFeeds = (
  options?: UseInfiniteQueryOptions<
    AxiosResponse<GetAllFeedsResponse>,
    AxiosError<ErrorResponse>
  >,
) =>
  useInfiniteQuery<AxiosResponse<GetAllFeedsResponse>, AxiosError<ErrorResponse>>(
    queryKeys.getAllFeeds,
    ({ pageParam = 0 }) => getAllFeeds(pageParam),
    {
      ...options,
      getNextPageParam: (currentPage) => {
        const currentDataLength = currentPage.data.length;

        return currentDataLength < PAGE_SIZE.FEEDS
          ? undefined
          : currentPage.data[currentDataLength - 1].cycleDetailId;
      },
    },
  );

// 2. id로 피드 조회(GET)
export const useGetFeedById = (
  { cycleDetailId }: GetFeedByIdParams,
  options?: UseQueryOptions<
    AxiosResponse<GetFeedByIdResponse>,
    AxiosError<ErrorResponse>
  >,
) =>
  useQuery<AxiosResponse<GetFeedByIdResponse>, AxiosError<ErrorResponse>>(
    [queryKeys.getFeedById, cycleDetailId],
    () => getFeedById({ cycleDetailId }),
    options,
  );

// 3. 댓글 생성(POST)
export const usePostComment = (
  { cycleDetailId }: UsePostCommentProps,
  options?: UseMutationOptions<
    AxiosResponse,
    AxiosError<ErrorResponse>,
    UsePostCommentMutationFunctionProps
  >,
) =>
  useMutation<
    AxiosResponse,
    AxiosError<ErrorResponse>,
    UsePostCommentMutationFunctionProps
  >(({ content }) => postComments({ cycleDetailId, content }), options);

// 4. 댓글 조회(GET)
export const useGetCommentsById = (
  { cycleDetailId, isLogin }: UseGetCommentsByIdProps,
  options?: UseQueryOptions<
    AxiosResponse<GetCommentsByIdResponse>,
    AxiosError<ErrorResponse>
  >,
) =>
  useQuery<AxiosResponse<GetCommentsByIdResponse>, AxiosError<ErrorResponse>>(
    [queryKeys.getCommentsById, cycleDetailId],
    isLogin
      ? () => getCommentsByIdAuth({ cycleDetailId })
      : () => getCommentsById({ cycleDetailId }),
    options,
  );

// 5. 댓글 수정(PATCH)
export const usePatchComments = (
  options?: UseMutationOptions<
    AxiosResponse,
    AxiosError<ErrorResponse>,
    PatchCommentsPayload
  >,
) =>
  useMutation<AxiosResponse, AxiosError<ErrorResponse>, PatchCommentsPayload>(
    ({ commentId, content }) => patchComments({ commentId, content }),
    options,
  );

// 6. 댓글 삭제(DELETE)
export const useDeleteComments = (
  options?: UseMutationOptions<
    AxiosResponse,
    AxiosError<ErrorResponse>,
    DeleteCommentsParams
  >,
) =>
  useMutation<AxiosResponse, AxiosError<ErrorResponse>, DeleteCommentsParams>(
    ({ commentId }) => deleteComments({ commentId }),
    options,
  );

// 7. 댓글에서 @를 눌렀을 때
export const useGetMembers = (
  { filter }: GetMembersParams,
  options?: UseInfiniteQueryOptions<
    AxiosResponse<GetMembersResponse>,
    AxiosError<ErrorResponse>
  >,
) =>
  useInfiniteQuery<AxiosResponse<GetMembersResponse>, AxiosError<ErrorResponse>>(
    [queryKeys.getMembers, filter],
    ({ pageParam = 0 }) => getMembers(filter, pageParam),
    {
      ...options,
      getNextPageParam: (currentPage) => {
        const currentDataLength = currentPage.data.length;

        return currentDataLength < PAGE_SIZE.ALL_MEMBERS
          ? undefined
          : currentPage.data[currentDataLength - 1].memberId;
      },
    },
  );

// 8. 댓글 멘션에서 알림 보내기
export const usePostMentionNotifications = (
  options?: UseMutationOptions<
    AxiosResponse,
    AxiosError<ErrorResponse>,
    PostMentionNotificationsPayload
  >,
) =>
  useMutation<AxiosResponse, AxiosError<ErrorResponse>, PostMentionNotificationsPayload>(
    ({ memberIds, pathId }) => postMentionNotifications({ memberIds, pathId }),
    options,
  );
