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
      indexedDB.clearFeed().then(() => {
        for (const feed of feeds) {
          indexedDB.saveFeed(feed);
        }
      });
    },
  });

  const [feeds, setFeeds] = useState([]);

  useEffect(() => {
    if (!isError) {
      return;
    }
    indexedDB.getFeeds().then((feeds) => {
      setFeeds(feeds);
    });
  }, [isError]);

  return {
    feedInfiniteData,
    isFetching,
    hasNextPage,
    fetchNextPage,
    isError,
    feeds,
  };
};
