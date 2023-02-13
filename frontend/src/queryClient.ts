import { QueryClient } from 'react-query';

export const generateQueryClient = () =>
  new QueryClient({
    defaultOptions: {
      queries: {
        retry: 0,
        suspense: true,
        useErrorBoundary: true,
        refetchOnWindowFocus: false,
      },
      mutations: {
        useErrorBoundary: true,
      },
    },
  });
