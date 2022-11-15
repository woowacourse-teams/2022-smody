import { renderWithProviders } from '__test__/utils/renderWithProviders';
import { authApiClient } from 'apis/apiClient';
import { accessTokenData } from 'mocks/data';
import { feedData } from 'mocks/data';

import { screen, waitFor } from '@testing-library/react';

describe('피드 상세 페이지 테스트', () => {
  const mockedFeedData = feedData[0];

  beforeEach(() => {
    const route = `/feed/detail/${mockedFeedData.cycleDetailId}`;

    authApiClient.updateAuth(accessTokenData);
    renderWithProviders({ route });
  });

  test('[useGetFeedById] 조회한 피드의 description이 mocking된 피드 데이터의 description과 일치한다.', async () => {
    const DESCRIPTION_LABEL = '피드 내용';
    const EXPECTED_DESCRIPTION = mockedFeedData.description;

    const descriptionElement = await waitFor(() =>
      screen.getByLabelText(DESCRIPTION_LABEL),
    );

    await waitFor(() =>
      expect(descriptionElement).toHaveTextContent(EXPECTED_DESCRIPTION),
    );
  });
});
