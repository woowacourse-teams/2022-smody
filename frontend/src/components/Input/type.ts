import { HTMLInputTypeAttribute, ChangeEventHandler } from 'react';

export interface InputProps {
  type: HTMLInputTypeAttribute;
  label?: string;
  placeholder: string;
  value: string | undefined;
  onChange: ChangeEventHandler<HTMLInputElement>;
  isValidated?: boolean;
  message: string;
  disabled?: boolean;
}

export interface InputContainerProps extends Pick<InputProps, 'isValidated'> {
  isFocus: boolean;
}
