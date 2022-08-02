import { QueryClient } from 'react-query';

export const generateQueryClient = (
  queryErrorHandler: (error: any) => void,
): QueryClient => {
  return new QueryClient({
    defaultOptions: {
      queries: {
        retry: 0,
        onError: queryErrorHandler,
        suspense: true,
      },
      mutations: {
        onError: queryErrorHandler,
      },
    },
  });
};
