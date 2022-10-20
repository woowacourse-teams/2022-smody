import { Bell } from 'components/Bell';
import { BellProps } from 'components/Bell/type';

export default {
  title: 'components/Bell',
  component: Bell,
};

export const DefaultBell = (args: BellProps) => <Bell {...args} />;
DefaultBell.args = {
  count: 0,
};
