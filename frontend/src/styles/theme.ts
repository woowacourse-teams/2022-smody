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
  onSurface: COLOR.LIGHT_BLACK,

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
};

export const darkTheme: DefaultTheme = {
  primary: COLOR.PURPLE,
  onPrimary: COLOR.WHITE,

  secondary: COLOR.BLACK,
  onSecondary: COLOR.DARK_PURPLE,

  background: COLOR.BLACK,
  onBackground: COLOR.DARKER_WHITE,

  surface: COLOR.LIGHT_BLACK,
  onSurface: COLOR.DARKER_WHITE,

  success: COLOR.PURPLE,
  onSuccess: COLOR.WHITE,

  error: COLOR.RED,
  onError: COLOR.WHITE,

  input: COLOR.LIGHT_BLACK,
  onInput: COLOR.DARKER_WHITE,

  disabled: COLOR.GRAY,
  disabledInput: COLOR.DARKER_GRAY,
  border: COLOR.LIGHT_BLACK,
  blur: COLOR.LIGHT_GRAY,
  mainText: COLOR.GRAY,

  backdrop: COLOR.OPAQUE_BLACK,
};
