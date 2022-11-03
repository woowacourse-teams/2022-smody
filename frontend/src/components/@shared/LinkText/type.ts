import { AvailablePickedColor, FontSizeType } from 'types/style';

export type LinkTextProps = {
  to: string;
  size?: FontSizeType;
  color: AvailablePickedColor;
  fontWeight?: 'normal' | 'bold';
};
