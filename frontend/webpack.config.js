const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const path = require('path');
const dotenv = require('dotenv');
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;
const { ESBuildMinifyPlugin } = require('esbuild-loader');

dotenv.config({ path: '.env' });

module.exports = {
  devtool: process.env.NODE_ENV === 'production' ? false : 'eval-source-map',
  performance: {
    hints: false,
  },
  entry: './src/index.tsx',
  output: {
    publicPath: '/',
    path: path.join(__dirname, '/dist'),
    filename: 'bundle.[contenthash].js',
    clean: true,
  },
  resolve: {
    extensions: ['.js', '.jsx', '.ts', '.tsx'],
    modules: [path.resolve(__dirname, 'src'), 'node_modules'], // node_modules/보다 우선으로 검색할 디렉터리를 추가하는 것은 다음과 같습니다.
  },
  module: {
    rules: [
      // esbuild-loader는 babel-loader와 ts-loader의 대체제이다.
      // esbuild-loader를 통해 js 및 ts 코드를 target으로 설정한 ECMAScript 문법에 맞게 변환할 수 잇다.
      {
        test: /\.(js|jsx|ts|tsx)$/i,
        exclude: /node_modules/,
        loader: 'esbuild-loader',
        options: {
          loader: 'tsx',
          target: 'es2020',
        },
      },
      {
        test: /\.(png|webp)$/,
        type: 'asset',
        generator: {
          filename: 'assets/[name][contenthash][ext]',
        },
      },
      // CRA에서는 svg를 자동으로 처리해주지만, CRA를 사용하지 않은 경우 webpack config를 통한 사용 설정이 필요하다.
      {
        test: /\.svg$/,
        use: ['@svgr/webpack'],
      },
    ],
  },
  devServer: {
    historyApiFallback: true,
    port: 3000,
    hot: true,
    open: true,
  },
  optimization: {
    splitChunks: {
      chunks: 'all',
    },
    minimizer: [
      '...',
      new ESBuildMinifyPlugin({
        target: 'es2020',
        css: true,
      }),
    ],
  },
  plugins: [
    new webpack.ProvidePlugin({
      React: 'react',
    }),
    new HtmlWebpackPlugin({
      template: './public/index.html',
    }),
    new webpack.HotModuleReplacementPlugin(),
    new webpack.EnvironmentPlugin({
      BASE_URL:
        process.env.NODE_ENV === 'production'
          ? process.env.PROD_BASE_URL
          : process.env.LOCAL_BASE_URL,
      IS_LOCAL: process.env.NODE_ENV === 'local',
      IS_DEV: process.env.NODE_ENV === 'development' && process.env.NODE_ENV !== 'local',
      IS_PROD: process.env.NODE_ENV === 'production',
      CLIENT_ID:
        process.env.NODE_ENV === 'development' ? process.env.CLIENT_ID : undefined,
    }),
    new CopyWebpackPlugin({
      patterns: [
        { from: 'public/image', to: 'image' },
        { from: 'public/manifest.json', to: '.' },
        { from: 'public/pwaServiceWorker.js', to: '.' },
      ],
    }),
    new BundleAnalyzerPlugin(),
  ],
};
