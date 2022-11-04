import { renderWithProviders } from 'utils/testUtils';

import { screen, waitFor } from '@testing-library/react';

import { CertPage } from 'pages';

describe('인증 페이지 테스트', () => {
  beforeEach(() => {
    renderWithProviders(<CertPage />);
  });

  test('[useGetMyCyclesInProgress] query 응답에 따라 내가 진행중인 챌린지가 렌더링되는지 확인한다.', () => {
    jest.isolateModules(async () => {
      const challengeName = await waitFor(
        () => screen.getAllByLabelText('진행중인 챌린지 이름')[1],
      );
      await waitFor(() => expect(challengeName).toHaveTextContent('운동'));
    });
  });
});
