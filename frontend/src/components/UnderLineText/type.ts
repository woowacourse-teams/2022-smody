import { AvailablePickedColor, FontSizeType } from 'styles/type';

export interface UnderLineTextProps {
  fontSize: FontSizeType;
  fontColor: AvailablePickedColor;
  fontWeight?: 'normal' | 'bold';
  underLineColor: AvailablePickedColor;
}
