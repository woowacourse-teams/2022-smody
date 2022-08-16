import { apiClient, authApiClient } from 'apis/apiClient';
import { PAGE_SIZE } from 'apis/constants';
import {
  PostCycleProps,
  PostCycleProgressProps,
  PostCycleProgressResponse,
  GetCycleByIdProps,
  GetMyCyclesStatResponse,
  GetCycleByIdResponse,
  GetMyCyclesInProgressResponse,
  GetMyCyclesByChallengeIdProps,
  GetMyCyclesByChallengeIdResponse,
  getMyCyclesByChallengeIdAPIProps,
} from 'apis/cycleApi/type';

// 1. 챌린지 사이클 생성(POST)
export const postCycle = async ({ challengeId, startTime }: PostCycleProps) => {
  return authApiClient.axios.post('/cycles', { challengeId, startTime });
};

// 2. 나의 모든 진행 중인 챌린지 사이클 조회(GET)
export const getMyCyclesInProgress = async () => {
  return authApiClient.axios.get<GetMyCyclesInProgressResponse[]>('/cycles/me');
};

// 3. 나의 사이클 통계 정보 조회(GET)
export const getMyCyclesStat = async () => {
  return authApiClient.axios.get<GetMyCyclesStatResponse>('/cycles/me/stat');
};

// 4. 챌린지 사이클의 진척도 증가(POST)
export const postCycleProgress = async ({
  cycleId,
  formData,
}: PostCycleProgressProps) => {
  return authApiClient.axios.post<PostCycleProgressResponse>(
    `/cycles/${cycleId}/progress`,
    formData,
    {
      headers: { 'Content-Type': 'multipart/form-data' },
    },
  );
};

// 7. 아이디로 사이클 조회(GET)
export const getCycleById = async ({ cycleId }: GetCycleByIdProps) => {
  return apiClient.axios.get<GetCycleByIdResponse>(`/cycles/${cycleId}`);
};

// 챌린지에 대한 전체 사이클 상세 조회 기능
export const getMyCyclesByChallengeId = async ({
  challengeId,
  lastCycleId,
}: getMyCyclesByChallengeIdAPIProps) => {
  const params = {
    size: PAGE_SIZE.CYCLES,
    lastCycleId,
  };

  return apiClient.axios.get<GetMyCyclesByChallengeIdResponse[]>(
    `/cycles/me/${challengeId}`,
    { params },
  );
};
