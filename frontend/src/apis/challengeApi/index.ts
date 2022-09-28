import {
  getAllChallenges,
  getAllChallengesAuth,
  getMyChallenges,
  getChallengeById,
  getChallengeByIdAuth,
  getChallengersById,
  postChallenge,
  getMyChallengeById,
} from 'apis/challengeApi/api';
import {
  GetAllChallengesResponse,
  GetChallengeByIdParams,
  GetChallengeByIdResponse,
  GetMyChallengesResponse,
  GetChallengersByIdResponse,
  GetChallengersByIdParams,
  PostChallengePayload,
  GetChallengeParams,
  GetMyChallengeByIdParams,
  GetMyChallengeByIdResponse,
} from 'apis/challengeApi/type';
import { PAGE_SIZE, queryKeys } from 'apis/constants';
import { AxiosResponse, AxiosError } from 'axios';
import {
  useQuery,
  useInfiniteQuery,
  UseQueryOptions,
  UseInfiniteQueryOptions,
  UseMutationOptions,
  useMutation,
} from 'react-query';
import { ErrorResponse } from 'types/internal';

// 5. 모든 챌린지 조회(GET)
export const useGetAllChallenges = (
  { searchValue }: GetChallengeParams,
  options?: UseInfiniteQueryOptions<
    AxiosResponse<GetAllChallengesResponse>,
    AxiosError<ErrorResponse>
  >,
) =>
  useInfiniteQuery<AxiosResponse<GetAllChallengesResponse>, AxiosError<ErrorResponse>>(
    queryKeys.getAllChallenges,
    localStorage.getItem('accessToken')
      ? ({ pageParam = 0 }) => getAllChallengesAuth(searchValue, pageParam)
      : ({ pageParam = 0 }) => getAllChallenges(searchValue, pageParam),
    {
      ...options,
      getNextPageParam: (currentPage) => {
        const currentDataLength = currentPage.data.length;

        return currentDataLength < PAGE_SIZE.ALL_CHALLENGES
          ? undefined
          : currentPage.data[currentDataLength - 1].challengeId;
      },
    },
  );

// 6. 내가 참가한 챌린지 조회(GET)
export const useGetMyChallenges = (
  options?: UseInfiniteQueryOptions<
    AxiosResponse<GetMyChallengesResponse>,
    AxiosError<ErrorResponse>
  >,
) =>
  useInfiniteQuery<AxiosResponse<GetMyChallengesResponse>, AxiosError<ErrorResponse>>(
    queryKeys.getMyChallenges,
    ({ pageParam = 0 }) => getMyChallenges({ cursorId: pageParam }),
    {
      ...options,
      getNextPageParam: (currentPage) => {
        const currentDataLength = currentPage.data.length;

        return currentDataLength < PAGE_SIZE.SUCCESS_CHALLENGES
          ? undefined
          : currentPage.data[currentDataLength - 1].challengeId;
      },
    },
  );

// 8. 챌린지 하나 상세 조회(GET)
export const useGetChallengeById = (
  { challengeId }: GetChallengeByIdParams,
  options?: UseQueryOptions<
    AxiosResponse<GetChallengeByIdResponse>,
    AxiosError<ErrorResponse>
  >,
) =>
  useQuery<AxiosResponse<GetChallengeByIdResponse>, AxiosError<ErrorResponse>>(
    [queryKeys.getChallengeById, challengeId],
    localStorage.getItem('accessToken')
      ? () => getChallengeByIdAuth({ challengeId })
      : () => getChallengeById({ challengeId }),
    options,
  );

// 9. 챌린지 참가자 목록 조회
export const useGetChallengersById = (
  { challengeId }: GetChallengersByIdParams,
  options?: UseQueryOptions<
    AxiosResponse<GetChallengersByIdResponse>,
    AxiosError<ErrorResponse>
  >,
) =>
  useQuery<AxiosResponse<GetChallengersByIdResponse>, AxiosError<ErrorResponse>>(
    [queryKeys.getChallengersById, challengeId],
    () => getChallengersById({ challengeId }),
    options,
  );

// 10. 챌린지 생성
export const usePostChallenge = (
  options?: UseMutationOptions<
    AxiosResponse,
    AxiosError<ErrorResponse>,
    PostChallengePayload
  >,
) =>
  useMutation<AxiosResponse, AxiosError<ErrorResponse>, PostChallengePayload>(
    postChallenge,
    options,
  );

// 참여한 챌린지 상세 조회 기능
export const useGetMyChallengeById = (
  { challengeId }: GetMyChallengeByIdParams,
  options?: UseQueryOptions<
    AxiosResponse<GetMyChallengeByIdResponse>,
    AxiosError<ErrorResponse>
  >,
) =>
  useQuery<AxiosResponse<GetMyChallengeByIdResponse>, AxiosError<ErrorResponse>>(
    [queryKeys.getMyChallengeById, challengeId],
    () => getMyChallengeById({ challengeId }),
    options,
  );
