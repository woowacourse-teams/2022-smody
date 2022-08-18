import { PAGE_SIZE, queryKeys } from 'apis/constants';
import {
  postCycle,
  getMyCyclesInProgress,
  getMyCyclesStat,
  postCycleProgress,
  getCycleById,
  getMyCyclesByChallengeId,
} from 'apis/cycleApi/api';
import {
  PostCycleProps,
  PostCycleProgressProps,
  PostCycleProgressResponse,
  GetMyCyclesStatResponse,
  GetCycleByIdResponse,
  GetCycleByIdProps,
  GetMyCyclesInProgressResponse,
  GetMyCyclesByChallengeIdProps,
  GetMyCyclesByChallengeIdResponse,
} from 'apis/cycleApi/type';
import { AxiosResponse, AxiosError } from 'axios';
import { ErrorResponse } from 'commonType';
import {
  useQuery,
  useMutation,
  UseQueryOptions,
  UseMutationOptions,
  UseInfiniteQueryOptions,
  useInfiniteQuery,
} from 'react-query';

// 1. 챌린지 사이클 생성(POST)
export const usePostCycle = (
  options?: UseMutationOptions<AxiosResponse, AxiosError<ErrorResponse>, PostCycleProps>,
) =>
  useMutation<AxiosResponse, AxiosError<ErrorResponse>, PostCycleProps>(
    postCycle,
    options,
  );

// TODO 4. 인피니트 쿼리 및 커서페이징 처리
// 2. 나의 모든 진행 중인 챌린지 사이클 조회(GET)
export const useGetMyCyclesInProgress = (
  options?: UseInfiniteQueryOptions<
    AxiosResponse<GetMyCyclesInProgressResponse[]>,
    AxiosError<ErrorResponse>
  >,
) =>
  useInfiniteQuery<
    AxiosResponse<GetMyCyclesInProgressResponse[]>,
    AxiosError<ErrorResponse>
  >(
    queryKeys.getMyCyclesInProgress,
    ({ pageParam = 0 }) => getMyCyclesInProgress({ cursorId: pageParam }),
    {
      ...options,
      getNextPageParam: (currentPage) => {
        const currentDataLength = currentPage.data.length;

        return currentDataLength < PAGE_SIZE.CYCLES
          ? undefined
          : currentPage.data[currentDataLength - 1].cycleId;
      },
    },
  );

// 3. 나의 사이클 통계 정보 조회(GET)
export const useGetMyCyclesStat = (
  options?: UseQueryOptions<
    AxiosResponse<GetMyCyclesStatResponse>,
    AxiosError<ErrorResponse>
  >,
) =>
  useQuery<AxiosResponse<GetMyCyclesStatResponse>, AxiosError<ErrorResponse>>(
    queryKeys.getMyCyclesStat,
    getMyCyclesStat,
    options,
  );

// 4. 챌린지 사이클의 진척도 증가(POST)
export const usePostCycleProgress = (
  options?: UseMutationOptions<
    AxiosResponse<PostCycleProgressResponse>,
    AxiosError<ErrorResponse>,
    PostCycleProgressProps
  >,
) =>
  useMutation<
    AxiosResponse<PostCycleProgressResponse>,
    AxiosError<ErrorResponse>,
    PostCycleProgressProps
  >(postCycleProgress, options);

// 7. 아이디로 사이클 조회(GET)
export const useGetCycleById = (
  { cycleId }: GetCycleByIdProps,
  options?: UseQueryOptions<
    AxiosResponse<GetCycleByIdResponse>,
    AxiosError<ErrorResponse>
  >,
) =>
  useQuery<AxiosResponse<GetCycleByIdResponse>, AxiosError<ErrorResponse>>(
    [queryKeys.getCycleById, cycleId],
    () => getCycleById({ cycleId }),
    options,
  );

// TODO 커서페이징은 이거 참고
// 챌린지에 대한 전체 사이클 상세 조회 기능
export const useGetMyCyclesByChallengeId = (
  { challengeId, filter }: GetMyCyclesByChallengeIdProps,
  options?: UseInfiniteQueryOptions<
    AxiosResponse<GetMyCyclesByChallengeIdResponse[]>,
    AxiosError<ErrorResponse>
  >,
) =>
  useInfiniteQuery<
    AxiosResponse<GetMyCyclesByChallengeIdResponse[]>,
    AxiosError<ErrorResponse>
  >(
    [queryKeys.getMyCyclesByChallengeId, challengeId, filter],
    ({ pageParam = 0 }) =>
      getMyCyclesByChallengeId({ challengeId, filter, cursorId: pageParam }),
    {
      ...options,
      getNextPageParam: (currentPage) => {
        const currentDataLength = currentPage.data.length;

        return currentDataLength < PAGE_SIZE.CYCLES
          ? undefined
          : currentPage.data[currentDataLength - 1].cycleId;
      },
    },
  );
