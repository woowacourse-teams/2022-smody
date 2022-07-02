const path = require('path');

module.exports = {
  stories: ['../src/**/*.stories.mdx', '../src/**/*.stories.@(js|jsx|ts|tsx)'],
  addons: [
    '@storybook/addon-links',
    '@storybook/addon-essentials',
    '@storybook/addon-interactions',
  ],
  framework: '@storybook/react',
  core: {
    builder: '@storybook/builder-webpack5',
  },
  webpackFinal: async (config) => {
    // '../src'는 현재 파일 위치에서 baseUrl로 설정한 위치까지의 상대 경로가 들어간다.
    config.resolve.modules = [
      ...config.resolve.modules,
      path.resolve(__dirname, '../src'),
    ];

    return config;
  },
};
