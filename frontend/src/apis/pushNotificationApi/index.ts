import {
  deleteAllNotification,
  deleteNotification,
  getNotifications,
  getVapidPublicKey,
  postSubscribe,
  postUnsubscribe,
} from './api';
import {
  DeleteNotificationParams,
  GetNotificationsResponse,
  GetVapidPublicKeyResponse,
  PostUnsubscribePayload,
} from './type';
import { mutationKeys, queryKeys } from 'apis/constants';
import { AxiosError, AxiosResponse } from 'axios';
import { useMutation, UseMutationOptions, useQuery, UseQueryOptions } from 'react-query';
import { ErrorResponse } from 'types/internal';

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
    PostUnsubscribePayload
  >,
) => {
  return useMutation<AxiosResponse, AxiosError<ErrorResponse>, PostUnsubscribePayload>(
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
    DeleteNotificationParams
  >,
) =>
  useMutation<AxiosResponse, AxiosError<ErrorResponse>, DeleteNotificationParams>(
    mutationKeys.deleteNotification,
    deleteNotification,
    options,
  );

export const useDeleteAllNotification = (
  options?: UseMutationOptions<AxiosResponse, AxiosError<ErrorResponse>>,
) =>
  useMutation<AxiosResponse, AxiosError<ErrorResponse>>(deleteAllNotification, options);
