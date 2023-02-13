import type { Config } from '@jest/types';

const config: Config.InitialOptions = {
  moduleFileExtensions: ['js', 'json', 'jsx', 'ts', 'tsx', 'json'],
  transform: {
    '^.+\\.(js|jsx|ts|tsx)?$': 'ts-jest',
    '^.+\\.svg$': 'jest-transformer-svg',
  },
  testEnvironment: 'jsdom',
  moduleDirectories: ['node_modules', 'src'],
  moduleNameMapper: {
    '\\.(jpg|ico|jpeg|png|gif|webp)$': '<rootDir>/mocks/fileMock.js',
  },
  rootDir: './src',
  setupFilesAfterEnv: ['<rootDir>/setupTests.ts'],
  setupFiles: ['fake-indexeddb/auto'],
  reporters: ['default', 'jest-junit'],
};

export default config;
