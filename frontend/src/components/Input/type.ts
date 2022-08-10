import { HTMLInputTypeAttribute, ChangeEventHandler } from 'react';

export interface InputProps {
  type?: HTMLInputTypeAttribute;
  label?: string;
  placeholder: string;
  value: string | undefined;
  onChange: ChangeEventHandler<HTMLInputElement | HTMLTextAreaElement>;
  isValidated?: boolean;
  message: string;
  disabled?: boolean;
  isTextArea?: boolean;
}

export interface InputContainerProps extends Pick<InputProps, 'isValidated'> {
  isFocus: boolean;
}
