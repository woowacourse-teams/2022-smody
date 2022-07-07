import { QueryClient, QueryClientProvider } from 'react-query';
import { BrowserRouter } from 'react-router-dom';
import { RecoilRoot } from 'recoil';
import { ThemeProvider } from 'styled-components';

import GlobalStyle from 'styles/GlobalStyle';
import { lightTheme } from 'styles/theme';

export const parameters = {
  actions: { argTypesRegex: '^on[A-Z].*' },
  controls: {
    matchers: {
      color: /(background|color)$/i,
      date: /Date$/,
    },
  },
};

export const argTypes = {
  ref: {
    table: {
      disable: true,
    },
  },
  theme: {
    table: {
      disable: true,
    },
  },
  as: {
    table: {
      disable: true,
    },
  },
  forwardedAs: {
    table: {
      disable: true,
    },
  },
};

const queryClient = new QueryClient();

export const decorators = [
  (Story) => (
    <RecoilRoot>
      <QueryClientProvider client={queryClient}>
        <ThemeProvider theme={lightTheme}>
          <GlobalStyle />
          <BrowserRouter>
            <Story />
          </BrowserRouter>
        </ThemeProvider>
      </QueryClientProvider>
    </RecoilRoot>
  ),
];
