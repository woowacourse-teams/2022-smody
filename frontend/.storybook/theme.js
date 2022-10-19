import logo from './logo.png';

import { create } from '@storybook/theming/create';

export default create({
  base: 'light',
  colorPrimary: ' #7B61FF',
  colorSecondary: '#7B61FF',

  // UI
  appBg: 'white',
  appContentBg: 'white',
  appBorderColor: 'grey',
  appBorderRadius: 4,

  // Text colors
  textColor: 'black',
  textInverseColor: 'white',

  // Toolbar default and active colors
  barTextColor: '#343434',
  barSelectedColor: '#7054FE',
  barBg: 'white',

  // Form colors
  inputBg: 'white',
  inputBorder: '#3b2d81',
  inputTextColor: 'black',
  inputBorderRadius: 4,

  brandTitle: 'Smody',
  brandUrl: 'https://www.smody.co.kr',
  brandImage: logo,
});
