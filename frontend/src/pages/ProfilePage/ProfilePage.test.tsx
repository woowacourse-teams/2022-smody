/**
 * @jest-environment jsdom
 */
// import { Profile } from '../../components/Profile';
import { ProfilePage } from '../../pages';
import { renderWithQueryClient } from '../../utils/testUtil';
import React from 'react';

import '@testing-library/jest-dom/extend-expect';
import { screen } from '@testing-library/react';

// beforeAll(() => {
//   localStorage.setItem('accessToken', 'test');
// });

test('renders response from query', async () => {
  localStorage.setItem('accessToken', 'test');

  renderWithQueryClient(<ProfilePage />);

  const userName = await screen.getByTestId('nickname');

  expect(userName).toBe('빅터짱짱맨');
});
