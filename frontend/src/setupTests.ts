import MockIntersectionObserver from 'mocks/MockIntersectionObserver';
import 'mocks/matchMedia';
import { server } from 'mocks/server';

import '@testing-library/jest-dom';
import { cleanup } from '@testing-library/react';

beforeAll(() => {
  server.listen();
  window.IntersectionObserver = MockIntersectionObserver;
});

afterAll(() => {
  server.close();
  jest.resetAllMocks();
});

afterEach(() => {
  cleanup();
  server.resetHandlers();
});

beforeEach(() => {
  // @ts-ignore
  globalThis.IS_REACT_ACT_ENVIRONMENT = true;
});
