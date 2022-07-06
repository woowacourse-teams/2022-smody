import { Timer } from 'components/Timer';
import { TimerProps } from 'components/Timer/type';

export default {
  title: 'components/Timer',
  component: Timer,
};

export const DefaultTimer = (args: TimerProps) => <Timer {...args} />;

DefaultTimer.args = {
  certEndDate: new Date('2023-07-06T17:00:00'),
};
