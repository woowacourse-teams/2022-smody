import { mutationKeys, queryKeys } from 'apis/constants';
import {
  getLinkGoogle,
  getMyInfo,
  getTokenGoogle,
  patchMyInfo,
  deleteMyInfo,
  postProfileImage,
} from 'apis/oAuthApi/api';
import {
  GetMyInfoResponse,
  GetTokenGoogleResponse,
  PatchMyInfoProps,
} from 'apis/oAuthApi/type';
import { AxiosResponse, AxiosError } from 'axios';
import { ErrorResponse } from 'commonType';
import { useMutation, useQuery, UseQueryOptions, UseMutationOptions } from 'react-query';

export const useGetLinkGoogle = (
  options?: UseQueryOptions<AxiosResponse<string>, AxiosError<ErrorResponse>>,
) =>
  useQuery<AxiosResponse<string>, AxiosError<ErrorResponse>>(
    queryKeys.getLinkGoogle,
    getLinkGoogle,
    {
      ...options,
      enabled: false,
      onSuccess: ({ data: redirectionUrl }) => {
        window.location.href = redirectionUrl;
      },
    },
  );

export const useGetTokenGoogle = (
  code: string,
  options?: UseQueryOptions<
    AxiosResponse<GetTokenGoogleResponse>,
    AxiosError<ErrorResponse>
  >,
) =>
  useQuery<AxiosResponse<GetTokenGoogleResponse>, AxiosError<ErrorResponse>>(
    queryKeys.getTokenGoogle,
    () => getTokenGoogle(code),
    options,
  );

export const useGetMyInfo = (
  options?: UseQueryOptions<AxiosResponse<GetMyInfoResponse>, AxiosError<ErrorResponse>>,
) =>
  useQuery<AxiosResponse<GetMyInfoResponse>, AxiosError<ErrorResponse>>(
    queryKeys.getMyInfo,
    getMyInfo,
    options,
  );

export const usePatchMyInfo = (
  options?: UseMutationOptions<
    AxiosResponse,
    AxiosError<ErrorResponse>,
    PatchMyInfoProps
  >,
) =>
  useMutation<AxiosResponse, AxiosError<ErrorResponse>, PatchMyInfoProps>(
    mutationKeys.patchMyInfo,
    patchMyInfo,
    options,
  );

export const useDeleteMyInfo = (
  options?: UseMutationOptions<AxiosResponse, AxiosError<ErrorResponse>>,
) =>
  useMutation<AxiosResponse, AxiosError<ErrorResponse>>(
    mutationKeys.deleteMyInfo,
    deleteMyInfo,
    options,
  );

export const usePostProfileImage = (
  options?: UseMutationOptions<AxiosResponse, AxiosError<ErrorResponse>, FormData>,
) => {
  return useMutation(mutationKeys.postProfileImage, postProfileImage, options);
};
