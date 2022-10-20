import { ReactNode } from 'react';

export type TooltipProps = {
  xPosition?: 'left' | 'middle' | 'right';
  yPosition?: 'top' | 'bottom';
  icon?: ReactNode;
  ariaLabel: string;
  xDelta?: string;
  yDelta?: string;
  line?: number;
};
