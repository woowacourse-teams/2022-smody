import Router from 'Router';
import { generateQueryClient } from 'queryClient';
import { QueryClientProvider } from 'react-query';
import { ReactQueryDevtools } from 'react-query/devtools';
import { RecoilRoot } from 'recoil';
import { ThemeProvider } from 'styled-components';

import useLogout from 'hooks/auth/useLogout';

import GlobalStyle from 'styles/GlobalStyle';
import { lightTheme } from 'styles/theme';

const App = () => {
  const logoutByError = useLogout();
  const queryErrorHandler = (error: any): void => {
    logoutByError(error);
  };

  return (
    <RecoilRoot>
      <QueryClientProvider client={generateQueryClient(queryErrorHandler)}>
        <ThemeProvider theme={lightTheme}>
          <GlobalStyle />
          <Router />
          <ReactQueryDevtools initialIsOpen={true} />
        </ThemeProvider>
      </QueryClientProvider>
    </RecoilRoot>
  );
};

export default App;
