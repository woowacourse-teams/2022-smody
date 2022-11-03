import { PickType } from 'smody-library';

type TabType = { path: string; tabName: string };

export type TabsProps = {
  tabList: TabType[];
  ariaLabel?: string;
};

export type TabProps = {
  isSelected: boolean;
};

export type HandleClickTabFunc = (path: PickType<TabType, 'path'>) => void;
