import { getLinkGoogle, getMyInfo, getTokenGoogle } from 'apis/oAuthApi/api';
import { GetMyInfoResponse, GetTokenGoogleResponse } from 'apis/oAuthApi/type';
import { AxiosResponse, AxiosError } from 'axios';
import { useQuery, UseQueryOptions } from 'react-query';

export const useGetLinkGoogle = (
  options?: UseQueryOptions<AxiosResponse<string>, AxiosError>,
) => useQuery<AxiosResponse<string>, AxiosError>('getLinkGoogle', getLinkGoogle, options);

export const useGetTokenGoogle = (
  code: string,
  options?: UseQueryOptions<AxiosResponse<GetTokenGoogleResponse>, AxiosError>,
) =>
  useQuery<AxiosResponse<GetTokenGoogleResponse>, AxiosError>(
    'getTokenGoogle',
    () => getTokenGoogle(code),
    options,
  );

export const useGetMyInfo = (
  options?: UseQueryOptions<AxiosResponse<GetMyInfoResponse>, AxiosError>,
) =>
  useQuery<AxiosResponse<GetMyInfoResponse>, AxiosError>('getMyInfo', getMyInfo, options);
