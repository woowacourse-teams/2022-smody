import { CycleDetailItemProps } from './type';

import { CycleDetailItem } from 'components';

export default {
  title: 'Components/CycleDetailItem',
  component: CycleDetailItem,
};

export const DefaultCycleDetailItem = (args: CycleDetailItemProps) => (
  <CycleDetailItem {...args} />
);

DefaultCycleDetailItem.args = {
  progressImage:
    'https://health.chosun.com/site/data/img_dir/2018/03/07/2018030700812_2.jpg',
  progressTime: '2022-07-01T17:00:00',
  description: '알찼다.',
};
