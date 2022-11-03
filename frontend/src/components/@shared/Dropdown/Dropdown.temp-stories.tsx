import { Dropdown } from 'components/@shared/Dropdown';
import { DropdownProps } from 'components/@shared/Dropdown/type';

import { Bell } from 'components/Bell';

export default {
  title: 'components/Dropdown',
  component: Dropdown,
};

export const DefaultDropdown = (args: DropdownProps) => <Dropdown {...args} />;

DefaultDropdown.args = {
  children: <Bell count={0} isSubscribed={true} />,
  updateIsSubscribed: (updatedIsSubscribed: boolean) => {},
  updateNotificationCount: (updatedNotificationCount: number) => {},
};
