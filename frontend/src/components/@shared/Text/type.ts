import { AvailablePickedColor, FontSizeType } from 'styles/type';

export interface TextProps {
  size?: FontSizeType;
  color: AvailablePickedColor;
  fontWeight?: 'normal' | 'bold';
}
