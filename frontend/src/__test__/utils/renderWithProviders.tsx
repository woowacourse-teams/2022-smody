import Router from 'Router';
import { renderWithProvidersProps } from '__test__/utils/type';
import { generateQueryClient } from 'queryClient';
import { QueryClientProvider } from 'react-query';
import { MemoryRouter } from 'react-router-dom';
import { RecoilRoot } from 'recoil';
import { ThemeProvider } from 'styled-components';

import { render, RenderResult } from '@testing-library/react';

import { darkTheme } from 'styles/theme';

const generateTestQueryClient = () => {
  const client = generateQueryClient();
  const options = client.getDefaultOptions();
  options.queries = { ...options.queries, retry: false };
  return client;
};

export function renderWithProviders({ route }: renderWithProvidersProps): RenderResult {
  return render(
    <RecoilRoot>
      <QueryClientProvider client={generateTestQueryClient()}>
        <ThemeProvider theme={darkTheme}>
          <MemoryRouter initialEntries={[route]}>
            <Router />
          </MemoryRouter>
        </ThemeProvider>
      </QueryClientProvider>
    </RecoilRoot>,
  );
}
