import MockIntersectionObserver from 'mocks/MockIntersectionObserver';
import 'mocks/matchMedia';
import { server } from 'mocks/server';

import '@testing-library/jest-dom';

beforeAll(() => {
  server.listen();
  window.IntersectionObserver = MockIntersectionObserver;
});

afterAll(() => {
  server.close();
  jest.resetAllMocks();
});

afterEach(() => {
  server.resetHandlers();
});
