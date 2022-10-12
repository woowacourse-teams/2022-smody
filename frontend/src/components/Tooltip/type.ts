export type TooltipProps = {
  title: string;
  isOpenTooltip: boolean;
  toggleTooltip: () => void;
  xPosition?: 'left' | 'middle' | 'right';
  yPosition?: 'top' | 'bottom';
};
