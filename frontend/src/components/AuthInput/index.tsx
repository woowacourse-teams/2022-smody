import { useState } from 'react';
import styled, { css } from 'styled-components';

import { InputProps, InputContainerProps } from 'components/AuthInput/type';
import { ValidationMessage } from 'components/ValidationMessage';

export const AuthInput = ({
  icon,
  type,
  label,
  placeholder,
  value,
  onChange,
  isValidated,
  message,
}: InputProps) => {
  const [isFocus, setIsFocus] = useState(false);

  return (
    <Wrapper>
      <Label htmlFor={label}>{label}</Label>
      <InputWrapper isFocus={isFocus} isValidated={isValidated}>
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
      <ValidationMessage isValidated={isValidated} value={value} message={message} />
    </Wrapper>
  );
};

const Wrapper = styled.div`
  width: 100%;
  outline: none;
`;

const Label = styled.label``;

const InputWrapper = styled.div<InputContainerProps>`
  ${({ theme, isFocus, isValidated }) => css`
    display: flex;
    border: 1px solid ${theme.onSurface};
    ${isFocus &&
    css`
      border-color: ${isValidated ? theme.success : theme.error};
    `};
    border-radius: 1rem;
    padding: 1rem;
    margin-top: 0.5rem;
  `}
`;

const InputElement = styled.input`
  border: none;
  width: 100%;
  margin-left: 0.5rem;
  background-color: transparent;
  outline: none;
  font-size: 1rem;
`;
