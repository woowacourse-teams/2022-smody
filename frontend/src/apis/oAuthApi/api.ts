import { apiClient, authApiClient } from 'apis/apiClient';
import {
  GetMyInfoResponse,
  GetTokenGoogleParams,
  GetTokenGoogleResponse,
  PatchMyInfoContent,
  PostProfileImageContent,
} from 'apis/oAuthApi/type';

// 구글 링크 조회(GET)
export const getLinkGoogle = async () => {
  return apiClient.axios.get<string>('/oauth/link/google');
};

// 구글 토큰 조회(GET)
export const getTokenGoogle = async ({ code }: GetTokenGoogleParams) => {
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
export const patchMyInfo = async (updatedUserInfo: PatchMyInfoContent) => {
  return authApiClient.axios.patch('/members/me', updatedUserInfo);
};

// 4. 회원 탈퇴(DELETE)
export const deleteMyInfo = async () => {
  return authApiClient.axios.delete('/members/me');
};

// 프로필 이미지 업로드(POST)
export const postProfileImage = async ({ formData }: PostProfileImageContent) => {
  return authApiClient.axios.post('/members/me/profile-image', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
};

// AccessToken 유효성 판단(GET)
export const getIsValidAccessToken = async () => {
  return authApiClient.axios.get('/oauth/check');
};
