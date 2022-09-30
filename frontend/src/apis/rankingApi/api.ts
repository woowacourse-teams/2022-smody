import { GetRankingPeriodsResponse } from './type';
import { apiClient } from 'apis/apiClient';

// 전체 랭킹 기간 조회
export const getRankingPeriods = async () => {
  const params = {
    sort: 'startDate desc',
  };

  return apiClient.axios.get<GetRankingPeriodsResponse>('/ranking-periods', { params });
};
