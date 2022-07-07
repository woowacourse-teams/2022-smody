import {
  postCycle,
  getMyCyclesInProgress,
  postCycleProgress,
  getCycleById,
  getAllChallenges,
} from 'apis/challengeApi/api';
import {
  PostCycleProps,
  PostCycleProgressProps,
  PostCycleProgressResponse,
  GetChallengeResponse,
} from 'apis/challengeApi/type';
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

// 3. 챌린지 사이클의 진척도 증가(POST)
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

// 4. 모든 챌린지 조회(GET)
export const useGetAllChallenges = (
  options?: UseQueryOptions<AxiosResponse<GetChallengeResponse[]>, AxiosError>,
) =>
  useQuery<AxiosResponse<GetChallengeResponse[]>, AxiosError>(
    'getAllChallenges',
    getAllChallenges,
    options,
  );

// 5. 아이디로 사이클 조회(GET)
export const useGetCycleById = (
  cycleId: PostCycleProgressProps,
  options?: UseQueryOptions<AxiosResponse<Cycle>, AxiosError>,
) =>
  useQuery<AxiosResponse<Cycle>, AxiosError>(
    'getCycleById',
    () => getCycleById(cycleId),
    options,
  );
