import { apiClient, authApiClient } from 'apis/apiClient';
import {
  GetAllChallengesResponse,
  GetChallengeByIdParams,
  GetMyChallengesResponse,
  GetChallengeByIdResponse,
  GetChallengersByIdParams,
  GetChallengersByIdResponse,
  PostChallengePayload,
  GetMyChallengeByIdParams,
  GetMyChallengeByIdResponse,
  GetMyChallengesParams,
  GetAllChallengesParams,
  GetAllChallengesParamsObject,
} from 'apis/challengeApi/type';
import { PAGE_SIZE } from 'apis/constants';

// 5. 모든 챌린지 조회(GET)- 비회원용
export const getAllChallenges = async ({
  searchValue,
  sort,
  cursorId,
}: GetAllChallengesParams) => {
  const params: GetAllChallengesParamsObject = {
    cursorId,
    size: PAGE_SIZE.ALL_CHALLENGES,
  };

  if (typeof searchValue !== 'undefined' && searchValue !== '') {
    params.filter = searchValue;
  }
  if (typeof sort !== 'undefined') {
    params.sort = sort;
  }

  return apiClient.axios.get<GetAllChallengesResponse>(`/challenges`, {
    params,
  });
};

// 5. 모든 챌린지 조회(GET) - 회원용
export const getAllChallengesAuth = async ({
  searchValue,
  sort,
  cursorId,
}: GetAllChallengesParams) => {
  const params: GetAllChallengesParamsObject = {
    cursorId,
    size: PAGE_SIZE.ALL_CHALLENGES,
  };

  if (typeof searchValue !== 'undefined' && searchValue !== '') {
    params.filter = searchValue;
  }
  if (typeof sort !== 'undefined') {
    params.sort = sort;
  }

  return authApiClient.axios.get<GetAllChallengesResponse>(`/challenges/auth`, {
    params,
  });
};

// 6. 나의 성공한 챌린지 조회(GET)
export const getMyChallenges = async ({ cursorId }: GetMyChallengesParams) => {
  const params = { size: PAGE_SIZE.SUCCESS_CHALLENGES, cursorId };

  return authApiClient.axios.get<GetMyChallengesResponse>(`/challenges/me`, {
    params,
  });
};

// 8. 챌린지 하나 상세 조회(GET) - 비회원용
export const getChallengeById = async ({ challengeId }: GetChallengeByIdParams) => {
  return apiClient.axios.get<GetChallengeByIdResponse>(`/challenges/${challengeId}`);
};

// 8. 챌린지 하나 상세 조회(GET) - 회원용
export const getChallengeByIdAuth = async ({ challengeId }: GetChallengeByIdParams) => {
  return authApiClient.axios.get<GetChallengeByIdResponse>(
    `/challenges/${challengeId}/auth`,
  );
};

// 9. 챌린지 참가자 목록 조회
export const getChallengersById = async ({ challengeId }: GetChallengersByIdParams) => {
  return authApiClient.axios.get<GetChallengersByIdResponse>(
    `/challenges/${challengeId}/challengers`,
  );
};

// 10. 챌린지 생성
export const postChallenge = async ({
  challengeName,
  description,
  emojiIndex,
  colorIndex,
}: PostChallengePayload) => {
  return authApiClient.axios.post('/challenges', {
    challengeName,
    description,
    emojiIndex,
    colorIndex,
  });
};

// 참여한 챌린지 상세 조회 기능
export const getMyChallengeById = async ({ challengeId }: GetMyChallengeByIdParams) => {
  return authApiClient.axios.get<GetMyChallengeByIdResponse>(
    `/challenges/me/${challengeId}`,
  );
};
