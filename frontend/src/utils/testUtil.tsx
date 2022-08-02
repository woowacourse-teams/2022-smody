import { generateQueryClient } from 'queryClient';
import React, { ReactElement, ReactNode } from 'react';
import { QueryClient, QueryClientProvider, setLogger } from 'react-query';

import { render, RenderResult } from '@testing-library/react';

// 콘솔에 적히는 에러 억제
setLogger({
  log: console.log,
  warn: console.warn,
  error: () => {
    // 에러를 삼킨다
  },
});

// 테스트마다 고유의 QueryClient를 생성하여 전달한다
const generateTestQueryClient = () => {
  const client = generateQueryClient();
  const options = client.getDefaultOptions();
  options.queries = { ...options.queries, retry: false };
  return client;
};

export const renderWithQueryClient = (
  ui: ReactElement,
  client?: QueryClient,
): RenderResult => {
  const queryClient = client ?? generateTestQueryClient();

  return render(<QueryClientProvider client={queryClient}>{ui}</QueryClientProvider>);
};

export const createQueryClientWrapper = () => {
  const queryClient = generateTestQueryClient();
  const getQueryClientWrapper = ({ children }: { children: ReactNode }) => (
    <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  );

  return getQueryClientWrapper;
};
