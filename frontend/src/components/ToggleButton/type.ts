import { ChangeEventHandler } from 'react';

export interface ToggleButtonProps {
  checked: boolean;
  handleChange: ChangeEventHandler<HTMLInputElement>;
  disabled?: boolean;
}
