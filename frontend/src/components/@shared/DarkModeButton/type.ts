import { ChangeEventHandler } from 'react';

export type DarkModeButtonProps = {
  checked: boolean;
  handleChange: ChangeEventHandler<HTMLInputElement>;
};
