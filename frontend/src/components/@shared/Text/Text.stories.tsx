import { Text } from 'components/@shared/Text';
import { TextProps } from 'components/@shared/Text/type';

export default {
  title: 'Text',
  component: Text,
  argTypes: {
    ref: {
      table: {
        disable: true,
      },
    },
    theme: {
      table: {
        disable: true,
      },
    },
    as: {
      table: {
        disable: true,
      },
    },
    forwardedAs: {
      table: {
        disable: true,
      },
    },
  },
};

export const DefaultText = (args: TextProps) => <Text {...args}>Text component</Text>;

DefaultText.args = {
  size: 10,
  color: '#fbdfdf',
};
