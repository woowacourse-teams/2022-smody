import { DefaultTheme } from 'styled-components';

import COLOR from 'styles/color';

export const lightTheme: DefaultTheme = {
  primary: COLOR.PURPLE,
  onPrimary: COLOR.WHITE,

  secondary: COLOR.LIGHT_PURPLE,
  onSecondary: COLOR.DARK_PURPLE,

  background: COLOR.WHITE,
  onBackground: COLOR.BLACK,

  surface: COLOR.WHITE,
  onSurface: COLOR.LIGHTEST_BLACK,

  success: COLOR.PURPLE,
  onSuccess: COLOR.WHITE,

  error: COLOR.RED,
  onError: COLOR.WHITE,

  input: COLOR.LIGHT_GRAY,
  onInput: COLOR.BLACK,

  disabled: COLOR.GRAY,
  disabledInput: COLOR.DARKER_GRAY,
  border: COLOR.LIGHT_GRAY,
  blur: COLOR.LIGHT_GRAY,
  mainText: COLOR.GRAY,

  backdrop: COLOR.OPAQUE_BLACK,

  accent: COLOR.LIGHT_BLUE,
  onAccent: COLOR.BLACK,
};

export const darkTheme: DefaultTheme = {
  primary: COLOR.PURPLE,
  onPrimary: COLOR.WHITE,

  secondary: COLOR.LIGHTER_BLACK,
  onSecondary: COLOR.LIGHT_PURPLE,

  background: COLOR.BLACK,
  onBackground: COLOR.DARKER_WHITE,

  surface: COLOR.LIGHTEST_BLACK,
  onSurface: COLOR.DARKER_WHITE,

  success: COLOR.PURPLE,
  onSuccess: COLOR.WHITE,

  error: COLOR.RED,
  onError: COLOR.WHITE,

  input: COLOR.LIGHTEST_BLACK,
  onInput: COLOR.DARKER_WHITE,

  disabled: COLOR.GRAY,
  disabledInput: COLOR.DARKER_GRAY,
  border: COLOR.LIGHTEST_BLACK,
  blur: COLOR.LIGHT_GRAY,
  mainText: COLOR.GRAY,

  backdrop: COLOR.OPAQUE_BLACK,

  accent: COLOR.DARK_BLUE,
  onAccent: COLOR.WHITE,
};
