import Router from 'Router';
import { QueryClient, QueryClientProvider } from 'react-query';
import { ReactQueryDevtools } from 'react-query/devtools';
import { useRecoilValue } from 'recoil';
import { isDarkState } from 'recoil/darkMode/atoms';
import { ThemeProvider } from 'styled-components';

import GlobalStyle from 'styles/GlobalStyle';
import { darkTheme, lightTheme } from 'styles/theme';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 0,
      suspense: true,
      useErrorBoundary: true,
    },
    mutations: {
      useErrorBoundary: true,
    },
  },
});

const App = () => {
  const isDark = useRecoilValue(isDarkState);

  return (
    <QueryClientProvider client={queryClient}>
      <ThemeProvider theme={isDark ? darkTheme : lightTheme}>
        <GlobalStyle />
        <Router />
        <ReactQueryDevtools initialIsOpen={true} />
      </ThemeProvider>
    </QueryClientProvider>
  );
};

export default App;
