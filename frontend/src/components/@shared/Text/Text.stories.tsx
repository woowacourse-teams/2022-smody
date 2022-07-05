import { Text } from 'components/@shared/Text';
import { TextProps } from 'components/@shared/Text/type';

export default {
  title: '@shared/Text',
  component: Text,
  argTypes: {
    fontWeight: {
      options: ['bold', 'normal'],
      control: { type: 'radio' },
    },
  },
};

export const DefaultText = (args: TextProps) => <Text {...args}>Text component</Text>;

DefaultText.args = {
  size: 20,
  color: '#000000',
  fontWeight: 'bold',
};
