import { CSSProperties } from 'react';

export interface FlexBoxProps {
  flexDirection?: Pick<CSSProperties, 'flexDirection'>;
  justifyContent?: Pick<CSSProperties, 'justifyContent'>;
  alignItems?: Pick<CSSProperties, 'alignItems'>;
  gap?: Pick<CSSProperties, 'gap'>;
}
