import { AvailablePickedColor, FontSizeType } from 'types/style';

export type UnderLineTextProps = {
  fontSize: FontSizeType;
  fontColor: AvailablePickedColor;
  fontWeight?: 'normal' | 'bold';
  underLineColor: AvailablePickedColor;
};
