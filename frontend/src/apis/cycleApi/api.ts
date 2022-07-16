import { apiClient, authApiClient } from 'apis/apiClient';
import {
  PostCycleProps,
  PostCycleProgressProps,
  PostCycleProgressResponse,
} from 'apis/cycleApi/type';
import { Cycle } from 'commonType';

// 1. 챌린지 사이클 생성(POST)
export const postCycle = async ({ startTime, challengeId }: PostCycleProps) => {
  return authApiClient.axios.post('/cycles', { startTime, challengeId });
};

// 2. 나의 모든 진행 중인 챌린지 사이클 조회(GET)
export const getMyCyclesInProgress = async () => {
  return authApiClient.axios.get<Cycle[]>('/cycles/me?status=inProgress');
};

// 4. 챌린지 사이클의 진척도 증가(POST)
export const postCycleProgress = async ({ cycleId }: PostCycleProgressProps) => {
  return authApiClient.axios.post<PostCycleProgressResponse>(
    `/cycles/${cycleId}/progress`,
  );
};

// 7. 아이디로 사이클 조회(GET)
export const getCycleById = async ({ cycleId }: PostCycleProgressProps) => {
  return apiClient.axios.get<Cycle>(`/cycles/${cycleId}`);
};
