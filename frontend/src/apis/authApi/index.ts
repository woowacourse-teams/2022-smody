import { postSignUp } from 'apis/authApi/api';
import { postSignUpProps, postSignUpResponse } from 'apis/authApi/type';
import { AxiosResponse, AxiosError } from 'axios';
import { useMutation, UseMutationOptions } from 'react-query';

export const usePostSignUp = (
  options?: UseMutationOptions<
    AxiosResponse<postSignUpResponse>,
    AxiosError,
    postSignUpProps
  >,
) =>
  useMutation<AxiosResponse<postSignUpResponse>, AxiosError, postSignUpProps>(
    postSignUp,
    options,
  );
