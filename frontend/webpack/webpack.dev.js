const webpack = require('webpack');
const isLocal = process.env.NODE_ENV === 'local';
const dotenv = require('dotenv');
const { merge } = require('webpack-merge');
const common = require('./webpack.common');
const path = require('path');
const SpeedMeasurePlugin = require('speed-measure-webpack-plugin');
const smp = new SpeedMeasurePlugin();

dotenv.config({ path: path.join(__dirname, '../.env') });

module.exports = smp.wrap(
  merge(common, {
    mode: 'development',
    devtool: 'eval-cheap-module-source-map',
    cache: {
      type: 'filesystem',
    },
    module: {
      rules: [
        {
          test: /\.(js|jsx|ts|tsx)$/i,
          exclude: /node_modules/,
          loader: 'babel-loader',
          options: {
            cacheCompression: false,
            cacheDirectory: true,
            presets: [
              '@babel/preset-env',
              ['@babel/preset-react', { runtime: 'automatic' }],
              '@babel/preset-typescript',
            ],
            plugins: [['babel-plugin-styled-components']],
          },
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
      runtimeChunk: {
        name: (entrypoint) => `runtime-${entrypoint.name}`,
      },
    },
    plugins: [
      new webpack.DefinePlugin({
        'process.env.BASE_URL': JSON.stringify(process.env.DEV_BASE_URL),
        'process.env.CLIENT_ID': JSON.stringify(process.env.CLIENT_ID),
        'process.env.PUBLIC_KEY': JSON.stringify(process.env.PUBLIC_KEY),
        'process.env.IS_LOCAL': JSON.stringify(isLocal),
      }),
    ],
  }),
);
