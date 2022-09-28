import { HTMLInputTypeAttribute, ChangeEventHandler } from 'react';

export type InputProps = {
  type?: HTMLInputTypeAttribute;
  label?: string;
  placeholder: string;
  value: string | undefined;
  onChange: ChangeEventHandler<HTMLInputElement | HTMLTextAreaElement>;
  isValidated?: boolean;
  message: string;
  disabled?: boolean;
  isTextArea?: boolean;
  needWordLength?: boolean;
  maxLength?: number;
};

export type InputContainerProps = Pick<InputProps, 'isValidated'> & {
  isFocus: boolean;
};

export type WordLengthProps = {
  isMargin: boolean;
};
