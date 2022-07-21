import { getLinkGoogle, getMyInfo, getTokenGoogle } from 'apis/oAuthApi/api';
import { GetMyInfoResponse, GetTokenGoogleResponse } from 'apis/oAuthApi/type';
import { AxiosResponse, AxiosError } from 'axios';
import { ErrorResponse } from 'commonType';
import { useQuery, UseQueryOptions } from 'react-query';

export const useGetLinkGoogle = (
  options?: UseQueryOptions<AxiosResponse<string>, AxiosError<ErrorResponse>>,
) =>
  useQuery<AxiosResponse<string>, AxiosError<ErrorResponse>>(
    'getLinkGoogle',
    getLinkGoogle,
    options,
  );

export const useGetTokenGoogle = (
  code: string,
  options?: UseQueryOptions<
    AxiosResponse<GetTokenGoogleResponse>,
    AxiosError<ErrorResponse>
  >,
) =>
  useQuery<AxiosResponse<GetTokenGoogleResponse>, AxiosError<ErrorResponse>>(
    'getTokenGoogle',
    () => getTokenGoogle(code),
    options,
  );

export const useGetMyInfo = (
  options?: UseQueryOptions<AxiosResponse<GetMyInfoResponse>, AxiosError<ErrorResponse>>,
) =>
  useQuery<AxiosResponse<GetMyInfoResponse>, AxiosError<ErrorResponse>>(
    'getMyInfo',
    getMyInfo,
    options,
  );
