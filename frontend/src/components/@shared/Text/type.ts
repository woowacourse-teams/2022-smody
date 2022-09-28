import { AvailablePickedColor, FontSizeType } from 'types/style';

export type TextProps = {
  size?: FontSizeType;
  color: AvailablePickedColor;
  fontWeight?: 'normal' | 'bold';
};
