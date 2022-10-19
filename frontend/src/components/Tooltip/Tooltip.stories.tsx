import { TooltipProps } from './type';

import { Tooltip } from 'components/Tooltip';

export default {
  Tooltip: 'components/Tooltip',
  component: Tooltip,
};

export const DefaultTooltip = (args: TooltipProps) => <Tooltip {...args} />;

DefaultTooltip.args = { ariaLabel: '툴팁 내용', xPosition: 'right' };
