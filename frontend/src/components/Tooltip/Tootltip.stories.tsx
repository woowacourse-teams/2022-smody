import { TooltipProps } from './type';

import { Tooltip } from 'components/Tooltip';

// import { TooltipProps } from 'components/Tooltip/type';

export default {
  Tooltip: 'components/Tooltip',
  component: Tooltip,
};

export const DefaultTooltip = (args: TooltipProps) => <Tooltip {...args} />;

DefaultTooltip.args = { title: '툴팁 내용', isOpenTooltip: true };
