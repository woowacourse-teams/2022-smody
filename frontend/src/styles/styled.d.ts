import 'styled-components';
import { AvailablePickedColor } from 'types/style';

declare module 'styled-components' {
  export interface DefaultTheme {
    primary: AvailablePickedColor;
    secondary: AvailablePickedColor;
    background: AvailablePickedColor;
    surface: AvailablePickedColor;
    success: AvailablePickedColor;
    error: AvailablePickedColor;
    onPrimary: AvailablePickedColor;
    onSecondary: AvailablePickedColor;
    onBackground: AvailablePickedColor;
    onSurface: AvailablePickedColor;
    onSuccess: AvailablePickedColor;
    onError: AvailablePickedColor;
    disabled: AvailablePickedColor;
    disabledInput: AvailablePickedColor;
    border: AvailablePickedColor;
    successBorder: AvailablePickedColor;
    blur: AvailablePickedColor;
    mainText: AvailablePickedColor;
    backdrop: AvailablePickedColor;
    input: AvailablePickedColor;
    onInput: AvailablePickedColor;
    first: AvailablePickedColor;
    second: AvailablePickedColor;
    third: AvailablePickedColor;
  }
}
