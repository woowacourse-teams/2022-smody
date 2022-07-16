import { apiClient } from 'apis/apiClient';
import { GetChallengeResponse } from 'apis/challengeApi/type';

// 5. 모든 챌린지 조회(GET)
export const getAllChallenges = async () => {
  return apiClient.axios.get<GetChallengeResponse[]>('/challenges');
};
