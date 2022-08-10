import { PAGE_SIZE, queryKeys } from 'apis/constants';
import { getAllFeeds } from 'apis/feedApi/api';
import { GetAllFeedsResponse } from 'apis/feedApi/type';
import { AxiosResponse, AxiosError } from 'axios';
import { ErrorResponse } from 'commonType';
import { useInfiniteQuery, UseInfiniteQueryOptions } from 'react-query';

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
