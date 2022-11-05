import MockIntersectionObserver from '__test__/MockIntersectionObserver';
import { server } from 'mocks/server';

import '@testing-library/jest-dom';
import { cleanup } from '@testing-library/react';

Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: jest.fn().mockImplementation((query) => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: jest.fn(), // Deprecated
    removeListener: jest.fn(), // Deprecated
    addEventListener: jest.fn(),
    removeEventListener: jest.fn(),
    dispatchEvent: jest.fn(),
  })),
});

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

jest.mock('react-router-dom', () => {
  return {
    __esModule: true,
    ...jest.requireActual('react-router-dom'),
  };
});

window.HTMLElement.prototype.scrollIntoView = jest.fn();
