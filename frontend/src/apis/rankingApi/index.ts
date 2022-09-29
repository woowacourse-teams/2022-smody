import { getRankingPeriods } from './api';
import { GetRankingPeriodsResponse } from './type';
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
    [queryKeys.getRankingPeriods],
    () => getRankingPeriods(),
    options,
  );
