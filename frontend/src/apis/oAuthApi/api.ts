import { apiClient, authApiClient } from 'apis/apiClient';
import {
  GetMyInfoResponse,
  GetTokenGoogleResponse,
  PatchMyInfoProps,
} from 'apis/oAuthApi/type';

// 구글 링크 조회(GET)
export const getLinkGoogle = async () => {
  return apiClient.axios.get<string>('/oauth/link/google');
};

// 구글 토큰 조회(GET)
export const getTokenGoogle = async (code: string) => {
  const params = { code };

  return apiClient.axios.get<GetTokenGoogleResponse>('/oauth/login/google', {
    params,
  });
};

// 1. 내 정보 조회(GET)
export const getMyInfo = async () => {
  return authApiClient.axios.get<GetMyInfoResponse>('/members/me');
};

// 3. 회원 정보 수정(PATCH)
export const patchMyInfo = async (updatedUserInfo: PatchMyInfoProps) => {
  return authApiClient.axios.patch('/members/me', updatedUserInfo);
};

// 4. 회원 탈퇴(DELETE)
export const deleteMyInfo = async () => {
  return authApiClient.axios.delete('/members/me');
};
