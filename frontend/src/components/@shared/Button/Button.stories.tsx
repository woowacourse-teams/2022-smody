import { Button } from 'components/@shared/Button';
import { ButtonProps } from 'components/@shared/Button/type';

export default {
  title: '@shared/Button',
  component: Button,
};

export const DefaultButton = (args: ButtonProps) => <Button {...args} />;

DefaultButton.args = {
  size: 'medium',
  children: '버튼',
};
