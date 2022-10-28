const webpack = require('webpack');
const dotenv = require('dotenv');
const { merge } = require('webpack-merge');
const common = require('./webpack.common');
const path = require('path');

dotenv.config({ path: path.join(__dirname, '../.env') });

module.exports = merge(common, {
  mode: 'production',
  devtool: false,
  cache: {
    type: 'filesystem',
  },
  target: ['web', 'es5'],
  module: {
    rules: [
      {
        test: /\.(js|jsx|ts|tsx)$/i,
        exclude: /node_modules/,
        loader: 'babel-loader',
        options: {
          presets: [
            [
              '@babel/preset-env',
              {
                targets: { safari: '11' },
                useBuiltIns: 'usage',
                corejs: {
                  version: 3,
                },
              },
            ],
            ['@babel/preset-react', { runtime: 'automatic' }],
            '@babel/preset-typescript',
          ],
        },
      },
    ],
  },
  optimization: {
    splitChunks: {
      chunks: 'all',
    },
  },
  plugins: [
    new webpack.DefinePlugin({
      'process.env.BASE_URL': JSON.stringify(process.env.PROD_BASE_URL),
      'process.env.IS_LOCAL': false,
    }),
  ],
});
