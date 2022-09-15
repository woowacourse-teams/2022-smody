const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const path = require('path');
const isProd = process.env.NODE_ENV === 'production';
const isLocal = process.env.NODE_ENV === 'local';
const dotenv = require('dotenv');
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;
const { ESBuildMinifyPlugin } = require('esbuild-loader');

dotenv.config({ path: '.env' });

module.exports = {
  mode: isProd ? 'production' : 'development',
  devtool: isProd ? false : 'eval',
  performance: {
    hints: false,
  },
  entry: './src/index.tsx',
  output: {
    publicPath: '/',
    path: path.join(__dirname, '/dist'),
    filename: 'bundle.[chunkhash].js',
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
          filename: 'assets/[name][hash][ext]',
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
    new webpack.DefinePlugin({
      'process.env.BASE_URL': JSON.stringify(
        isProd ? process.env.PROD_BASE_URL : process.env.DEV_BASE_URL,
      ),
      'process.env.CLIENT_ID': isProd ? undefined : JSON.stringify(process.env.CLIENT_ID),
      'process.env.PUBLIC_KEY': isProd
        ? undefined
        : JSON.stringify(process.env.PUBLIC_KEY),
      'process.env.IS_LOCAL': JSON.stringify(isLocal),
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
