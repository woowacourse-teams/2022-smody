import {
  deleteNotification,
  getNotifications,
  getVapidPublicKey,
  postSubscribe,
  postUnsubscribe,
} from './api';
import {
  DeleteNotificationProps,
  GetNotificationsResponse,
  GetVapidPublicKeyResponse,
  PostUnsubscribeProps,
} from './type';
import { mutationKeys, queryKeys } from 'apis/constants';
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
    queryKeys.getVapidPublicKey,
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
    mutationKeys.postSubscribe,
    postSubscribe,
    options,
  );
};

export const usePostUnsubscribe = (
  options?: UseMutationOptions<
    AxiosResponse,
    AxiosError<ErrorResponse>,
    PostUnsubscribeProps
  >,
) => {
  return useMutation<AxiosResponse, AxiosError<ErrorResponse>, PostUnsubscribeProps>(
    mutationKeys.postUnsubscribe,
    postUnsubscribe,
    options,
  );
};

export const useGetNotifications = (
  options?: UseQueryOptions<
    AxiosResponse<GetNotificationsResponse>,
    AxiosError<ErrorResponse>
  >,
) => {
  return useQuery<AxiosResponse<GetNotificationsResponse>, AxiosError<ErrorResponse>>(
    queryKeys.getNotifications,
    getNotifications,
    { ...options, suspense: false },
  );
};

export const useDeleteNotification = (
  options?: UseMutationOptions<
    AxiosResponse,
    AxiosError<ErrorResponse>,
    DeleteNotificationProps
  >,
) =>
  useMutation<AxiosResponse, AxiosError<ErrorResponse>, DeleteNotificationProps>(
    mutationKeys.deleteNotification,
    deleteNotification,
    options,
  );
