import { DefaultTheme } from 'styled-components';

import COLOR from 'styles/color';

export const lightTheme: DefaultTheme = {
  primary: COLOR.PURPLE_500,
  onPrimary: COLOR.WHITE,

  secondary: COLOR.PURPLE_200,
  onSecondary: COLOR.WHITE,

  background: COLOR.WHITE,
  onBackground: COLOR.BLACK_900,

  surface: COLOR.WHITE,
  onSurface: COLOR.BLACK_900,

  success: COLOR.GREEN_800,
  onSuccess: COLOR.WHITE,

  error: COLOR.RED_900,
  onError: COLOR.WHITE,

  disabled: COLOR.LIGHT_GRAY_900,
  border: COLOR.LIGHT_GRAY_300,
  blur: COLOR.DARK_GRAY_800,
};
