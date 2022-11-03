import { ChangeEventHandler } from 'react';

export type ToggleButtonProps = {
  checked: boolean;
  handleChange: ChangeEventHandler<HTMLInputElement>;
  disabled?: boolean;
};
