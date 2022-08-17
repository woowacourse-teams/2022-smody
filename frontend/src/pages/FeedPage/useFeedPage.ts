import { useGetAllFeeds } from 'apis/feedApi';

export const useFeedPage = () => {
  const {
    data: feedInfiniteData,
    isFetching,
    hasNextPage,
    fetchNextPage,
  } = useGetAllFeeds();

  return {
    feedInfiniteData,
    isFetching,
    hasNextPage,
    fetchNextPage,
  };
};
