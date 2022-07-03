import CorrectIcon from 'assets/correct_icon.svg';
import IncorrectIcon from 'assets/incorrect_icon.svg';
import { useState } from 'react';
import styled, { css } from 'styled-components';

import {
  InputProps,
  ValidationMessageProps,
  InputContainerProps,
} from 'components/Input/type';

export const Input = ({
  icon,
  type,
  label,
  placeholder,
  value,
  onChange,
  isCorrect,
  message,
}: InputProps) => {
  const [isFocus, setIsFocus] = useState(false);

  return (
    <>
      <Label htmlFor={label}>{label}</Label>
      <InputWrapper isFocus={isFocus} isCorrect={isCorrect}>
        {icon}
        <InputElement
          id={label}
          type={type}
          placeholder={placeholder}
          value={value}
          onChange={onChange}
          onFocus={() => setIsFocus(true)}
          onBlur={() => setIsFocus(false)}
        />
      </InputWrapper>
      <ValidationMessage isCorrect={isCorrect} value={value} message={message} />
    </>
  );
};

Input.displayName = 'INputDis';

const Label = styled.label``;

const InputElement = styled.input``;

const ValidationMessage = ({ isCorrect, value, message }: ValidationMessageProps) => {
  if (typeof isCorrect === 'undefined') {
    return null;
  }

  if (value) {
    return null;
  }

  return (
    <ValidationWrapper>
      {isCorrect ? <CorrectIcon /> : <IncorrectIcon />}
      <Message isCorrect={isCorrect}>{message}</Message>
    </ValidationWrapper>
  );
};

const InputWrapper = styled.div<InputContainerProps>`
  ${({ theme, isFocus, isCorrect }) => css`
    display: flex;
    border: 1px solid ${theme.onSurface};
    ${isFocus &&
    css`
      border-color: ${isCorrect ? theme.primary : theme.error};
    `};
    border-radius: 5px;
    padding: 14px;
    margin-top: 8px;
  `}
`;

const ValidationWrapper = styled.div`
  display: flex;
  align-items: center;
  height: 30px;
  padding-left: 2px;
`;

const Message = styled.span<Required<Pick<InputProps, 'isCorrect'>>>`
  ${({ theme, isCorrect }) => css`
    color: ${isCorrect ? theme.primary : theme.error};
    margin-left: 6px;
  `}
`;
