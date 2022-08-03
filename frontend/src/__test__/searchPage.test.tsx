import { renderWithQueryClient } from 'utils/testUtils';

import { screen } from '@testing-library/react';

import { LandingPage } from 'pages';

describe('테스트', () => {
  beforeEach(async () => {
    renderWithQueryClient(<LandingPage />);
  });

  test('앱이 제대로 렌더링되는지 확인한다.', async () => {
    expect(screen.getByText('구글로 시작하기')).toBeInTheDocument();
  });
});
