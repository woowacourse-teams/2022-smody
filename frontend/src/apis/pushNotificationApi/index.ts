import { getVapidPublicKey, postSubscribe, postUnsubscribe } from './api';
import { GetVapidPublicKeyResponse } from './type';
import { AxiosError, AxiosResponse } from 'axios';
import { ErrorResponse } from 'commonType';
import { useMutation, UseMutationOptions, useQuery, UseQueryOptions } from 'react-query';

export const useGetVapidPublicKey = (
  options?: UseQueryOptions<
    AxiosResponse<GetVapidPublicKeyResponse>,
    AxiosError<ErrorResponse>
  >,
) =>
  useQuery<AxiosResponse<GetVapidPublicKeyResponse>, AxiosError<ErrorResponse>>(
    'getVapidPublicKey',
    getVapidPublicKey,
    {
      ...options,
      enabled: false,
    },
  );

export const usePostSubscribe = (
  options?: UseMutationOptions<
    AxiosResponse,
    AxiosError<ErrorResponse>,
    PushSubscription
  >,
) => {
  return useMutation<AxiosResponse, AxiosError<ErrorResponse>, PushSubscription>(
    'postSubscribe',
    postSubscribe,
    options,
  );
};

export const usePostUnsubscribe = (
  options?: UseMutationOptions<AxiosResponse, AxiosError<ErrorResponse>, string>,
) => {
  return useMutation<AxiosResponse, AxiosError<ErrorResponse>, string>(
    'postSubscribe',
    postUnsubscribe,
    options,
  );
};
