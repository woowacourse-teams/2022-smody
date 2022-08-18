import { mutationKeys, queryKeys } from 'apis/constants';
import {
  getLinkGoogle,
  getMyInfo,
  getTokenGoogle,
  patchMyInfo,
  deleteMyInfo,
  postProfileImage,
  getIsValidAccessToken,
} from 'apis/oAuthApi/api';
import {
  GetMyInfoResponse,
  GetTokenGoogleResponse,
  PatchMyInfoProps,
  PostProfileImageProps,
  GetIsValidAccessTokenResponse,
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
    { ...options, enabled: false, retry: false, suspense: false },
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
    { ...options, enabled: false, suspense: false },
  );

export const useGetMyInfo = (
  options?: UseQueryOptions<AxiosResponse<GetMyInfoResponse>, AxiosError<ErrorResponse>>,
) =>
  useQuery<AxiosResponse<GetMyInfoResponse>, AxiosError<ErrorResponse>>(
    queryKeys.getMyInfo,
    getMyInfo,
    {
      ...options,
      refetchOnMount: false,
      refetchOnReconnect: false,
      suspense: false,
      onError: () => {
        // error swallow
        return null;
      },
    },
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
  options?: UseMutationOptions<
    AxiosResponse,
    AxiosError<ErrorResponse>,
    PostProfileImageProps
  >,
) => {
  return useMutation<AxiosResponse, AxiosError<ErrorResponse>, PostProfileImageProps>(
    mutationKeys.postProfileImage,
    postProfileImage,
    options,
  );
};

export const useGetIsValidAccessToken = (
  options?: UseQueryOptions<
    AxiosResponse<GetIsValidAccessTokenResponse>,
    AxiosError<ErrorResponse>
  >,
) => {
  return useQuery<
    AxiosResponse<GetIsValidAccessTokenResponse>,
    AxiosError<ErrorResponse>
  >(queryKeys.getIsValidAccessToken, getIsValidAccessToken, options);
};
