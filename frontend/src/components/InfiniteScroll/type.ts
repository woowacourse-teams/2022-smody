import { ReactNode } from 'react';

export interface InfiniteScrollProps {
  children: ReactNode;
  loadMore: () => void;
  hasMore?: boolean;
  isFetching: boolean;
  loader?: ReactNode;
  threshold?: number;
}
