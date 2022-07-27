import { apiClient, authApiClient } from 'apis/apiClient';
import { GetChallengeResponse, GetChallengeByIdProps } from 'apis/challengeApi/type';
import { PAGE_SIZE } from 'apis/constants';
import { Challenge } from 'commonType';

// 5. 모든 챌린지 조회(GET)- 비회원용
export const getAllChallenges = async (pageParam: number) => {
  const params = { page: pageParam, size: PAGE_SIZE.ALL_CHALLENGES };

  return apiClient.axios.get<GetChallengeResponse[]>(`/challenges`, {
    params,
  });
};

// 5. 모든 챌린지 조회(GET) - 회원용
export const getAllChallengesAuth = async (pageParam: number) => {
  const params = { page: pageParam, size: PAGE_SIZE.ALL_CHALLENGES };

  return authApiClient.axios.get<GetChallengeResponse[]>(`/challenges/auth`, {
    params,
  });
};

// 6. 나의 성공한 챌린지 조회(GET)
export const getMySuccessChallenges = async (pageParam: number) => {
  const params = { page: pageParam, size: PAGE_SIZE.SUCCESS_CHALLENGES };

  return authApiClient.axios.get<Challenge[]>(`/challenges/me`, { params });
};

// 8. 챌린지 하나 상세 조회(GET) - 비회원용
export const getChallengeById = async ({ challengeId }: GetChallengeByIdProps) => {
  return apiClient.axios.get<GetChallengeResponse>(`/challenges/${challengeId}`);
};

// 8. 챌린지 하나 상세 조회(GET) - 회원용
export const getChallengeByIdAuth = async ({ challengeId }: GetChallengeByIdProps) => {
  return authApiClient.axios.get<GetChallengeResponse>(`/challenges/${challengeId}/auth`);
};
