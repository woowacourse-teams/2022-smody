import { generateQueryClient } from 'queryClient';
import React from 'react';
import { QueryClientProvider } from 'react-query';
import { RecoilRoot } from 'recoil';
import { ThemeProvider } from 'styled-components';

import { render, screen } from '@testing-library/react';

import { LandingPage } from 'pages';

import GlobalStyle from 'styles/GlobalStyle';
import { darkTheme } from 'styles/theme';

describe('테스트', () => {
  beforeEach(async () => {
    const snackBarElement = document.createElement('div');
    snackBarElement.id = 'snackbar';
    window.history.replaceState({}, '렌더링', '/home');

    render(
      <RecoilRoot>
        <QueryClientProvider client={generateQueryClient()}>
          <ThemeProvider theme={darkTheme}>
            <GlobalStyle />
            <LandingPage />
          </ThemeProvider>
        </QueryClientProvider>
      </RecoilRoot>,
    );
  });

  test('앱이 제대로 렌더링되는지 확인한다.', async () => {
    // Object.assign(location, { host: 'localhost:3000', pathname: '/home' });
    expect(screen.getByText('구글로 시작하기')).toBeInTheDocument();
  });
});
