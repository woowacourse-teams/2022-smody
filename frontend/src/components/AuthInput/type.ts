import { ReactNode, HTMLInputTypeAttribute, ChangeEventHandler } from 'react';

export interface InputProps {
  icon?: ReactNode;
  type: HTMLInputTypeAttribute;
  label?: string;
  placeholder: string;
  value: string;
  onChange: ChangeEventHandler<HTMLInputElement>;
  isValidated?: boolean;
  message: string;
}

export interface InputContainerProps extends Pick<InputProps, 'isValidated'> {
  isFocus: boolean;
}
