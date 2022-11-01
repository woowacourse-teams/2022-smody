import { ToggleButtonProps } from './type';

import { useArgs } from '@storybook/client-api';

import { ToggleButton } from 'components/@shared/ToggleButton';

export default {
  title: '@shared/ToggleButton',
  component: ToggleButton,
  argTypes: {
    handleChange: {
      table: {
        disable: true,
      },
    },
  },
};

export const DefaultToggleButton = (args: Pick<ToggleButtonProps, 'checked'>) => {
  const [_, updateArgs] = useArgs();

  const handleChange = () => {
    updateArgs({ checked: !args.checked });
  };

  return <ToggleButton {...args} handleChange={handleChange} />;
};

DefaultToggleButton.args = {
  checked: true,
  disabled: false,
};
