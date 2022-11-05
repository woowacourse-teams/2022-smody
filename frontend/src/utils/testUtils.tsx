import { generateQueryClient } from 'queryClient';
import { ReactElement } from 'react';
import { QueryClient, QueryClientProvider, setLogger } from 'react-query';
import { MemoryRouter } from 'react-router-dom';
import { RecoilRoot } from 'recoil';
import { ThemeProvider } from 'styled-components';

import { render, RenderResult } from '@testing-library/react';

import { darkTheme } from 'styles/theme';

// setLogger({
//   log: console.log,
//   warn: console.warn,
//   error: () => {
//     // swallow the errors
//   },
// });

const generateTestQueryClient = () => {
  const client = generateQueryClient();
  const options = client.getDefaultOptions();
  options.queries = { ...options.queries, retry: false };
  return client;
};

export function renderWithProviders(
  ui: ReactElement,
  client?: QueryClient,
): RenderResult {
  const queryClient = client ?? generateTestQueryClient();
  return render(
    <RecoilRoot>
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <ThemeProvider theme={darkTheme}>{ui}</ThemeProvider>
        </MemoryRouter>
      </QueryClientProvider>
    </RecoilRoot>,
  );
}
