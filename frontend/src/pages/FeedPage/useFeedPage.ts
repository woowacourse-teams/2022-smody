import { useGetAllFeeds } from 'apis/feedApi';

export const useFeedPage = () => {
  const {
    data: feedInfiniteData,
    isFetching,
    hasNextPage,
    fetchNextPage,
  } = useGetAllFeeds({
    refetchOnWindowFocus: false,
  });

  return {
    feedInfiniteData,
    isFetching,
    hasNextPage,
    fetchNextPage,
  };
};
