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
      indexedDB.clearPost('feed').then(() => {
        for (const feed of feeds) {
          indexedDB.savePost('feed', feed);
        }
      });
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
