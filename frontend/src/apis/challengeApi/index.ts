import {
  getAllChallenges,
  getAllChallengesAuth,
  getMySuccessChallenges,
  getChallengeById,
  getChallengeByIdAuth,
} from 'apis/challengeApi/api';
import {
  GetChallengeResponse,
  GetChallengeByIdProps,
  GetChallengeByIdResponse,
} from 'apis/challengeApi/type';
import { PAGE_SIZE } from 'apis/constants';
import { AxiosResponse, AxiosError } from 'axios';
import { Challenge, ErrorResponse } from 'commonType';
import {
  useQuery,
  useInfiniteQuery,
  UseQueryOptions,
  UseInfiniteQueryOptions,
} from 'react-query';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/selectors';

// 5. 모든 챌린지 조회(GET)
export const useGetAllChallenges = (
  options?: UseInfiniteQueryOptions<
    AxiosResponse<GetChallengeResponse[]>,
    AxiosError<ErrorResponse>
  >,
) =>
  useInfiniteQuery<AxiosResponse<GetChallengeResponse[]>, AxiosError<ErrorResponse>>(
    'getAllChallenges',
    useRecoilValue(isLoginState)
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
    AxiosResponse<Challenge[]>,
    AxiosError<ErrorResponse>
  >,
) =>
  useInfiniteQuery<AxiosResponse<Challenge[]>, AxiosError<ErrorResponse>>(
    'getMySuccessChallenges',
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
    'getChallengeById',
    useRecoilValue(isLoginState)
      ? () => getChallengeByIdAuth({ challengeId })
      : () => getChallengeById({ challengeId }),
    options,
  );
