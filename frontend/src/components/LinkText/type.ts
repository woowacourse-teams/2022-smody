import { TextProps } from 'components/@shared/Text/type';

export type FontSizeType = 10 | 11 | 12 | 14 | 16 | 20 | 24 | 32 | 40 | 48;

export interface LinkTextProps extends TextProps {
  to: string;
}
