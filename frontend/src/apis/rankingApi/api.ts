import {
  GetAllRankingParams,
  GetAllRankingResponse,
  GetMyRankingParams,
  GetMyRankingResponse,
  GetRankingPeriodsResponse,
} from './type';
import { apiClient, authApiClient } from 'apis/apiClient';

// 전체 랭킹 기간 조회
export const getRankingPeriods = async () => {
  const params = {
    sort: 'startDate:desc',
  };

  return apiClient.axios.get<GetRankingPeriodsResponse>('/ranking-periods', { params });
};

// 나의 랭킹
export const getMyRanking = async ({ rankingPeriodId }: GetMyRankingParams) => {
  return authApiClient.axios.get<GetMyRankingResponse>(
    `/ranking-periods/${rankingPeriodId}/ranking-activities/me`,
  );
};

// 전체 랭킹 활동 조회
export const getAllRanking = async ({ rankingPeriodId }: GetAllRankingParams) => {
  return authApiClient.axios.get<GetAllRankingResponse>(
    `/ranking-periods/${rankingPeriodId}/ranking-activities`,
  );
};
