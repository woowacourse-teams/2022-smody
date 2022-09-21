import { useGetAllFeeds } from 'apis/feedApi';
import { indexedDB } from 'pwa/indexedDB';
import { useEffect, useState } from 'react';

export const useFeedPage = () => {
  const {
    data: feedInfiniteData,
    isFetching,
    hasNextPage,
    fetchNextPage,
    isError,
  } = useGetAllFeeds({
    useErrorBoundary: false,
    onSuccess: (data) => {
      const feeds = data.pages[0].data;
      indexedDB.putPost('feed', feeds);
    },
  });

  const [savedFeeds, setSavedFeeds] = useState([]);

  useEffect(() => {
    if (!isError) {
      return;
    }
    indexedDB.getPosts('feed').then((feeds) => {
      setSavedFeeds(feeds);
    });
  }, [isError]);

  return {
    feedInfiniteData,
    isFetching,
    hasNextPage,
    fetchNextPage,
    isError,
    savedFeeds,
  };
};
