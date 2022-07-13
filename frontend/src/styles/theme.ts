import { DefaultTheme } from 'styled-components';

import COLOR from 'styles/color';

export const lightTheme: DefaultTheme = {
  primary: COLOR.PURPLE,
  onPrimary: COLOR.WHITE,

  secondary: COLOR.LIGHT_PURPLE,
  onSecondary: COLOR.WHITE,

  background: COLOR.WHITE,
  onBackground: COLOR.BLACK,

  surface: COLOR.WHITE,
  onSurface: COLOR.LIGHT_BLACK,

  success: COLOR.GREEN,
  onSuccess: COLOR.WHITE,

  error: COLOR.RED,
  onError: COLOR.WHITE,

  disabled: COLOR.GRAY,
  border: COLOR.LIGHT_GRAY,
  blur: COLOR.DARK_GRAY,
};
