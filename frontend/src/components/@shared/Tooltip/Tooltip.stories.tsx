import { TooltipProps } from './type';

import { Tooltip } from 'components/@shared/Tooltip';

export default {
  title: '@shared/Tooltip',
  component: Tooltip,
};

export const DefaultTooltip = (args: TooltipProps) => <Tooltip {...args} />;

DefaultTooltip.args = { ariaLabel: '툴팁 내용', xPosition: 'right' };
