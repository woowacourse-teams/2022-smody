import { Logo } from 'components/Logo';
import { LogoProps } from 'components/Logo/type';

export default {
  title: 'components/Logo',
  component: Logo,
};

export const DefaultLogo = (args: LogoProps) => <Logo {...args} />;

DefaultLogo.args = {
  width: '300',
};
