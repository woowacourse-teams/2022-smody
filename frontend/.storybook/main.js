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

    // storybook의 fileLoader 기본 설정 rule에서 svg를 제거하고, svgr 모듈을 사용하도록 수정한다. modules 배열은 사용하는 라이브러리를 오른쪽에서부터 읽기 때문에, override할 라이브러리를 unshift로 가장 앞에 넣어준다.
    const fileLoaderRule = config.module.rules.find(
      (rule) => rule.test && rule.test.test('.svg'),
    );
    fileLoaderRule.exclude = /\.svg$/;

    config.module.rules.unshift({
      test: /\.svg$/,
      enforce: 'pre',
      use: ['@svgr/webpack'],
    });

    return config;
  },
};
