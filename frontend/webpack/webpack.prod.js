const webpack = require('webpack');
const dotenv = require('dotenv');
const { merge } = require('webpack-merge');
const common = require('./webpack.common');
const path = require('path');
const ForkTsCheckerWebpackPlugin = require('fork-ts-checker-webpack-plugin');
const SpeedMeasurePlugin = require('speed-measure-webpack-plugin');
const smp = new SpeedMeasurePlugin();

dotenv.config({ path: path.join(__dirname, '../.env') });

module.exports = smp.wrap(
  merge(common, {
    mode: 'production',
    devtool: false,
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
                  useBuiltIns: 'usage',
                  corejs: {
                    version: 3,
                  },
                },
              ],
              ['@babel/preset-react', { runtime: 'automatic' }],
              '@babel/preset-typescript',
            ],
            plugins: [
              [
                'babel-plugin-styled-components',
                {
                  displayName: false,
                  minify: true,
                  transpileTemplateLiterals: true,
                  pure: true,
                },
              ],
            ],
          },
        },
      ],
    },
    plugins: [
      new ForkTsCheckerWebpackPlugin(),
      new webpack.DefinePlugin({
        'process.env.BASE_URL': JSON.stringify(process.env.PROD_BASE_URL),
        'process.env.IS_LOCAL': false,
        'process.env.CLIENT_ID': null,
        'process.env.PUBLIC_KEY': null,
      }),
    ],
  }),
);
