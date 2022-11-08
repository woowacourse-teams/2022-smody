import { generateQueryClient } from 'queryClient';
import { ReactElement } from 'react';
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

export const renderWithProviders = (Component: ReactElement): RenderResult => {
  return render(
    <RecoilRoot>
      <QueryClientProvider client={generateTestQueryClient()}>
        <ThemeProvider theme={darkTheme}>
          <MemoryRouter>{Component}</MemoryRouter>
        </ThemeProvider>
      </QueryClientProvider>
    </RecoilRoot>,
  );
};
