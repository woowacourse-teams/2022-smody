import { DarkModeButtonProps } from './type';

import { useArgs } from '@storybook/client-api';

import { DarkModeButton } from 'components/@shared/DarkModeButton';

export default {
  title: 'components/DarkModeButton',
  component: DarkModeButton,
  argTypes: {
    handleChange: {
      table: {
        disable: true,
      },
    },
  },
};

export const DefaultDarkModeButton = (args: Pick<DarkModeButtonProps, 'checked'>) => {
  const [_, updateArgs] = useArgs();

  const handleChange = () => {
    updateArgs({ checked: !args.checked });
  };

  return <DarkModeButton {...args} handleChange={handleChange} />;
};

DefaultDarkModeButton.args = {
  checked: true,
};
