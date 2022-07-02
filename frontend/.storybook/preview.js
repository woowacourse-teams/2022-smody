import { BrowserRouter } from 'react-router-dom';
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

export const decorators = [
  (Story) => (
    <ThemeProvider theme={lightTheme}>
      <GlobalStyle />
      <BrowserRouter>
        <Story />
      </BrowserRouter>
    </ThemeProvider>
  ),
];
