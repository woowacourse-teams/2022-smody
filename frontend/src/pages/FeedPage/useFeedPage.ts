import { useGetAllFeeds } from 'apis/feedApi';
import { indexedDB, saveDataToCache } from 'pwa/indexedDB';
import { useEffect, useState } from 'react';

import { INDEXED_DB } from 'constants/storage';

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
      saveDataToCache(INDEXED_DB.FEED, data.pages.length, feeds);
    },
  });

  const [savedFeeds, setSavedFeeds] = useState([]);

  useEffect(() => {
    if (!isError) {
      return;
    }
    indexedDB.getPosts(INDEXED_DB.FEED).then((feeds) => {
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
