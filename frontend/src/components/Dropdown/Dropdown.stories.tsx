import { Bell } from 'components/Bell';
import { Dropdown } from 'components/Dropdown';
import { DropdownProps } from 'components/Dropdown/type';

export default {
  title: 'components/Dropdown',
  component: Dropdown,
};

export const DefaultDropdown = (args: DropdownProps) => <Dropdown {...args} />;

DefaultDropdown.args = {
  children: <Bell count={0} isSubscribed={true} />,
};
