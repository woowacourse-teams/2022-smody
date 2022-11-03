import { UnderLineText } from 'components/@shared/UnderLineText';
import { UnderLineTextProps } from 'components/@shared/UnderLineText/type';

export default {
  title: '@shared/UnderLineText',
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
