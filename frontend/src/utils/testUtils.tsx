// test-utils/index.tsx

/* eslint-disable no-console */
import { generateQueryClient } from 'queryClient';
import { ReactElement } from 'react';
import { QueryClient, QueryClientProvider, setLogger } from 'react-query';
import { RecoilRoot } from 'recoil';
import { ThemeProvider } from 'styled-components';

import { render, RenderResult } from '@testing-library/react';

import GlobalStyle from 'styles/GlobalStyle';
import { darkTheme } from 'styles/theme';

// suppress errors written to console
setLogger({
  log: console.log,
  warn: console.warn,
  error: () => {
    // swallow the errors
  },
});

const generateTestQueryClient = () => {
  const client = generateQueryClient();
  const options = client.getDefaultOptions();
  options.queries = { ...options.queries, retry: false };
  return client;
};

export function renderWithQueryClient(
  ui: ReactElement,
  client?: QueryClient,
): RenderResult {
  const queryClient = client ?? generateTestQueryClient();
  return render(
    <RecoilRoot>
      <QueryClientProvider client={queryClient}>
        <ThemeProvider theme={darkTheme}>{ui}</ThemeProvider>
      </QueryClientProvider>
    </RecoilRoot>,
  );
}
