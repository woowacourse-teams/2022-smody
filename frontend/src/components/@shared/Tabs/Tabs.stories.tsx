import { Tabs } from 'components/@shared/Tabs';
import { TabsProps } from 'components/@shared/Tabs/type';

import { CLIENT_PATH } from 'constants/path';

export default {
  title: 'components/Tabs',
  component: Tabs,
};

export const DefaultTabs = (args: TabsProps) => <Tabs {...args} />;

const tabList = [
  {
    path: CLIENT_PATH.CHALLENGE_EVENT,
    tabName: '[우테코]이벤트',
  },
  {
    path: CLIENT_PATH.CHALLENGE_SEARCH,
    tabName: '전체검색',
  },
];

DefaultTabs.args = {
  tabList: tabList,
};
