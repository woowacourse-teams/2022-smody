import styled, { css } from 'styled-components';

import { FlexBox } from 'components/@shared/FlexBox';
import { InputProps, InputContainerProps } from 'components/@shared/Input/type';
import { useInput } from 'components/@shared/Input/useInput';

import { ValidationMessage } from 'components/ValidationMessage';

export const Input = ({
  type = 'text',
  label,
  placeholder,
  value,
  onChange,
  isValidated,
  message,
  disabled,
  isTextArea = false,
  needWordLength,
  maxLength,
}: InputProps) => {
  const { isFocus, handleFocus, handleBlur } = useInput();

  return (
    <Wrapper>
      <Label htmlFor={label}>{label}</Label>
      <InputWrapper isFocus={isFocus} isValidated={isValidated}>
        {isTextArea ? (
          <TextAreaElement
            id={label}
            placeholder={placeholder}
            value={value}
            onChange={onChange}
            onFocus={handleFocus}
            onBlur={handleBlur}
            disabled={disabled}
          />
        ) : (
          <InputElement
            id={label}
            type={type}
            placeholder={placeholder}
            value={value}
            onChange={onChange}
            onFocus={handleFocus}
            onBlur={handleBlur}
            disabled={disabled}
          />
        )}
      </InputWrapper>
      <FlexBox justifyContent="space-between">
        <div>
          <ValidationMessage isValidated={isValidated} value={value} message={message} />
        </div>
        {needWordLength && <WordLength>{`${value?.length}/${maxLength}`}</WordLength>}
      </FlexBox>
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

const TextAreaElement = styled.textarea`
  ${({ theme }) => css`
    background-color: transparent;
    border: none;
    width: 100%;
    height: 10rem;
    margin-left: 0.5rem;
    outline: none;
    font-size: 1rem;
    color: ${theme.onInput};
    resize: none;

    &:disabled {
      color: ${theme.disabledInput};
    }
  `}
`;

const WordLength = styled.span`
  margin-top: 0.3rem;
`;
