import { postSignUpProps, postSignUpResponse } from 'apis/authApi/type';
import axios from 'axios';

export const postSignUp = async ({ email, password, nickname }: postSignUpProps) => {
  return axios.post<postSignUpResponse>('/members', {
    email,
    password,
    nickname,
  });
};
