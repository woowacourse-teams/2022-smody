import { renderWithProviders } from '__test__/renderWithProviders';
import { authApiClient } from 'apis/apiClient';
import { accessTokenData } from 'mocks/data';

import { screen, waitFor } from '@testing-library/react';

import { CertPage } from 'pages';

describe('인증 페이지 테스트', () => {
  beforeEach(() => {
    authApiClient.updateAuth(accessTokenData);
    renderWithProviders(<CertPage />);
  });

  test('[useGetMyCyclesInProgress] API 응답 결과, "내가 진행중인 챌린지"가 렌더링되는지 확인한다.', async () => {
    const challengeName = await waitFor(
      () => screen.getAllByLabelText('진행중인 챌린지 이름')[1],
    );
    await waitFor(() => expect(challengeName).toHaveTextContent('운동'));
  });
});
