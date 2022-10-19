import { UnderLineText } from 'components/UnderLineText';
import { UnderLineTextProps } from 'components/UnderLineText/type';

export default {
  title: 'components/UnderLineText',
  component: UnderLineText,
  argTypes: {
    fontWeight: {
      options: ['bold', 'normal'],
      control: { type: 'radio' },
    },
  },
};

export const DefaultUnderLineText = (args: UnderLineTextProps) => (
  <UnderLineText {...args}>UnderLineText</UnderLineText>
);

DefaultUnderLineText.args = {
  fontSize: '24',
  fontColor: '#3b2d81',
  underLineColor: '#7054FE',
};
