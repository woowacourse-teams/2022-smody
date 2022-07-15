import { apiClient } from 'apis/apiClient';
import {
  AuthProps,
  PostSignUpResponse,
  LoginProps,
  PostLoginResponse,
} from 'apis/authApi/type';

export const postSignUp = async ({ email, password, nickname }: AuthProps) => {
  return apiClient.axios.post<PostSignUpResponse>('/members', {
    email,
    password,
    nickname,
  });
};

export const postLogin = async () => {
  console.log('여기까지는 되나?');
  return apiClient.axios.get<string>('/oauth/link/google');
};
