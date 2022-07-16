import { getAllChallenges, getMySuccessChallenges } from 'apis/challengeApi/api';
import { GetChallengeResponse } from 'apis/challengeApi/type';
import { PAGE_SIZE } from 'apis/constants';
import { AxiosResponse, AxiosError } from 'axios';
import { Challenge } from 'commonType';
import { useInfiniteQuery, UseInfiniteQueryOptions } from 'react-query';

// 5. 모든 챌린지 조회(GET)
export const useGetAllChallenges = (
  options?: UseInfiniteQueryOptions<AxiosResponse<GetChallengeResponse[]>, AxiosError>,
) =>
  useInfiniteQuery<AxiosResponse<GetChallengeResponse[]>, AxiosError>(
    'getAllChallenges',
    ({ pageParam = 0 }) => getAllChallenges(pageParam),
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
  options?: UseInfiniteQueryOptions<AxiosResponse<Challenge[]>, AxiosError>,
) =>
  useInfiniteQuery<AxiosResponse<Challenge[]>, AxiosError>(
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
