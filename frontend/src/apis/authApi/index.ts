import { postSignUp, postLogin } from 'apis/authApi/api';
import {
  AuthProps,
  LoginProps,
  PostSignUpResponse,
  PostLoginResponse,
} from 'apis/authApi/type';
import { AxiosResponse, AxiosError } from 'axios';
import { useMutation, UseMutationOptions } from 'react-query';

export const usePostSignUp = (
  options?: UseMutationOptions<AxiosResponse<PostSignUpResponse>, AxiosError, AuthProps>,
) =>
  useMutation<AxiosResponse<PostSignUpResponse>, AxiosError, AuthProps>(
    postSignUp,
    options,
  );

export const usePostLogin = (
  options?: UseMutationOptions<AxiosResponse<PostLoginResponse>, AxiosError, LoginProps>,
) =>
  useMutation<AxiosResponse<PostLoginResponse>, AxiosError, LoginProps>(
    postLogin,
    options,
  );
