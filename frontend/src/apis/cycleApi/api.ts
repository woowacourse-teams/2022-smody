import { apiClient, authApiClient } from 'apis/apiClient';
import { PAGE_SIZE } from 'apis/constants';
import {
  PostCyclePayload,
  GetMyCyclesInProgressParams,
  PostCycleProgressPayload,
  PostCycleProgressResponse,
  GetCycleByIdParams,
  GetMyCyclesStatResponse,
  GetCycleByIdResponse,
  GetMyCyclesInProgressResponse,
  GetMyCyclesByChallengeIdResponse,
  GetMyCyclesByChallengeIdAPIParams,
} from 'apis/cycleApi/type';

// 1. 챌린지 사이클 생성(POST)
export const postCycle = async ({ challengeId, startTime }: PostCyclePayload) => {
  return authApiClient.axios.post('/cycles', { challengeId, startTime });
};

// 2. 나의 모든 진행 중인 챌린지 사이클 조회(GET)
export const getMyCyclesInProgress = async ({
  cursorId,
}: GetMyCyclesInProgressParams) => {
  const params = {
    size: PAGE_SIZE.CYCLES,
    cursorId,
  };

  return authApiClient.axios.get<GetMyCyclesInProgressResponse>('/cycles/me', {
    params,
  });
};

// 3. 나의 사이클 통계 정보 조회(GET)
export const getMyCyclesStat = async () => {
  return authApiClient.axios.get<GetMyCyclesStatResponse>('/cycles/me/stat');
};

// 4. 챌린지 사이클의 진척도 증가(POST)
export const postCycleProgress = async ({
  cycleId,
  formData,
}: PostCycleProgressPayload) => {
  return authApiClient.axios.post<PostCycleProgressResponse>(
    `/cycles/${cycleId}/progress`,
    formData,
    {
      headers: { 'Content-Type': 'multipart/form-data' },
    },
  );
};

// 7. 아이디로 사이클 조회(GET)
export const getCycleById = async ({ cycleId }: GetCycleByIdParams) => {
  return apiClient.axios.get<GetCycleByIdResponse>(`/cycles/${cycleId}`);
};

// TODO 커서페이징 이거 참고할 것
// 챌린지에 대한 전체 사이클 상세 조회 기능
export const getMyCyclesByChallengeId = async ({
  challengeId,
  cursorId,
  filter,
}: GetMyCyclesByChallengeIdAPIParams) => {
  const params =
    filter === 'all'
      ? {
          size: PAGE_SIZE.CYCLES,
          cursorId,
        }
      : { size: PAGE_SIZE.CYCLES, cursorId, filter };

  return authApiClient.axios.get<GetMyCyclesByChallengeIdResponse[]>(
    `/cycles/me/${challengeId}`,
    { params },
  );
};
