export type TabsProps = {
  tabList: { path: string; tabName: string }[];
};

export type TabProps = {
  isSelected: boolean;
  onClick: () => void;
  tabName: string;
};
