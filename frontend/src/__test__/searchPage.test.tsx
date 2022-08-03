import App from 'App';
import { MemoryRouter } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';
import { renderWithProviders } from 'utils/testUtils';

import { screen, waitFor } from '@testing-library/react';

import { SearchPage } from 'pages';

describe('검색 페이지 테스트', () => {
  test('[useGetAllChallenges] query 응답에 따라 모든 챌린지가 렌더링되는지 확인한다.', async () => {
    renderWithProviders(<SearchPage />);

    const challengeName = await waitFor(
      () => screen.getAllByLabelText('challenge-name')[0],
    );
    await waitFor(() => expect(challengeName).toHaveTextContent('미라클 모닝'));
  });
});
