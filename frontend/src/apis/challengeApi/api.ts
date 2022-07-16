import { apiClient, authApiClient } from 'apis/apiClient';
import { GetChallengeResponse } from 'apis/challengeApi/type';
import { PAGE_SIZE } from 'apis/constants';
import { Challenge } from 'commonType';

// 5. 모든 챌린지 조회(GET)
export const getAllChallenges = async (pageParam: number) => {
  const params = { page: pageParam, size: PAGE_SIZE.ALL_CHALLENGES };

  return apiClient.axios.get<GetChallengeResponse[]>(`/challenges`, {
    params,
  });
};

// 6. 나의 성공한 챌린지 조회(GET)
export const getMySuccessChallenges = async (pageParam: number) => {
  const params = { page: pageParam, size: PAGE_SIZE.SUCCESS_CHALLENGES };

  return authApiClient.axios.get<Challenge[]>(`/challenges/me`, { params });
};
