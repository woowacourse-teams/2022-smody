import { LinkText } from 'components/@shared/LinkText';
import { LinkTextProps } from 'components/@shared/LinkText/type';

export default {
  title: '@shared/LinkText',
  component: LinkText,
  argTypes: {
    fontWeight: {
      options: ['bold', 'normal'],
      control: { type: 'radio' },
    },
  },
};

export const DefaultLinkText = (args: LinkTextProps) => (
  <LinkText {...args}>로그인</LinkText>
);

DefaultLinkText.args = {
  size: 20,
  color: '#000000',
  fontWeight: 'bold',
  to: '/login',
};
