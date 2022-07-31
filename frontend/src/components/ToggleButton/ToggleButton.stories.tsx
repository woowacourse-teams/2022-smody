import { ToggleButtonProps } from './type';

import { ToggleButton } from 'components/ToggleButton';

export default {
  title: 'components/ToggleButton',
  component: ToggleButton,
};

export const DefaultToggleButton = (args: ToggleButtonProps) => (
  <ToggleButton {...args}>🎁</ToggleButton>
);

let checkedValue = false;
DefaultToggleButton.args = {
  checked: checkedValue,
  handleChange: () => {
    checkedValue = !checkedValue;
    console.log(checkedValue);
  },
};
