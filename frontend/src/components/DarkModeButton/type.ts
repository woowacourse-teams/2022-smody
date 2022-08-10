import { ChangeEventHandler } from 'react';

export interface DarkModeButtonProps {
  checked: boolean;
  handleChange: ChangeEventHandler<HTMLInputElement>;
}
