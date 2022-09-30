import { ReactNode } from 'react';

export type InfiniteScrollProps = {
  loadMore: () => void;
  hasMore?: boolean;
  isFetching?: boolean;
  loader?: ReactNode;
  threshold?: number;
};
