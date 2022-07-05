import { Button } from 'components/Button';
import { ButtonProps } from 'components/Button/type';

export default {
  title: 'Components/Button',
  component: Button,
};

export const DefaultButton = (args: ButtonProps) => <Button {...args} />;

DefaultButton.args = {
  size: 'medium',
  children: '버튼',
};
