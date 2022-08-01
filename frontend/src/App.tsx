import Router from 'Router';
import { generateQueryClient } from 'queryClient';
import { QueryClientProvider } from 'react-query';
import { ReactQueryDevtools } from 'react-query/devtools';
import { useRecoilValue } from 'recoil';
import { isDarkState } from 'recoil/darkMode/atoms';
import { ThemeProvider } from 'styled-components';

import useQueryErrorHandler from 'hooks/useQueryErrorHandler';

import GlobalStyle from 'styles/GlobalStyle';
import { darkTheme, lightTheme } from 'styles/theme';

const App = () => {
  const isDark = useRecoilValue(isDarkState);
  const queryErrorHandler = useQueryErrorHandler();

  return (
    <QueryClientProvider client={generateQueryClient(queryErrorHandler)}>
      <ThemeProvider theme={isDark ? darkTheme : lightTheme}>
        <GlobalStyle />
        <Router />
        <ReactQueryDevtools initialIsOpen={true} />
      </ThemeProvider>
    </QueryClientProvider>
  );
};

export default App;
