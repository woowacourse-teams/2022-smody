import {
  postCycle,
  getMyCyclesInProgress,
  postCycleProgress,
  getCycleById,
} from 'apis/cycleApi/api';
import {
  PostCycleProps,
  PostCycleProgressProps,
  PostCycleProgressResponse,
} from 'apis/cycleApi/type';
import { AxiosResponse, AxiosError } from 'axios';
import { Cycle } from 'commonType';
import { useQuery, useMutation, UseQueryOptions, UseMutationOptions } from 'react-query';

// 1. 챌린지 사이클 생성(POST)
export const usePostCycle = (
  options?: UseMutationOptions<AxiosResponse, AxiosError, PostCycleProps>,
) => useMutation<AxiosResponse, AxiosError, PostCycleProps>(postCycle, options);

// 2. 나의 모든 진행 중인 챌린지 사이클 조회(GET)
export const useGetMyCyclesInProgress = (
  options?: UseQueryOptions<AxiosResponse<Cycle[]>, AxiosError>,
) =>
  useQuery<AxiosResponse<Cycle[]>, AxiosError>(
    'getMyCyclesInResponse',
    getMyCyclesInProgress,
    options,
  );

// 4. 챌린지 사이클의 진척도 증가(POST)
export const usePostCycleProgress = (
  options?: UseMutationOptions<
    AxiosResponse<PostCycleProgressResponse>,
    AxiosError,
    PostCycleProgressProps
  >,
) =>
  useMutation<
    AxiosResponse<PostCycleProgressResponse>,
    AxiosError,
    PostCycleProgressProps
  >(postCycleProgress, options);

// 7. 아이디로 사이클 조회(GET)
export const useGetCycleById = (
  cycleId: PostCycleProgressProps,
  options?: UseQueryOptions<AxiosResponse<Cycle>, AxiosError>,
) =>
  useQuery<AxiosResponse<Cycle>, AxiosError>(
    'getCycleById',
    () => getCycleById(cycleId),
    options,
  );
