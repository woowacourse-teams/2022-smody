import type { Config } from '@jest/types';

const config: Config.InitialOptions = {
  bail: 1,
  verbose: true,
  moduleFileExtensions: ['js', 'json', 'jsx', 'ts', 'tsx', 'json'],
  // jest는 기본적으로 node 환경에서 동작한다. transform 설정은 node 환경에서 지원하지 않는 구문(jsx, ts, tsx 등)을 일반 JS로 변환한다.
  transform: {
    '^.+\\.(js|jsx|ts|tsx)?$': 'ts-jest',
    '^.+\\.svg$': 'jest-transformer-svg',
  },
  testEnvironment: 'jsdom',
  testMatch: ['<rootDir>/**/*.test.(js|jsx|ts|tsx)'],
  // transformIgnorePatterns: ['<rootDir>/node_modules/'],
  moduleDirectories: ['node_modules', 'src'],

  moduleNameMapper: {
    '\\.(jpg|ico|jpeg|png|gif|eot|otf|webp|svg|ttf|woff|woff2|mp4|webm|wav|mp3|m4a|aac|oga)$':
      '<rootDir>/mocks/fileMock.js',
    '\\.(css|less)$': '<rootDir>/mocks/fileMock.js',
  },
  rootDir: './src',
  setupFilesAfterEnv: ['<rootDir>/setupTests.ts'],
};

export default config;
