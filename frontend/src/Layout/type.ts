import { AvailablePickedColor } from 'types/style';

export type horizontalPadding = {
  pc: string;
  tablet: string;
  mobile: string;
};
export type OutletWrapperProps = {
  bgColor: AvailablePickedColor;
  horizontalPadding: horizontalPadding;
};
