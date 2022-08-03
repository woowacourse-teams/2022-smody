import { authApiClient } from 'apis/apiClient';
import { renderWithProviders } from 'utils/testUtils';

import { screen, waitFor } from '@testing-library/react';

import { ProfilePage } from 'pages';

describe('프로필 페이지 테스트', () => {
  beforeEach(() => {
    authApiClient.updateAuth('test');
    renderWithProviders(<ProfilePage />);
  });

  test('[useGetMyInfo] query 응답에 따라 나의 프로필 정보가 렌더링되는지 확인한다.', async () => {
    const nickname = await waitFor(() => screen.getByLabelText('닉네임'));
    await waitFor(() => expect(nickname).toHaveTextContent('테스트 닉네임'));

    const introduction = await waitFor(() => screen.getByLabelText('자기소개'));
    await waitFor(() => expect(introduction).toHaveTextContent('테스트 자기소개'));
  });

  test('[useGetMyCyclesStat] query 응답에 따라 나의 챌린지 통계가 렌더링되는지 확인한다.', async () => {
    const totalCount = await waitFor(() => screen.getByLabelText('시도한 챌린지 횟수'));
    await waitFor(() => expect(totalCount).toHaveTextContent('35'));

    const successCount = await waitFor(() => screen.getByLabelText('성공한 챌린지 횟수'));
    await waitFor(() => expect(successCount).toHaveTextContent('5'));
  });
});
