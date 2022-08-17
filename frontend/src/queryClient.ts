import { QueryClient } from 'react-query';

export const generateQueryClient = (
  queryErrorHandler?: (error: any) => void,
): QueryClient => {
  return new QueryClient({
    defaultOptions: {
      queries: {
        retry: 0,
        refetchOnWindowFocus: false,
        onError: queryErrorHandler,
        suspense: true,
      },
      mutations: {
        onError: queryErrorHandler,
      },
    },
  });
};
