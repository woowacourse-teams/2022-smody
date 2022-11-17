import { renderWithProviders } from '__test__/utils/renderWithProviders';
import { authApiClient } from 'apis/apiClient';
import { accessTokenData } from 'mocks/data';
import { feedData } from 'mocks/data';
import { act } from 'react-dom/test-utils';

import { screen, waitFor } from '@testing-library/react';

describe('피드 상세 페이지 테스트', () => {
  const mockedFeedData = feedData[0];
  beforeEach(async () => {
    const route = `/feed/detail/${mockedFeedData.cycleDetailId}`;
    authApiClient.updateAuth(accessTokenData);

    await act(() => {
      renderWithProviders({ route });
    });
  });

  test('[useGetFeedById] 조회한 피드의 description이 mocking된 피드 데이터의 description과 일치한다.', async () => {
    // given
    const DESCRIPTION_LABEL = '피드 내용';
    const expectedDescription = mockedFeedData.description;

    // when
    const descriptionElement = await waitFor(() =>
      screen.getByLabelText(DESCRIPTION_LABEL),
    );

    // then
    await waitFor(() =>
      expect(descriptionElement).toHaveTextContent(expectedDescription),
    );
  });
});
