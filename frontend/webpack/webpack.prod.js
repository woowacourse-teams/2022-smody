const webpack = require('webpack');
const dotenv = require('dotenv');
const { ESBuildMinifyPlugin } = require('esbuild-loader');
const { merge } = require('webpack-merge');
const common = require('./webpack.common');
const path = require('path');

dotenv.config({ path: path.join(__dirname, '../.env') });

module.exports = merge(common, {
  mode: 'production',
  devtool: false,
  output: {
    filename: 'bundle.[chunkhash].js',
  },
  module: {
    rules: [
      {
        test: /\.(js|jsx|ts|tsx)$/i,
        exclude: /node_modules/,
        loader: 'esbuild-loader',
        options: {
          loader: 'tsx',
          target: 'es2020',
        },
      },
    ],
  },
  optimization: {
    splitChunks: {
      chunks: 'all',
    },
    minimizer: [
      '...',
      new ESBuildMinifyPlugin({
        target: 'es2020',
      }),
    ],
  },
  plugins: [
    new webpack.DefinePlugin({
      'process.env.BASE_URL': JSON.stringify(process.env.PROD_BASE_URL),
      'process.env.IS_LOCAL': false,
    }),
  ],
});
