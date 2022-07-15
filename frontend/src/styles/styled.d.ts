import 'styled-components';

import { AvailablePickedColor } from 'styles/type';

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
    border: AvailablePickedColor;
    blur: AvailablePickedColor;
    mainText: AvailablePickedColor;
    backdrop: AvailablePickedColor;
  }
}
