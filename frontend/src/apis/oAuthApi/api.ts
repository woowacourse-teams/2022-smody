import { apiClient, authApiClient } from 'apis/apiClient';
import { GetMyInfoResponse, GetTokenGoogleResponse } from 'apis/oAuthApi/type';

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

// 내 정보 조회(GET)
export const getMyInfo = async () => {
  return authApiClient.axios.get<GetMyInfoResponse>('/members/me');
};
