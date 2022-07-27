import { AvailablePickedColor } from 'styles/type';

export interface horizontalPadding {
  pc: string;
  tablet: string;
  mobile: string;
}
export interface WrapperProps {
  bgColor: AvailablePickedColor;
  horizontalPadding: horizontalPadding;
}
