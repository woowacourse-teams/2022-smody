import { UnderLineText } from 'components/UnderLineText';
import { UnderLineTextProps } from 'components/UnderLineText/type';

export default {
  title: 'components/UnderLineText',
  component: UnderLineText,
};

export const DefaultUnderLineText = (args: UnderLineTextProps) => (
  <UnderLineText {...args} />
);

DefaultUnderLineText.args = {
  fontSize: '20px',
  fontColor: '#000',
  underLineColor: '#549',
  children: '테스트',
};
