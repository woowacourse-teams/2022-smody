import { getAllRanking, getMyRanking, getRankingPeriods } from './api';
import {
  GetAllRankingParams,
  GetAllRankingResponse,
  GetMyRankingParams,
  GetMyRankingResponse,
  GetRankingPeriodsResponse,
} from './type';
import { queryKeys } from 'apis/constants';
import { AxiosError, AxiosResponse } from 'axios';
import { useQuery, UseQueryOptions } from 'react-query';
import { ErrorResponse } from 'types/internal';

// 전체 랭킹 기간 조회
export const useGetRankingPeriods = (
  options?: UseQueryOptions<
    AxiosResponse<GetRankingPeriodsResponse>,
    AxiosError<ErrorResponse>
  >,
) =>
  useQuery<AxiosResponse<GetRankingPeriodsResponse>, AxiosError<ErrorResponse>>(
    queryKeys.getRankingPeriods,
    () => getRankingPeriods(),
    options,
  );

// 나의 랭킹
export const useGetMyRanking = (
  { rankingPeriodId }: GetMyRankingParams,
  options?: UseQueryOptions<
    AxiosResponse<GetMyRankingResponse>,
    AxiosError<ErrorResponse>
  >,
) =>
  useQuery<AxiosResponse<GetMyRankingResponse>, AxiosError<ErrorResponse>>(
    [queryKeys.getMyRanking, rankingPeriodId],
    () => getMyRanking({ rankingPeriodId }),
    options,
  );

// 전체 랭킹 활동 조회
export const useGetAllRanking = (
  { rankingPeriodId }: GetAllRankingParams,
  options?: UseQueryOptions<
    AxiosResponse<GetAllRankingResponse>,
    AxiosError<ErrorResponse>
  >,
) =>
  useQuery<AxiosResponse<GetAllRankingResponse>, AxiosError<ErrorResponse>>(
    [queryKeys.getAllRanking, rankingPeriodId],
    () => getAllRanking({ rankingPeriodId }),
    options,
  );
