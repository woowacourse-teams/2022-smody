import { ReactNode, HTMLInputTypeAttribute, ChangeEventHandler } from 'react';

export interface InputProps {
  icon?: ReactNode;
  type: HTMLInputTypeAttribute;
  label: string;
  placeholder: string;
  value: string;
  onChange: ChangeEventHandler<HTMLInputElement>;
  isCorrect?: boolean;
  message: string;
}

export interface InputContainerProps extends Pick<InputProps, 'isCorrect'> {
  isFocus: boolean;
}

export type ValidationMessageProps = Pick<InputProps, 'value' | 'isCorrect' | 'message'>;
