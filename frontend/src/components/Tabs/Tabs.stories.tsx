import { Tabs } from 'components/Tabs';
import { TabsProps } from 'components/Tabs/type';

import { CLIENT_PATH } from 'constants/path';

export default {
  title: 'components/Tabs',
  component: Tabs,
};

export const DefaultTabs = (args: TabsProps) => <Tabs {...args} />;

const tabList = [
  {
    path: CLIENT_PATH.EVENT,
    tabName: '[우테코]이벤트',
  },
  {
    path: CLIENT_PATH.SEARCH,
    tabName: '전체검색',
  },
];

DefaultTabs.args = {
  tabList: tabList,
};
