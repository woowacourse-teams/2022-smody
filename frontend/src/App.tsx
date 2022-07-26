import Router from 'Router';
import { generateQueryClient } from 'queryClient';
import { QueryClientProvider } from 'react-query';
import { ReactQueryDevtools } from 'react-query/devtools';
import { ThemeProvider } from 'styled-components';

import useQueryErrorHandler from 'hooks/useQueryErrorHandler';

import GlobalStyle from 'styles/GlobalStyle';
import { lightTheme } from 'styles/theme';

const App = () => {
  const queryErrorHandler = useQueryErrorHandler();

  return (
    <QueryClientProvider client={generateQueryClient(queryErrorHandler)}>
      <ThemeProvider theme={lightTheme}>
        <GlobalStyle />
        <Router />
        <ReactQueryDevtools initialIsOpen={true} />
      </ThemeProvider>
    </QueryClientProvider>
  );
};

export default App;
