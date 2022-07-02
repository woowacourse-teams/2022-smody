import { AvailablePickedColor } from 'styles/type';

export type FontSizeType = 10 | 12 | 14 | 16 | 20 | 24 | 32 | 40 | 48;

export interface TextProps {
  size: FontSizeType;
  color: AvailablePickedColor;
}
