import { useState } from 'react';
import styled, { css } from 'styled-components';

import { InputProps, InputContainerProps } from 'components/Input/type';
import { ValidationMessage } from 'components/ValidationMessage';

export const Input = ({
  type,
  label,
  placeholder,
  value,
  onChange,
  isValidated,
  message,
  disabled,
}: InputProps) => {
  const [isFocus, setIsFocus] = useState(false);

  return (
    <Wrapper>
      <Label htmlFor={label}>{label}</Label>
      <InputWrapper isFocus={isFocus} isValidated={isValidated}>
        <InputElement
          id={label}
          type={type}
          placeholder={placeholder}
          value={value}
          onChange={onChange}
          onFocus={() => setIsFocus(true)}
          onBlur={() => setIsFocus(false)}
          disabled={disabled}
        />
      </InputWrapper>
      <ValidationMessage isValidated={isValidated} value={value} message={message} />
    </Wrapper>
  );
};

const Wrapper = styled.div`
  width: 100%;
  outline: none;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
`;

const Label = styled.label`
  ${({ theme }) => css`
    font-size: 1rem;
    color: ${theme.disabled};
  `}
`;

const InputWrapper = styled.div<InputContainerProps>`
  ${({ theme, isFocus, isValidated }) => css`
    display: flex;
    border: 1px solid ${theme.input};
    background-color: ${theme.input};

    ${isFocus &&
    css`
      border-color: ${typeof isValidated === 'undefined'
        ? theme.primary
        : isValidated
        ? theme.success
        : theme.error};
    `};
    border-radius: 20px;
    padding: 1rem;
    margin-top: 0.5rem;
  `}
`;

const InputElement = styled.input`
  ${({ theme }) => css`
    background-color: transparent;
    border: none;
    width: 100%;
    margin-left: 0.5rem;
    outline: none;
    font-size: 1rem;
    color: ${theme.onInput};

    &:disabled {
      color: ${theme.disabledInput};
    }
  `}
`;
