import { renderWithProviders } from '__test__/utils/renderWithProviders';
import { authApiClient } from 'apis/apiClient';
import { accessTokenData } from 'mocks/data';

import { screen, waitFor } from '@testing-library/react';

import { CLIENT_PATH } from 'constants/path';

describe('프로필 페이지 테스트', () => {
  beforeEach(() => {
    authApiClient.updateAuth(accessTokenData);
    renderWithProviders({ route: CLIENT_PATH.PROFILE });
  });

  test('[useGetMyInfo] query 응답에 따라 나의 프로필 정보가 렌더링되는지 확인한다.', async () => {
    const nickname = await waitFor(() => screen.getByLabelText('닉네임'));
    await waitFor(() => expect(nickname).toHaveTextContent('테스트닉네임'));

    const introduction = await waitFor(() => screen.getByLabelText('자기소개'));
    await waitFor(() => expect(introduction).toHaveTextContent('테스트 자기소개'));
  });

  test('[useGetMyCyclesStat] query 응답에 따라 나의 챌린지 통계가 렌더링되는지 확인한다.', async () => {
    const totalCount = await waitFor(() => screen.getByLabelText('시도한 챌린지 횟수'));
    await waitFor(() => expect(totalCount).toHaveTextContent('35'));

    const successCount = await waitFor(() => screen.getByLabelText('성공한 챌린지 횟수'));
    await waitFor(() => expect(successCount).toHaveTextContent('5'));
  });

  test('[useGetMySuccessChallenges] query 응답에 따라 성공한 챌린지 목록이 렌더링되는지 확인한다.', async () => {
    const successChallenge = await waitFor(
      () => screen.getAllByLabelText('성공한 챌린지 이름')[0],
    );
    await waitFor(() => expect(successChallenge).toHaveTextContent('미라클 모닝'));
  });
});
