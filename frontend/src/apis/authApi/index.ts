import { postSignUp, postLogin } from 'apis/authApi/api';
import { AuthProps, PostSignUpResponse } from 'apis/authApi/type';
import { AxiosResponse, AxiosError } from 'axios';
import { ErrorResponse } from 'commonType';
import { useMutation, UseMutationOptions } from 'react-query';

export const usePostSignUp = (
  options?: UseMutationOptions<
    AxiosResponse<PostSignUpResponse>,
    AxiosError<ErrorResponse>,
    AuthProps
  >,
) =>
  useMutation<AxiosResponse<PostSignUpResponse>, AxiosError<ErrorResponse>, AuthProps>(
    postSignUp,
    options,
  );

export const usePostLogin = (
  options?: UseMutationOptions<AxiosResponse<string>, AxiosError<ErrorResponse>>,
) => useMutation<AxiosResponse<string>, AxiosError<ErrorResponse>>(postLogin, options);
