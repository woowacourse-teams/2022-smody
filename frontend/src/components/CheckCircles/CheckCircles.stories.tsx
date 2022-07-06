import { CheckCircles } from 'components/CheckCircles';
import { CheckCirclesProps } from 'components/CheckCircles/type';

export default {
  title: 'Components/CheckCircles',
  component: CheckCircles,
  argTypes: {
    progressCount: {
      control: 'select',
      options: [0, 1, 2, 3],
    },
  },
};

export const DefaultCheckCircles = (args: CheckCirclesProps) => (
  <CheckCircles {...args} />
);

DefaultCheckCircles.args = {
  progressCount: 2,
};
