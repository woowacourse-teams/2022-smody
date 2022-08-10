import {
  getAllChallenges,
  getAllChallengesAuth,
  getMySuccessChallenges,
  getChallengeById,
  getChallengeByIdAuth,
  getChallengersById,
  postChallenge,
} from 'apis/challengeApi/api';
import {
  GetChallengeResponse,
  GetChallengeByIdProps,
  GetChallengeByIdResponse,
  GetMySuccessChallengesResponse,
  GetChallengersByIdResponse,
  GetChallengersByIdProps,
  PostChallengeProps,
} from 'apis/challengeApi/type';
import { PAGE_SIZE, queryKeys } from 'apis/constants';
import { AxiosResponse, AxiosError } from 'axios';
import { ErrorResponse } from 'commonType';
import {
  useQuery,
  useInfiniteQuery,
  UseQueryOptions,
  UseInfiniteQueryOptions,
  UseMutationOptions,
  useMutation,
} from 'react-query';

// 5. 모든 챌린지 조회(GET)
export const useGetAllChallenges = (
  options?: UseInfiniteQueryOptions<
    AxiosResponse<GetChallengeResponse[]>,
    AxiosError<ErrorResponse>
  >,
) =>
  useInfiniteQuery<AxiosResponse<GetChallengeResponse[]>, AxiosError<ErrorResponse>>(
    queryKeys.getAllChallenges,
    localStorage.getItem('accessToken')
      ? ({ pageParam = 0 }) => getAllChallengesAuth(pageParam)
      : ({ pageParam = 0 }) => getAllChallenges(pageParam),
    {
      ...options,
      getNextPageParam: (currentPage) => {
        return currentPage.data.length < PAGE_SIZE.ALL_CHALLENGES
          ? undefined
          : currentPage.config.params.page + 1;
      },
    },
  );

// 6. 나의 성공한 챌린지 조회(GET)
export const useGetMySuccessChallenges = (
  options?: UseInfiniteQueryOptions<
    AxiosResponse<GetMySuccessChallengesResponse[]>,
    AxiosError<ErrorResponse>
  >,
) =>
  useInfiniteQuery<
    AxiosResponse<GetMySuccessChallengesResponse[]>,
    AxiosError<ErrorResponse>
  >(
    queryKeys.getMySuccessChallenges,
    ({ pageParam = 0 }) => getMySuccessChallenges(pageParam),
    {
      ...options,
      getNextPageParam: (currentPage) => {
        return currentPage.data.length < PAGE_SIZE.SUCCESS_CHALLENGES
          ? undefined
          : currentPage.config.params.page + 1;
      },
    },
  );

// 8. 챌린지 하나 상세 조회(GET)
export const useGetChallengeById = (
  { challengeId }: GetChallengeByIdProps,
  options?: UseQueryOptions<
    AxiosResponse<GetChallengeByIdResponse>,
    AxiosError<ErrorResponse>
  >,
) =>
  useQuery<AxiosResponse<GetChallengeByIdResponse>, AxiosError<ErrorResponse>>(
    queryKeys.getChallengeById,
    localStorage.getItem('accessToken')
      ? () => getChallengeByIdAuth({ challengeId })
      : () => getChallengeById({ challengeId }),
    options,
  );

// 9. 챌린지 참가자 목록 조회
export const useGetChallengersById = (
  { challengeId }: GetChallengersByIdProps,
  options?: UseQueryOptions<
    AxiosResponse<GetChallengersByIdResponse[]>,
    AxiosError<ErrorResponse>
  >,
) =>
  useQuery<AxiosResponse<GetChallengersByIdResponse[]>, AxiosError<ErrorResponse>>(
    queryKeys.getChallengersById,
    () => getChallengersById({ challengeId }),
    options,
  );

// 10. 챌린지 생성
export const usePostChallenge = (
  options?: UseMutationOptions<
    AxiosResponse,
    AxiosError<ErrorResponse>,
    PostChallengeProps
  >,
) =>
  useMutation<AxiosResponse, AxiosError<ErrorResponse>, PostChallengeProps>(
    postChallenge,
    options,
  );
