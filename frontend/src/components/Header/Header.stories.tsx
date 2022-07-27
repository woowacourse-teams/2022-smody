import { Header } from 'components/Header';
import { HeaderProps } from 'components/Header/type';

export default {
  title: 'Components/Header',
  component: Header,
};

export const DefaultHeader = (args: HeaderProps) => <Header {...args} />;
DefaultHeader.args = {
  bgColor: '#F5F3FF',
};
