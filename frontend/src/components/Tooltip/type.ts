import { ReactNode } from 'react';

export type TooltipProps = {
  isOpenTooltip: boolean;
  xPosition?: 'left' | 'middle' | 'right';
  yPosition?: 'top' | 'bottom';
  icon?: ReactNode;
  ariaLabel: string;
};
