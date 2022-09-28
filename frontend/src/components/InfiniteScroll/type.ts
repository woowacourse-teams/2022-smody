import { ReactNode } from 'react';

export interface InfiniteScrollProps {
  loadMore: () => void;
  hasMore?: boolean;
  isFetching: boolean;
  loader?: ReactNode;
  threshold?: number;
}
