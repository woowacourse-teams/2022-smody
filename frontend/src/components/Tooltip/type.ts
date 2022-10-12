import { ReactNode, MouseEventHandler } from 'react';

export type TooltipProps = {
  isOpenTooltip: boolean;
  openTooltip: MouseEventHandler<HTMLButtonElement>;
  closeTooltip: MouseEventHandler<HTMLButtonElement>;
  closeTooltipOnBg: MouseEventHandler<HTMLDivElement>;
  xPosition?: 'left' | 'middle' | 'right';
  yPosition?: 'top' | 'bottom';
  icon?: ReactNode;
  ariaLabel: string;
};
