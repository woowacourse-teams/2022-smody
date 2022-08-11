import { PAGE_SIZE, queryKeys } from 'apis/constants';
import { getAllFeeds, getFeedById } from 'apis/feedApi/api';
import {
  GetAllFeedsResponse,
  GetFeedByIdProps,
  GetFeedByIdResponse,
} from 'apis/feedApi/type';
import { AxiosResponse, AxiosError } from 'axios';
import { ErrorResponse } from 'commonType';
import {
  useQuery,
  useInfiniteQuery,
  UseQueryOptions,
  UseInfiniteQueryOptions,
} from 'react-query';

// 1. 피드 전체 조회(GET)
export const useGetAllFeeds = (
  options?: UseInfiniteQueryOptions<
    AxiosResponse<GetAllFeedsResponse>,
    AxiosError<ErrorResponse>
  >,
) =>
  useInfiniteQuery<AxiosResponse<GetAllFeedsResponse>, AxiosError<ErrorResponse>>(
    queryKeys.getAllFeeds,
    ({ pageParam }) => getAllFeeds(pageParam),
    {
      ...options,
      getNextPageParam: (currentPage) => {
        const currentDataLength = currentPage.data.length;

        return currentDataLength < PAGE_SIZE.FEEDS
          ? undefined
          : currentPage.data[currentDataLength - 1].cycleDetailId;
      },
    },
  );

// 2. id로 피드 조회(GET)
export const useGetFeedById = (
  { cycleDetailId }: GetFeedByIdProps,
  options?: UseQueryOptions<
    AxiosResponse<GetFeedByIdResponse>,
    AxiosError<ErrorResponse>
  >,
) =>
  useQuery<AxiosResponse<GetFeedByIdResponse>, AxiosError<ErrorResponse>>(
    queryKeys.getFeedById,
    () => getFeedById({ cycleDetailId }),
    options,
  );
