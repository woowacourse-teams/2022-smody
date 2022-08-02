import { queryKeys } from 'apis/constants';
import {
  postCycle,
  getMyCyclesInProgress,
  getMyCyclesStat,
  postCycleProgress,
  getCycleById,
} from 'apis/cycleApi/api';
import {
  PostCycleProps,
  PostCycleProgressProps,
  PostCycleProgressResponse,
  GetMyCyclesStatResponse,
} from 'apis/cycleApi/type';
import { AxiosResponse, AxiosError } from 'axios';
import { Cycle, ErrorResponse } from 'commonType';
import { useQuery, useMutation, UseQueryOptions, UseMutationOptions } from 'react-query';

// 1. 챌린지 사이클 생성(POST)
export const usePostCycle = (
  options?: UseMutationOptions<AxiosResponse, AxiosError<ErrorResponse>, PostCycleProps>,
) =>
  useMutation<AxiosResponse, AxiosError<ErrorResponse>, PostCycleProps>(
    postCycle,
    options,
  );

// 2. 나의 모든 진행 중인 챌린지 사이클 조회(GET)
export const useGetMyCyclesInProgress = (
  options?: UseQueryOptions<AxiosResponse<Cycle[]>, AxiosError<ErrorResponse>>,
) =>
  useQuery<AxiosResponse<Cycle[]>, AxiosError<ErrorResponse>>(
    queryKeys.getMyCyclesInProgress,
    getMyCyclesInProgress,
    options,
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
  cycleId: PostCycleProgressProps,
  options?: UseQueryOptions<AxiosResponse<Cycle>, AxiosError<ErrorResponse>>,
) =>
  useQuery<AxiosResponse<Cycle>, AxiosError<ErrorResponse>>(
    queryKeys.getCycleById,
    () => getCycleById(cycleId),
    options,
  );
